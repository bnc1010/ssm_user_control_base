package com.acm.handler;

import com.acm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 统一注入service实例
 * @author Leeyom Wang
 * @date 2017年10月26日 16:14
 */
@Controller
public class BaseHandler {

    @Autowired
    IUserService userService;

}
