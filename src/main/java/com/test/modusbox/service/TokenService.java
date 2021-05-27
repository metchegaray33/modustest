package com.test.modusbox.service;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import com.test.modusbox.model.dto.TransactionDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Prefix of "Redis" and "Token"
     */
    private static final String IDEMPOTENT_TOKEN_PREFIX = "idempotent_token:";

    /**
     * Create a Token, save it to Redis, and return the Token
     *
     * @param transactionDTO The ﹣ value ﹣ value  used for auxiliary verification
     * @return Generated Token string
     */
    public String generateToken(TransactionDTO transactionDTO) {
        //Instantiate to generate the ﹣ ID ﹣ tool object
        String token = UUID.randomUUID().toString();
        //Set the Key stored in "Redis"
        String key = IDEMPOTENT_TOKEN_PREFIX + token;
        //Store "Token" to "Redis" and set the expiration time to 5 minutes
        redisTemplate.opsForValue().set(key, transactionDTO.getTransactionId(), 5, TimeUnit.MINUTES);
        //Return to 'Token'
        return key;
    }

    /**
     * Verify the correctness of the Token
     *
     * @param token token character string
     * @param value value Auxiliary verification information stored in Redis
     * @return Validation results
     */
    public boolean validToken(String token, String value) {
        //Set the "Lua" script, where "KEYS[1] is" key "and" KEYS[2] is "value"
        String script = "if redis.call('get', KEYS[1]) == KEYS[2] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        //Splice "Key" according to "Key" prefix
        String key = token;
        //Execute Lua script
        Long result = redisTemplate.execute(redisScript, Arrays.asList(key, value));
        //According to the returned result, judge whether the match is successful and delete the "Redis" key value pair. If the result is not empty and 0, the verification is passed
        if (result != null && result != 0L) {
            log.info("verification token={},key={},value={} success", token, key, value);
            return true;
        }
        log.info("verification token={},key={},value={} fail", token, key, value);
        return false;
    }

}