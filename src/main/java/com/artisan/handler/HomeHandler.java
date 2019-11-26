package com.artisan.handler;

import com.artisan.authorization.manager.TokenManager;
import com.artisan.authorization.model.TokenModel;
import com.artisan.common.annotation.IgnoreSecurity;
import com.artisan.common.constant.StatusCode;
import com.artisan.common.utils.Base64Util;
import com.artisan.common.utils.MD5Util;
import com.artisan.pojo.db.User;
import com.artisan.pojo.vo.ResultBean;
import com.artisan.pojo.vo.UserVO;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 平台登录登出
 * @author Leeyom Wang
 * @date 2017年10月19日 12:06
 */
@Api(description = "平台登录注册", tags = "HomeHandler", basePath = "/home")
@Controller
@RequestMapping("/home")
public class HomeHandler extends BaseHandler {

    private static final Logger LOGGER = Logger.getLogger(HomeHandler.class);

    @Autowired
    private TokenManager tokenManager;

    /**
     * 登录
     * @param userName 用户名
     * @param password 密码，MD5加密
     * @return 登录结果信息
     */
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "用户密码", required = true, dataType = "String"),
    })
    @ResponseBody
    @IgnoreSecurity
    public ResultBean login(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password) {
        ResultBean resultBean = new ResultBean();
        UserVO userVO = new UserVO();
        try {
            password = MD5Util.encrypt(password);
//            System.out.println(password);
            User user = userService.getUser(userName, password);
            if (user == null) {
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setMsg("Login failed, user name or password error！");
            } else {
                TokenModel token;
                // 判断用户是否已经登录过，如果登录过，就将redis缓存中的token删除，重新创建新的token值，保证一个用户在一个时间段只有一个可用 Token
                if (tokenManager.hasToken(user.getuId())) {
                    //清除过时的token
                    tokenManager.deleteToken(user.getuId());
                    //创建token
                    token = tokenManager.createToken(user.getuId());
                } else {
                    //创建token
                    token = tokenManager.createToken(user.getuId());
                }
                userVO.setuId(user.getuId());
                userVO.setuName(user.getUserName());
                userVO.setAge(user.getAge());
                userVO.setBirthday(user.getBirthday());
                userVO.setSex(user.getSex());
                //将token返回给客户端
                userVO.setToken(Base64Util.encodeData(token.getToken()));
                resultBean.setData(userVO);
            }
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Login failed, user name or password error！");
            LOGGER.error("用户登录失败！参数信息：userName = " + userName + ",password = " + password, e);
            e.printStackTrace();
        }
        return resultBean;
    }

    /**
     * 登出
     * @param userId 用户id
     * @return
     */
    @ApiOperation(value = "用户登出", notes = "用户登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "long")
    })
    @IgnoreSecurity
    @ResponseBody
    public ResultBean logout(@RequestParam(value = "userId") Long userId) {
        ResultBean resultBean = new ResultBean();
        try {
            tokenManager.deleteToken(userId);
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Logout failed!");
            LOGGER.error("遇到未知错误，退出失败！", e);
        }
        return resultBean;
    }

    /**
     *注册
     * 2019/11/4
     * bnc
     **/
    @ApiOperation(value = "用户注册")
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @IgnoreSecurity
    public ResultBean add(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password) {
        ResultBean resultBean = new ResultBean();
        if (userService.isExist(userName)){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setData("该用户名已注册");
        }
        else{
            User user = new User();
            user.setUserName(userName);
            user.setPassword(MD5Util.encrypt(password));
            try {
                userService.insert(user);
            } catch (Exception e) {
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setMsg("注册失败！");
                LOGGER.error("新增User失败！参数信息：User = " + user.toString(), e);
            }
        }
        return resultBean;
    }
}
