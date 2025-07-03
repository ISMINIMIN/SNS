package minzdev.sns.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import minzdev.sns.model.dto.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, User> userRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(User user) {
        String key = getKey(user.getUsername());
        boolean isSet = userRedisTemplate.opsForValue().setIfAbsent(key, user, USER_CACHE_TTL);

        if (isSet) log.info("[Redis] Set User to Redis {} : {}", key, user);
        else log.info("[Redis] Redis already has User {} : {}", key, user);
    }

    public Optional<User> getUser(String username) {
        String key = getKey(username);
        User user = userRedisTemplate.opsForValue().get(key);
        log.info("[Redis] Get data from Redis key = {}, data = {}", key, user);

        return Optional.ofNullable(user);
    }

    private String getKey(String username) {
        return "USER:" + username;
    }

}
