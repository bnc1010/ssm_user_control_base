package com.acm.handler;


import com.acm.authorization.manager.TokenManager;
import com.acm.authorization.model.TokenModel;
import com.acm.common.constant.StatusCode;
import com.acm.common.utils.Base64Util;
import com.acm.pojo.db.Role;
import com.acm.pojo.db.User;
import com.acm.pojo.vo.ResultBean;
import com.acm.pojo.vo.RoleVO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @ApiOperation(value = "添加角色",notes ="参数：roleName角色名, rType角色类型，rRank")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean addRole(@RequestBody RoleVO requestRole) {
        ResultBean resultBean = new ResultBean();
        try {
            Role role = new Role();
            role.setRoleName(requestRole.getRoleName());
            role.setRType(requestRole.getrType());
            role.setrRank(requestRole.getrRank());
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
            Role role = roleService.selectByPrimaryKey(requestRole.getrId());
            List<Integer> uIds = roleService.getUserIdByRoleId(role.getrId());

            for (int uId : uIds){
                User user = userService.selectByPrimaryKey(uId);
                if (user.getuRank() >= role.getrRank()){
                    List<Role> roles = roleService.getRoleByUserId(user.getuId());
                    int minRank = 10000;
                    for (Role _role : roles){
                        if (_role.getrId().equals(role.getrId())){
                            continue;
                        }
                        else{
                            minRank = minRank > _role.getrRank() ? _role.getrRank() : minRank;
                        }
                    }
                    if (minRank != role.getrRank()){
                        userService.updateURank(user.getuId(), minRank);
                    }
                }
            }
            roleService.robRole(role.getrId());
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
            for (Integer rs:requestRole.getpIds()){
                boolean ff = false;
                for (String au:aus){
                    if (au.equals("au:")){
                        continue;
                    }
                    if (rs.equals(Integer.parseInt(au.substring(1)))){
                        pIds.add(rs);
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

    @ApiOperation(value = "给角色去权限", notes = "参数：rId,权限id")
    @RequestMapping(value = "/drop", method = RequestMethod.POST)
    @ResponseBody
    public ResultBean drop(@RequestBody RoleVO requestRole) {
        ResultBean resultBean = new ResultBean();
        try {
            Map map = new HashMap();
            List<Integer> pIds = roleService.getPermissionIdByRoleId(requestRole.getrId());
            for (int pid:pIds){
                map.put(pid, true);
            }
            for (int rr : requestRole.getpIds()){
                if (!map.containsKey(rr)){
                    resultBean.setCode(StatusCode.HTTP_FAILURE);
                    resultBean.setMsg("该角色没有权限，permissionId:" + rr);
                    return resultBean;
                }
            }
            for (int rr : requestRole.getpIds()){
                roleService.deleteByRoleIdAndPermissionId(requestRole.getrId(), rr);
            }
        }
        catch (Exception e){
            resultBean.setCode(StatusCode.HTTP_FAILURE);
            resultBean.setMsg("drop permission of role Failed！");
            LOGGER.error("去权限失败！", e);
        }
        return resultBean;
    }

}