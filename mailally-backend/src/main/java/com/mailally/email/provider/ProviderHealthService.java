package com.mailally.email.provider;

import com.mailally.email.dto.ProviderHealthDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Background task running provider health checks and caching availability in Redis.
 */
@Service
public class ProviderHealthService {

    private static final Logger log = LoggerFactory.getLogger(ProviderHealthService.class);
    private static final String REDIS_HEALTH_PREFIX = "provider:%s:health";

    private final EmailProviderFactory providerFactory;
    private final StringRedisTemplate redisTemplate;

    public ProviderHealthService(EmailProviderFactory providerFactory,
                                 StringRedisTemplate redisTemplate) {
        this.providerFactory = providerFactory;
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(cron = "0 * * * * ?") // Run health check every minute
    public void monitorProviders() {
        log.info("ProviderHealthService: Running periodic provider health check...");

        Map<String, EmailProvider> providers = providerFactory.getAllProviders();
        for (EmailProvider provider : providers.values()) {
            try {
                long startTime = System.currentTimeMillis();
                ProviderHealthDto health = provider.health();
                long latency = System.currentTimeMillis() - startTime;

                String redisKey = String.format(REDIS_HEALTH_PREFIX, provider.getProviderName().toLowerCase());

                Map<String, String> cacheMap = new HashMap<>();
                cacheMap.put("providerName", provider.getProviderName());
                cacheMap.put("available", String.valueOf(health.isAvailable()));
                cacheMap.put("statusMessage", health.getStatusMessage());
                cacheMap.put("latencyMs", String.valueOf(latency));
                cacheMap.put("quotaPerSec", String.valueOf(provider.quota()));
                cacheMap.put("lastChecked", String.valueOf(System.currentTimeMillis()));

                redisTemplate.opsForHash().putAll(redisKey, cacheMap);

                log.info("ProviderHealthService: Provider [{}] is {}. Latency: {}ms",
                        provider.getProviderName(), health.isAvailable() ? "HEALTHY" : "DEGRADED", latency);
            } catch (Exception e) {
                log.error("ProviderHealthService: Failed to check health of provider [{}]: {}",
                        provider.getProviderName(), e.getMessage());
            }
        }
    }
}
