package im.juejin.redis;

import javax.annotation.Resource;

import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    
//    private static final String LOCK_VALUE = "lockValue";
//    
//    public boolean lock(String key, long expireSecond) {
//        return stringRedisTemplate.getConnectionFactory().getConnection().set(key.getBytes(),
//                LOCK_VALUE.getBytes(), Expiration.seconds(expireSecond), SetOption.SET_IF_ABSENT);
//    }
//
//    public void unlock(String key) {
//        stringRedisTemplate.delete(key);
//    }
    
}
