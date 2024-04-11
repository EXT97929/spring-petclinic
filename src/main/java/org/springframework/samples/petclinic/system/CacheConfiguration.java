package org.springframework.samples.petclinic.system;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@Lazy(false)
@EnableCaching
@ConfigurationProperties(prefix = "caching")
public class CacheConfiguration {

  @Setter private Map<String, CacheSpec> specs;

  @Bean
  @Primary
  public CacheManager cacheManager() {
    CustomCaffeineCacheManager caffeineCacheManager = new CustomCaffeineCacheManager();
    if (specs != null) {
      specs.entrySet().stream()
          .map(
              entry ->
                  buildCache(
                      entry.getKey(), CaffeineSpec.parse(entry.getValue().getSpecification())))
          .forEach(caffeineCacheManager::addCache);
    }
    return caffeineCacheManager;
  }

  private CaffeineCache buildCache(String name, CaffeineSpec cacheSpec) {
    log.info("Cache {} spec {}", name, cacheSpec);
    return new CaffeineCache(name, Caffeine.from(cacheSpec).recordStats().build());
  }

  public static class CustomCaffeineCacheManager extends CaffeineCacheManager {

    private final Map<String, Cache<Object, Object>> preDefinedCaches = new ConcurrentHashMap<>();

    public void addCache(CaffeineCache c) {
      preDefinedCaches.put(c.getName(), c.getNativeCache());
    }

    public void addCache(String name, Cache<Object, Object> cache) {
      preDefinedCaches.put(name, cache);
    }

    @Override
    protected Cache<Object, Object> createNativeCaffeineCache(String name) {
      return preDefinedCaches.getOrDefault(name, super.createNativeCaffeineCache(name));
    }
  }

  @Getter
  @Setter
  public static class CacheSpec {
    private String specification;
  }
}
