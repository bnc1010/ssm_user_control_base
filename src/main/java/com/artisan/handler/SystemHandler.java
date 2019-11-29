package com.artisan.handler;


import com.artisan.common.constant.StatusCode;
import com.artisan.pojo.db.User;
import com.artisan.pojo.vo.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(description = "系统管理", tags = "SystemHandler", basePath = "/system")
@Controller
@RequestMapping("/system")
public class SystemHandler {
    private static final Logger LOGGER = Logger.getLogger(UserHandler.class);

    @ApiOperation(value = "查询列表")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResultBean getUserList() {
        ResultBean resultBean = new ResultBean();
        return resultBean;
    }
}
