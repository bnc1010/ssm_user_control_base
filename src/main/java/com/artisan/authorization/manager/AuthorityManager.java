package com.artisan.authorization.manager;

public interface AuthorityManager {
    boolean checkAuthority(long userId, String target);
}
