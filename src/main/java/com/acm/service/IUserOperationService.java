package com.acm.service;


/**
 * 统一判断用户操作的一些问题，避免handler层代码过于臃肿
 *
 * 未完成
 */

public interface IUserOperationService {

    boolean checkOperationToUserByToken(String operatorToken, int target);

    boolean checkTokenNotEmpty(String token);

}
