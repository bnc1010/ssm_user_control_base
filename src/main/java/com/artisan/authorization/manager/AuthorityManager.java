package com.artisan.authorization.manager;


import org.springframework.stereotype.Service;

public interface AuthorityManager {
    boolean checkAuthority(long userId, String target);
}
