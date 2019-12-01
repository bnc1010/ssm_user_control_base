package com.acm.authorization.manager.impl;

import com.acm.authorization.manager.TokenManager;
import com.acm.authorization.model.TokenModel;
import com.acm.common.constant.Constants;
import com.acm.common.utils.Base64Util;
import com.acm.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 通过Redis存储和验证token的实现类
 * @author leeyom
 * @date 2017年10月19日 10:41
 * @see com.acm.authorization.manager.TokenManager
 */
@Component
public class RedisTokenManager implements TokenManager {

    private RedisTemplate<Long, String> redis;
    private final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");

    @Autowired
    public void setRedis(RedisTemplate<Long, String> redis) {
        this.redis = redis;
        //泛型设置成Long后必须更改对应的序列化方案
        redis.setKeySerializer(new JdkSerializationRedisSerializer());
    }

    @Override
    public TokenModel createToken(long userId, String authorityCode) {
        //uuid
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //时间戳
        String timestamp = SDF.format(new Date());
        //token => authorityCode_userId_timestamp_uuid;
        String token = userId + "_" + timestamp + "_" + uuid + "_" + authorityCode;
        TokenModel model = new TokenModel(userId, uuid, timestamp, authorityCode);
        //存储到redis并设置过期时间(有效期为2个小时)
        redis.boundValueOps(userId).set(Base64Util.encodeData(token), Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return model;
    }

    @Override
    public TokenModel getToken(String authentication) {
        if (authentication == null || authentication.length() == 0) {
            return null;
        }
        String[] param = authentication.split("_");
        if (param.length != 4) {
            return null;
        }

        //使用userId和源token简单拼接成的token，可以增加加密措施
        long userId = Long.parseLong(param[0]);
        String timestamp = param[1];
        String uuid = param[2];
        String authorityCode = param[3];
        return new TokenModel(userId, uuid, timestamp, authorityCode);
    }

    @Override
    public boolean checkToken(TokenModel model) {
        if (model == null) {
            return false;
        }
        System.out.println("model:" + model.getToken());

        String token = redis.boundValueOps(model.getUserId()).get();
        System.out.println("redis:" + token);
        if (token == null || !(Base64Util.decodeData(token)).equals(model.getToken())) {
            return false;
        }
        //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间(2个小时)
        redis.boundValueOps(model.getUserId()).expire(Constants.TOKEN_EXPIRES_HOUR, TimeUnit.HOURS);
        return true;
    }

    @Override
    public void deleteToken(long userId) {
        if (redis.hasKey(userId)) {
            redis.delete(userId);
        }
    }

    @Override
    public boolean hasToken(long userId) {
        String token = redis.boundValueOps(userId).get();
        return StringUtils.notNull(token);
    }


}
