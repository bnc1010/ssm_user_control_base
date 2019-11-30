package com.acm.authorization.manager;


public interface AuthorityManager {
    boolean checkAuthority(long userId, String target);
}
