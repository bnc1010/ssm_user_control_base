package com.acm.handler;

import com.acm.common.constant.StatusCode;
import com.acm.pojo.db.User;
import com.acm.pojo.vo.ResultBean;
import com.acm.pojo.vo.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户管理
 * @author Leeyom Wang
 * @date 2017年10月26日 15:20
 * 添加分页
 * 2019年11月30日
 */
@Api(description = "user管理", tags = "UserHandler", basePath = "/users")
@Controller
@RequestMapping("/users")
public class UserHandler extends BaseHandler {

    private static final Logger LOGGER = Logger.getLogger(UserHandler.class);

    @ApiOperation(value = "查询列表", notes = "可选参数：pageNum,pageSize")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getUserList(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        try {
            List<User> userList = null;
            int pageNum = 1;
            int pageSize = 10;
            if (requestUser.getPageNum() != null && requestUser.getPageSize() != null){
                pageNum = requestUser.getPageNum();
                pageSize = requestUser.getPageSize();
            }
            userList = userService.selectAll(pageNum, pageSize);
            resultBean.setData(userList);
            resultBean.setEtxra(new PageInfo<User>(userList));
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Request User list Failed！");
            LOGGER.error("查询列表失败！", e);
        }
        return resultBean;
    }

    @ApiOperation(value = "根据id查询指定的User", notes = "参数:uId")
    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResultBean getUser(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        try {
            User user = userService.selectByPrimaryKey(requestUser.getuId());
            resultBean.setData(user);
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Failed to request User details！");
            LOGGER.error("查询指定的User失败！参数信息：id = " + requestUser.getuId(), e);
        }
        return resultBean;
    }

    @ApiOperation(value = "新增User", notes = "参数:userName,password")
    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResultBean add(@RequestBody @Valid User user, BindingResult result) {
        ResultBean resultBean = new ResultBean();

        if (userService.isExist(user.getUserName())){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setData("该用户名已注册");
        }
        else {
            StringBuilder errorMsg = new StringBuilder("");
            if (result.hasErrors()) {
                List<ObjectError> list = result.getAllErrors();
                for (ObjectError error : list) {
                    errorMsg = errorMsg.append(error.getCode()).append("-").append(error.getDefaultMessage()).append(";");
                }
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setData(errorMsg.toString());
                return resultBean;
            }
            try {
                userService.insert(user);
            } catch (Exception e) {
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setMsg("新增User失败！");
                LOGGER.error("新增User失败！参数信息：User = " + user.toString(), e);
            }
        }
        return resultBean;
    }

    @ApiOperation(value = "更新指定的User", notes = "uId,需要更改的字段")
    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResultBean update(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        try {
            User oldUser = userService.selectByPrimaryKey(requestUser.getuId());
            oldUser.setAge(requestUser.getAge());
            oldUser.setBirthday(requestUser.getBirthday());
            oldUser.setPassword(requestUser.getPassword());
            oldUser.setSex(requestUser.getSex());
            oldUser.setUserName(requestUser.getUserName());
            userService.updateByPrimaryKey(oldUser);
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Update User failed！");
            LOGGER.error("更新失败！参数信息：id = " + requestUser.getuId() + ",User = " + requestUser.toString(), e);
        }
        return resultBean;
    }

    @ApiOperation(value = "根据id物理删除指定的User，需谨慎！", notes = "参数：uId")
    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ResultBean delete(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        try {
            userService.deleteByPrimaryKey(requestUser.getuId());
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Delete User failed！");
            e.printStackTrace();
            LOGGER.error("删除失败！参数信息：id = " + requestUser.getuId(), e);
        }
        return resultBean;
    }

}

