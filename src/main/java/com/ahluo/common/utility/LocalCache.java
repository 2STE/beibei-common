package com.ahluo.common.utility;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * local cache sample
 *
 * @author luochao . 2017-01-09.
 */
public class LocalCache {

//    private static Logger logger = LoggerFactory.getLogger(LocalCache.class);

    private static final Map<String, Object> localCache = new ConcurrentHashMap<>();
    private static final Map<String, Long> localCacheExpiration = new ConcurrentHashMap<>();
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, Executors.defaultThreadFactory());

    //default 60 seconds
    private static final long EXPIRATION = 60 * 1000;

    static {
        //check keys
        executeJob(scheduledExecutorService);
    }

    private static void executeJob(ScheduledExecutorService scheduledExecutorService) {
        try {
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                long currentTime = getCurrentTime();
                localCacheExpiration.forEach((key, value) -> {
                    if ((currentTime - value) > EXPIRATION) {
//                        logger.info("cleaner: k:{},v:{},duration:{}", key, value, (currentTime - value));
                        localCache.remove(key);
                        localCacheExpiration.remove(key);
                    }
                });
            }, EXPIRATION, EXPIRATION, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
//            logger.error("schedule cleaner job was wrong", e);
            if (scheduledExecutorService != null) {
                scheduledExecutorService.shutdownNow();
            }
            try {
                Thread.sleep(EXPIRATION);
            } catch (InterruptedException ie) {
//                logger.error("Failed to sleep", e);
            }
            scheduledExecutorService = Executors.newScheduledThreadPool(1, Executors.defaultThreadFactory());
            executeJob(scheduledExecutorService);
        }
    }


    /**
     * put key value
     */
    public static void put(String key, Object value) {
        if (key == null || value == null) {
            return;
        }

        localCache.put(key, value);
        localCacheExpiration.put(key, getCurrentTime());
    }

    /**
     * get value by key
     *
     * @return value
     */
    public static Object get(String key) {
        if (key == null) {
            return null;
        }

        return localCache.get(key);
    }

    private static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    private LocalCache() {
    }
}