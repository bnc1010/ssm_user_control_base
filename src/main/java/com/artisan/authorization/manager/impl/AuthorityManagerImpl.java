package com.artisan.authorization.manager.impl;

import com.artisan.authorization.manager.AuthorityManager;

public class AuthorityManagerImpl implements AuthorityManager {
    /**
     * @param userId, @param target:target api url
     * 检查权限
     **/
    @Override
    public boolean checkAuthority(long userId, String target) {
        return false;
    }
}
