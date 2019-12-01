package com.acm.common.aspect;

import com.acm.authorization.manager.AuthorityManager;
import com.acm.authorization.manager.TokenManager;
import com.acm.authorization.model.TokenModel;
import com.acm.common.annotation.IgnoreSecurity;
import com.acm.common.constant.Constants;
import com.acm.common.exception.AuthorityException;
import com.acm.common.exception.TokenException;
import com.acm.common.utils.Base64Util;
import com.acm.common.utils.WebContextUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;


/**
 * token有效性判断切面
 * @author leeyom
 * @date 2017年10月19日 10:41
 * @updatedate 2019年11月30日
 */
@Component
@Aspect
public class SecurityAspect {

    private static final Logger LOGGER = Logger.getLogger(SecurityAspect.class);

    @Autowired
    TokenManager tokenManager;

    @Autowired
    AuthorityManager authorityManager;

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object execute(ProceedingJoinPoint pjp) throws Throwable {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        // 从切点上获取目标方法
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();

        // *******************************放行swagger相关的请求url，开发阶段打开，生产环境注释掉*******************************

        HttpServletRequest request = WebContextUtil.getRequest();
        URL requestUrl = new URL(request.getRequestURL().toString());
        if (requestUrl.getPath().contains("configuration")) {
            return pjp.proceed();
        }
        if (requestUrl.getPath().contains("swagger")) {
            return pjp.proceed();
        }
        if (requestUrl.getPath().contains("api/")) {
            return pjp.proceed();
        }
        // ************************************************************************************************************

        // 若目标方法忽略了安全性检查,则直接调用目标方法
        if (method.isAnnotationPresent(IgnoreSecurity.class)) {
            return pjp.proceed();
        }

        // 从 request header 中获取当前 token
        String authentication = request.getHeader(Constants.DEFAULT_TOKEN_NAME);
        TokenModel tokenModel = tokenManager.getToken(Base64Util.decodeData(authentication));

        // 检查 token 有效性(检查是否登录)
        if (!tokenManager.checkToken(tokenModel)) {
            String message = "token " + Base64Util.decodeData(authentication) + " is invalid！！！";
            LOGGER.debug("message : " + message);
            throw new TokenException(message);
        }

        //检查权限
        if (!authorityManager.checkAuthority(tokenModel.getAuthorityCode(), requestUrl.getPath())){
            String message = tokenModel.getUserId() + "try to enter " + requestUrl.getPath() + "without permission";
            LOGGER.debug("message : " + message);
            throw new AuthorityException("无权访问");
        }

        // 调用目标方法
        return pjp.proceed();
    }
}
