package com.acm.handler;

import com.acm.authorization.manager.AuthorityManager;
import com.acm.service.IPermissionService;
import com.acm.service.IRoleService;
import com.acm.service.IUserOperationService;
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

    @Autowired
    IRoleService roleService;

    @Autowired
    IPermissionService permissionService;

    @Autowired
    IUserOperationService userOperationService;

    @Autowired
    AuthorityManager authorityManager;

}
