//package com.labs.ad_board.service;
//
//public class ReCaptchaAttemptService {
//    private int MAX_ATTEMPT = 4;
//    private LoadingCache<String, Integer> attemptsCache;
//
//    public ReCaptchaAttemptService() {
//        super();
//        attemptsCache = CacheBuilder.newBuilder()
//                .expireAfterWrite(4, TimeUnit.HOURS).build(new CacheLoader<String, Integer>() {
//                    @Override
//                    public Integer load(String key) {
//                        return 0;
//                    }
//                });
//    }
//
//    public void reCaptchaSucceeded(String key) {
//        attemptsCache.invalidate(key);
//    }
//
//    public void reCaptchaFailed(String key) {
//        int attempts = attemptsCache.getUnchecked(key);
//        attempts++;
//        attemptsCache.put(key, attempts);
//    }
//
//    public boolean isBlocked(String key) {
//        return attemptsCache.getUnchecked(key) >= MAX_ATTEMPT;
//    }
//}
