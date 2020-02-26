package com.acm.handler;


import com.acm.authorization.manager.TokenManager;
import com.acm.authorization.model.TokenModel;
import com.acm.common.constant.StatusCode;
import com.acm.common.utils.Base64Util;
import com.acm.pojo.db.Permission;
import com.acm.pojo.vo.PermissionVO;
import com.acm.pojo.vo.ResultBean;
import com.acm.pojo.vo.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Api(description = "权限管理", tags = "PermissionHandler", basePath = "/system")
@Controller
@RequestMapping("/system/permission")
public class PermissionHandler extends BaseHandler{
    private static final Logger LOGGER = Logger.getLogger(PermissionHandler.class);

    @Autowired
    private TokenManager tokenManager;

    @ApiOperation(value = "查询列表", notes = "参数：token")
    @RequestMapping(value = "all", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getPermissionList(@RequestBody PermissionVO requestPermission) {
        ResultBean resultBean = new ResultBean();
        try {
            String tk = requestPermission.getToken();
            userOperationService.checkTokenNotEmpty(requestPermission.getToken());
            TokenModel tokenModel = tokenManager.getToken(Base64Util.decodeData(tk));
            String [] pus = tokenModel.getPermissionCode().split("&");
            List<Permission> permissionsList = new ArrayList<>();
            for (String pu : pus){
                if (pu.equals("au:"))continue;
                Permission permission = permissionService.selectByPrimaryKey(Integer.parseInt(pu.substring(1)));
                if (permission != null){//删除权限后，权限码不会即时更新，要判断权限是否为空
                    permissionsList.add(permission);
                }
            }
            resultBean.setData(permissionsList);
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg(e.getMessage());
            LOGGER.error("查询列表失败！", e);
        }
        return resultBean;
    }

    @ApiOperation(value = "查询角色拥有的权限", notes = "参数：token")
    @RequestMapping(value = "get", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getPermissionByRoleId(@RequestBody RoleVO requestRole) {
        ResultBean resultBean = new ResultBean();
        try {
            String tk = requestRole.getToken();
            userOperationService.checkTokenNotEmpty(requestRole.getToken());
            TokenModel tokenModel = tokenManager.getToken(Base64Util.decodeData(tk));
            List permissions = roleService.getPermissionIdByRoleId(requestRole.getrId());
            resultBean.setData(permissions);
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Request permission Failed！");
            LOGGER.error("查询失败" + requestRole.getrId(), e);
        }
        return resultBean;
    }



    @ApiOperation(value = "添加权限",notes ="参数：pName,pUrl,pType,token。添加权限后系统管理员角色权限增加，将更新token，回传一个新token")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean addPermission(@RequestBody PermissionVO requestPermission) {
        ResultBean resultBean = new ResultBean();
        try {
            Permission permission = new Permission();
            permission.setpName(requestPermission.getpName());
            permission.setpUrl(requestPermission.getpUrl());
            permission.setpType(requestPermission.getpType());
            permissionService.insertPermission(permission.getpName(),permission.getpUrl(),permission.getpType());
            Integer pId = permissionService.getIdByNameAndUrl(permission.getpName(),permission.getpUrl());
            List pList = new ArrayList<Integer>();pList.add(pId);
            roleService.grantPrivileges(1,pList);
            String tk = requestPermission.getToken();
            TokenModel tokenModel = tokenManager.getToken(Base64Util.decodeData(tk));
            String [] authorityCode = authorityManager.getAuthorityCode(tokenModel.getUserId());
            tokenManager.deleteToken(tokenModel.getUserId());
            TokenModel token = tokenManager.createToken(tokenModel.getUserId(), authorityCode[0], authorityCode[1]);
            resultBean.setData(Base64Util.encodeData(token.getToken()));
        }
        catch (Exception e){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            if (e.getMessage().contains("SQLIntegrityConstraintViolationException")){
                resultBean.setMsg("该表达式已有对应权限，添加失败！");
            }
            else{
                resultBean.setMsg("Add permission Failed！");
            }
            LOGGER.error("添加失败！", e);
        }
        return resultBean;
    }


    @ApiOperation(value = "删除权限", notes = "参数：pId")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean deletePermission(@RequestBody PermissionVO requestPermission) {
        ResultBean resultBean = new ResultBean();
        try {
            permissionService.robPermission(requestPermission.getpId());
            permissionService.deleteByPrimaryKey(requestPermission.getpId());
        }
        catch (Exception e){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Delete permission Failed！");
            LOGGER.error("删除失败！", e);
        }
        return resultBean;
    }

    @ApiOperation(value = "修改权限", notes = "参数：pId，pName,pUrl,pType")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean updatePermission(@RequestBody PermissionVO requestPermission) {
        ResultBean resultBean = new ResultBean();
        try {
            Permission permission = new Permission();
            permission.setpId(requestPermission.getpId());
            permission.setpName(requestPermission.getpName());
            permission.setpUrl(requestPermission.getpUrl());
            permission.setpType(requestPermission.getpType());
            permissionService.updateByPrimaryKey(permission);
        }
        catch (Exception e){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Edit permission Failed！");
            LOGGER.error("编辑失败！", e);
        }
        return resultBean;
    }
}