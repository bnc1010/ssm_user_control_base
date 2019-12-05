package com.acm.handler;


import com.acm.pojo.vo.ResultBean;
import com.acm.pojo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(description = "一些判断接口", tags = "HelperHandler", basePath = "/helper")
@Controller
@RequestMapping("/helper")
public class HelperHandler {
    private static final Logger LOGGER = Logger.getLogger(UserHandler.class);

    @ApiOperation(value = "查询是否有权进入管理界面")
    @RequestMapping(value = "/checkAdmin", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getUserList(@RequestBody UserVO requestUser) {
        ResultBean resultBean = new ResultBean();
        return resultBean;
    }
}
