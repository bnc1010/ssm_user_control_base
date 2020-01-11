package com.acm.handler;

import com.acm.authorization.manager.TokenManager;
import com.acm.authorization.model.TokenModel;
import com.acm.common.constant.StatusCode;
import com.acm.common.utils.Base64Util;
import com.acm.common.utils.MD5Util;
import com.acm.pojo.db.Role;
import com.acm.pojo.db.User;
import com.acm.pojo.vo.ResultBean;
import com.acm.pojo.vo.UserVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @RequestMapping(value = "/all", method = RequestMethod.POST)
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
            resultBean.setEtxra(new PageInfo<>(userList));
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Request User list Failed！");
            LOGGER.error("查询列表失败！", e);
        }
        return resultBean;
    }

    @ApiOperation(value = "根据id查询指定的User", notes = "参数:uId")
    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.POST)
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
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResultBean add(@RequestBody @Valid User user, BindingResult result) {
        ResultBean resultBean = new ResultBean();

        if (userService.isExist(user.getUserName())){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("该用户名已注册");
        }
        else {
            StringBuilder errorMsg = new StringBuilder("");
            if (result.hasErrors()) {
                List<ObjectError> list = result.getAllErrors();
                for (ObjectError error : list) {
                    errorMsg = errorMsg.append(error.getCode()).append("-").append(error.getDefaultMessage()).append(";");
                }
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setMsg(errorMsg.toString());
                return resultBean;
            }
            try {
                user.setPassword(MD5Util.encrypt(user.getPassword()));
                userService.insert(user);
            } catch (Exception e) {
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setMsg("新增User失败！");
                LOGGER.error("新增User失败！参数信息：User = " + user.toString(), e);
            }
        }
        return resultBean;
    }

    @ApiOperation(value = "根据id查询指定的User", notes = "参数:uId")
    @ResponseBody
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public ResultBean getUserRole(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        try {
            String tk = requestUser.getToken();
            userOperationService.checkOperationToUserByToken(tk, requestUser.getuId());
            List<Integer> rIds = roleService.getRoleIdByUserId(requestUser.getuId());
            resultBean.setData(rIds);
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg(e.getMessage());
            LOGGER.error("查询指定的User失败！参数信息：id = " + requestUser.getuId(), e);
        }
        return resultBean;
    }


    @ApiOperation(value = "更新指定的User", notes = "uId,需要更改的字段")
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultBean update(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        try {
            String tk = requestUser.getToken();
            userOperationService.checkOperationToUserByToken(tk, requestUser.getuId());
            User oldUser = userService.selectByPrimaryKey(requestUser.getuId());
            oldUser.setAge(requestUser.getAge());
            oldUser.setBirthday(requestUser.getBirthday());
            oldUser.setSex(requestUser.getSex());
            userService.updateByPrimaryKey(oldUser);
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg(e.getMessage());
            LOGGER.error("更新失败！参数信息：id = " + requestUser.getuId() + ",User = " + requestUser.toString(), e);
        }
        return resultBean;
    }



    @ApiOperation(value = "根据id物理删除指定的Role，需谨慎！", notes = "参数：uId,token")
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResultBean delete(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        try {
            String tk = requestUser.getToken();
            userOperationService.checkOperationToUserByToken(tk, requestUser.getuId());
            userService.deleteByUserId(requestUser.getuId());
            userService.deleteByPrimaryKey(requestUser.getuId());
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg(e.getMessage());
            e.printStackTrace();
            LOGGER.error("删除失败！参数信息：id = " + requestUser.getuId(), e);
        }
        return resultBean;
    }

    @Autowired
    private TokenManager tokenManager;

    @ApiOperation(value = "根据uid赋角色", notes = "参数：uId，roleCode数组,token")
    @ResponseBody
    @RequestMapping(value = "/grant", method = RequestMethod.POST)
    public ResultBean grantUser(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        try {
            String tk = requestUser.getToken();
            if (tk == null){
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setMsg("token无效");
                return resultBean;
            }
            TokenModel tokenModel = tokenManager.getToken(Base64Util.decodeData(tk));
            String [] rus = tokenModel.getRoleCode().split("&");
            List<Integer> rIds = new ArrayList<>();
            int minRank = 10000;
            User nowUser = userService.getUser(tokenModel.getUserId());
            User targetUser = userService.getUser(requestUser.getuId());
            if (targetUser == null){
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setMsg("操作对象不存在!");
                return resultBean;
            }

            if (tokenModel.getRoleCode().contains("a1")){//系统管理员情况
                for (Integer rs:requestUser.getrIds()){
                    Role role = roleService.selectByPrimaryKey(rs);
                    if (role != null){
                        minRank = minRank > role.getrRank() ? role.getrRank() : minRank;
                        rIds.add(role.getrId());
                    }
                    else{
                        resultBean.setCode(StatusCode.HTTP_FAILURE);
                        resultBean.setMsg("角色" + rs + "不存在!");
                        return resultBean;
                    }
                }
            }
            else{
                if (targetUser.getuRank() <= nowUser.getuRank()){
                    resultBean.setCode(StatusCode.HTTP_FAILURE);
                    resultBean.setMsg("无权操作该用户！");
                    return resultBean;
                }
                else {
                    for (Integer rs:requestUser.getrIds()){
                        boolean ff = false;
                        for (String au:rus){
                            if (au.equals("ru:")){
                                continue;
                            }
                            if (rs.equals(Integer.parseInt(au.substring(1)))){
                                Role role = roleService.selectByPrimaryKey(rs);
                                if (role.getrRank() > nowUser.getuRank()){
                                    rIds.add(role.getrId());
                                    minRank = minRank > role.getrRank() ? role.getrRank() : minRank;
                                }
                                else{
                                    resultBean.setCode(StatusCode.HTTP_FAILURE);
                                    resultBean.setMsg("角色级别过高！");
                                    return resultBean;
                                }
                                ff = true;
                                break;
                            }
                        }
                        if (!ff){
                            resultBean.setCode(StatusCode.HTTP_FAILURE);
                            resultBean.setMsg("存在越权行为！");
                            return resultBean;
                        }
                    }
                }
            }
            if (minRank < targetUser.getuRank()){
                userService.updateURank(targetUser.getuId(), minRank);
            }
            userService.grantPrivileges(requestUser.getuId(),rIds);
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Grant User failed！");
            e.printStackTrace();
            LOGGER.error("赋角色失败 id = " + requestUser.getuId(), e);
        }
        return resultBean;
    }

    @ApiOperation(value = "根据uid去角色", notes = "参数：uId，roleCode数组,token")
    @ResponseBody
    @RequestMapping(value = "/drop", method = RequestMethod.POST)
    public ResultBean dropRole(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        try {
            String tk = requestUser.getToken();
            if (tk == null) {
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setMsg("token无效");
                return resultBean;
            }
            TokenModel tokenModel = tokenManager.getToken(Base64Util.decodeData(tk));

            User nowUser = userService.getUser(tokenModel.getUserId());
            User targetUser = userService.getUser(requestUser.getuId());

            if (targetUser == null){
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setMsg("操作对象不存在!");
                return resultBean;
            }

            if (targetUser.getuRank() <= nowUser.getuRank()){
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setMsg("无权对该用户操作！");
                return resultBean;
            }
            int minRank = 10000;
            Map map = new HashMap();
            List<Role> roles = roleService.getRoleByUserId(targetUser.getuId());
            for (Integer rs : requestUser.getrIds()){
                boolean ff = false;
                for (Role role : roles){
                    if (rs.equals(role.getrId())){
                        ff = true;
                        map.put(role.getrId(),true);
                        break;
                    }
                }
                if (!ff){
                    resultBean.setCode(StatusCode.HTTP_FAILURE);
                    resultBean.setMsg("该用户无角色,code:" + rs);
                    return resultBean;
                }
            }
            for (Role role : roles){
                if (!map.containsKey(role.getrId())){
                    minRank = minRank > role.getrRank() ? role.getrRank() : minRank;
                }
            }
            if (minRank != targetUser.getuRank()){
                userService.updateURank(targetUser.getuId(), minRank);
            }
            for (Integer rs : requestUser.getrIds()){
                userService.deleteByUserIdAndRoleId(targetUser.getuId(), rs);
            }
        }
        catch (Exception e){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("drop role of User failed！");
            e.printStackTrace();
            LOGGER.error("去角色失败 id = " + requestUser.getuId(), e);
        }
        return resultBean;
    }

    @ApiOperation(value = "根据uId重置密码", notes = "参数：uId,token")
    @ResponseBody
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ResultBean RestPassword(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        try{
            String tk = requestUser.getToken();
            userOperationService.checkOperationToUserByToken(tk, requestUser.getuId());
            userService.resetPassword(requestUser.getuId(), MD5Util.encrypt("123456"));
        }
        catch (Exception e){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg(e.getMessage());
            e.printStackTrace();
            LOGGER.error("去角色失败 id = " + requestUser.getuId(), e);
        }
        return resultBean;
    }

}

