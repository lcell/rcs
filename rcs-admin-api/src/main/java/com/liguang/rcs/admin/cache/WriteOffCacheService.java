package com.liguang.rcs.admin.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 核销的缓存服务
 */
@Service
public class WriteOffCacheService {
    private static final int MAX_CACHE_LEN = 1000;
    private final Cache<Long, List<? extends Object>> settlementCache = CacheBuilder.newBuilder().maximumSize(MAX_CACHE_LEN).build();

    public <T> void addToCache(Long contractId, List<T> serviceSettlementList) {
        settlementCache.put(contractId, Lists.newArrayList(serviceSettlementList));
    }

    public <T> List<T> getFromCatch(Long contractId) {
        return Lists.newArrayList((List<T>)settlementCache.getIfPresent(contractId));
    }

    public void removeCache(Long contractId) {
        settlementCache.asMap().remove(contractId);
    }
}
