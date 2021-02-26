package app.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    private final Logger logger = LoggerFactory.getLogger(app.config.CacheConfig.class);

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("restaurantsCache");
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .initialCapacity(10)
                        .maximumSize(100)
                        .removalListener((Object key, Object graph, RemovalCause cause) ->
                                logger.info(String.format("Key %s was removed (%s)%n", key, cause)))
        );
        return cacheManager;
    }

}
