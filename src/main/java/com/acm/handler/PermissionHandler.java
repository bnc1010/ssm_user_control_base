package com.acm.handler;


import com.acm.common.constant.StatusCode;
import com.acm.pojo.db.Permission;
import com.acm.pojo.db.Role;
import com.acm.pojo.vo.PermissionVO;
import com.acm.pojo.vo.ResultBean;
import com.acm.pojo.vo.RoleVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(description = "权限管理", tags = "PermissionHandler", basePath = "/system")
@Controller
@RequestMapping("/system/permission")
public class PermissionHandler extends BaseHandler{
    private static final Logger LOGGER = Logger.getLogger(PermissionHandler.class);

    @ApiOperation(value = "查询列表")
    @RequestMapping(value = "all", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getPermissionList(@RequestBody PermissionVO requestPermission) {
        ResultBean resultBean = new ResultBean();
        try {
            List<Permission> permissionsList = null;
            int pageNum = 1;
            int pageSize = 10;
            if (requestPermission.getPageNum() != null && requestPermission.getPageSize() != null){
                pageNum = requestPermission.getPageNum();
                pageSize = requestPermission.getPageSize();
            }
            permissionsList = permissionService.selectAll(pageNum, pageSize);
            resultBean.setData(permissionsList);
            resultBean.setEtxra(new PageInfo<>(permissionsList));
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Request permission list Failed！");
            LOGGER.error("查询列表失败！", e);
        }
        return resultBean;
    }
}