package com.acm.service.impl;

import com.acm.authorization.manager.TokenManager;
import com.acm.authorization.model.TokenModel;
import com.acm.common.exception.AuthorityException;
import com.acm.common.utils.Base64Util;
import com.acm.pojo.db.User;
import com.acm.service.IUserOperationService;
import com.acm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userOperationService")

public class UserOperationServiceImpl implements IUserOperationService {

    @Autowired
    IUserService userService;

    @Autowired
    TokenManager tokenManager;

    @Override
    public boolean checkOperationToUserByToken(String operatorToken, int target) {
        if (operatorToken == null){
            throw new AuthorityException("token为空");
        }

        TokenModel tokenModel = tokenManager.getToken(Base64Util.decodeData(operatorToken));

        if (tokenModel == null){
            throw new AuthorityException("token无效");
        }

        User operator = userService.getUser(tokenModel.getUserId());

        if (operator == null){
            throw new AuthorityException("token无效");
        }

        User targetUser = userService.getUser(target);
        if (targetUser == null){
            throw new AuthorityException("目标用户无效，id：" + target);
        }

        if (targetUser.getuRank() <= operator.getuRank()){
            throw new AuthorityException("权限不足，无法完成该操作");
        }

        return true;
    }
}
