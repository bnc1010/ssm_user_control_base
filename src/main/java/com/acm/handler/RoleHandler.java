package com.acm.handler;


import com.acm.authorization.manager.TokenManager;
import com.acm.authorization.model.TokenModel;
import com.acm.common.constant.StatusCode;
import com.acm.common.utils.Base64Util;
import com.acm.dao.RoleMapper;
import com.acm.pojo.db.Role;
import com.acm.pojo.vo.ResultBean;
import com.acm.pojo.vo.RoleVO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Api(description = "角色管理", tags = "RoleHandler", basePath = "/system")
@Controller
@RequestMapping("/system/role")
public class RoleHandler extends BaseHandler{
    private static final Logger LOGGER = Logger.getLogger(RoleHandler.class);

    @Autowired
    private TokenManager tokenManager;

    @ApiOperation(value = "查询列表")
    @RequestMapping(value = "all", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean getRoleList(@RequestBody RoleVO requestRole) {
        ResultBean resultBean = new ResultBean();
        try {
            String tk = requestRole.getToken();
            TokenModel tokenModel = tokenManager.getToken(Base64Util.decodeData(tk));
            String [] aus = tokenModel.getRoleCode().split("&");
            List<Role> roleList = new ArrayList<>();
            if (tokenModel.getRoleCode().contains("a1")){
                roleList = roleService.selectAll(1, 100000);
            }
            else{
                for (String au:aus){
                    if (au.equals("ru:"))continue;
                    Role role = roleService.selectByPrimaryKey(Integer.parseInt(au.substring(1)));
                    roleList.add(role);
                }
            }

            resultBean.setData(roleList);
        } catch (Exception e) {
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Request role list Failed！");
            LOGGER.error("查询列表失败！", e);
        }
        return resultBean;
    }


    @ApiOperation(value = "添加角色",notes ="参数：roleName角色名, rType角色类型")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean addRole(@RequestBody RoleVO requestRole) {
        ResultBean resultBean = new ResultBean();
        try {
            Role role = new Role();
            role.setRoleName(requestRole.getRoleName());
            role.setRType(requestRole.getrType());
            roleService.insert(role);
        }
        catch (Exception e){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Add role Failed！");
            LOGGER.error("添加失败！", e);
        }
        return resultBean;
    }


    @ApiOperation(value = "删除角色", notes = "参数：rId")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean deleteRole(@RequestBody RoleVO requestRole) {
        ResultBean resultBean = new ResultBean();
        try {
            roleService.deleteByPrimaryKey(requestRole.getrId());
        }
        catch (Exception e){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Delete role Failed！");
            LOGGER.error("删除失败！", e);
        }
        return resultBean;
    }



    @ApiOperation(value = "给角色赋权限", notes = "参数：rId,权限码数组,token")
    @RequestMapping(value = "grant", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean grantPrivilege(@RequestBody RoleVO requestRole) {
        ResultBean resultBean = new ResultBean();
        try {
            String tk = requestRole.getToken();
            TokenModel tokenModel = tokenManager.getToken(Base64Util.decodeData(tk));
            String [] aus = tokenModel.getPermissionCode().split("&");

            List<Integer> pIds = new ArrayList<>();
            for (String rs:requestRole.getpCodes()){
                boolean ff = false;
                for (String au:aus){
                    if (rs.equals(au)){
                        pIds.add(Integer.parseInt(rs.substring(1)));
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
            roleService.grantPrivileges(requestRole.getrId(),pIds);
        }
        catch (Exception e){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("Grant role Failed！");
            LOGGER.error("赋权失败！", e);
        }
        return resultBean;
    }
}