package com.acm.handler;

import com.acm.authorization.manager.AuthorityManager;
import com.acm.authorization.manager.TokenManager;
import com.acm.authorization.model.TokenModel;
import com.acm.common.annotation.IgnoreSecurity;
import com.acm.common.constant.StatusCode;
import com.acm.common.utils.Base64Util;
import com.acm.common.utils.MD5Util;
import com.acm.pojo.db.User;
import com.acm.pojo.vo.ResultBean;
import com.acm.pojo.vo.UserVO;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private AuthorityManager authorityManager;

    /**
     * 登录
     * @return 登录结果信息
     */
    @ApiOperation(value = "用户登录", notes = "参数：userName,password")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @IgnoreSecurity
    public ResultBean login(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        UserVO userVO = new UserVO();
        try {
            requestUser.setPassword(MD5Util.encrypt(requestUser.getPassword()));
//            System.out.println(password);
            User user = userService.getUser(requestUser.getUserName(), requestUser.getPassword());
            if (user == null) {
                resultBean.setCode(StatusCode.HTTP_FAILURE);
                resultBean.setMsg("Login failed, user name or password error！");
            } else {
                TokenModel token;
                String authorityCode = authorityManager.getAuthorityCode(user.getuId());
                // 判断用户是否已经登录过，如果登录过，就将redis缓存中的token删除，重新创建新的token值，保证一个用户在一个时间段只有一个可用 Token
                if (tokenManager.hasToken(user.getuId())) {
                    //清除过时的token
                    tokenManager.deleteToken(user.getuId());
                    //创建token
                    token = tokenManager.createToken(user.getuId(), authorityCode);
                } else {
                    //创建token
                    token = tokenManager.createToken(user.getuId(), authorityCode);
                }
                userVO.setuId(user.getuId());
                userVO.setUserName(user.getUserName());
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
            LOGGER.error("用户登录失败！参数信息：userName = " + requestUser.getUserName() + ",password = " + requestUser.getPassword(), e);
            e.printStackTrace();
        }
        return resultBean;
    }

    /**
     * 登出
     *
     */
    @ApiOperation(value = "用户登出", notes = "参数：uId")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean logout(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        try {
            tokenManager.deleteToken(requestUser.getuId());
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
    @ApiOperation(value = "用户注册", notes = "参数：userName,password")
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @IgnoreSecurity
    public ResultBean add(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        if (userService.isExist(requestUser.getUserName())){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setData("该用户名已注册");
        }
        else{
            User user = new User();
            user.setUserName(requestUser.getUserName());
            user.setPassword(MD5Util.encrypt(requestUser.getPassword()));
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
