//package com.labs.ad_board.service;
//
//public class CaptchaService implements ICaptchaService {
//
//    @Autowired
//    private CaptchaSettings captchaSettings;
//
//    @Autowired
//    private RestOperations restTemplate;
//
//    private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
//
//    @Override
//    public void processResponse(String response) {
//        if(!responseSanityCheck(response)) {
//            throw new InvalidReCaptchaException("Response contains invalid characters");
//        }
//
//        if(reCaptchaAttemptService.isBlocked(getClientIP())) {
//            throw new InvalidReCaptchaException("Client exceeded maximum number of failed attempts");
//        }
//
//        URI verifyUri = URI.create(String.format(
//                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
//                getReCaptchaSecret(), response, getClientIP()));
//
//        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
//
//        if(!googleResponse.isSuccess()) {
//            if(googleResponse.hasClientError()) {
//                reCaptchaAttemptService.reCaptchaFailed(getClientIP());
//            }
//            throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
//                    }
//        reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
//    }
//
//    private boolean responseSanityCheck(String response) {
//        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
//    }
//}
