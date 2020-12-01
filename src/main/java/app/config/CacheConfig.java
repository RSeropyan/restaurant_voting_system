package app.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Profile("dev")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("restaurants", "meals");
    }

//    @Bean
//    @Profile("test")
//    public CacheManager cacheManager() {
//        return new NoOpCacheManager();
//    }

}
