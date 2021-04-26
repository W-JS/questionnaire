package com.wjs.questionnaire.util;

import java.util.UUID;

/**
 * 通用唯一标识符
 */
public class UUIDGenerator {

    public static String get16UUID() {
        String randomUUID = UUID.randomUUID().toString().replaceAll("-", "");
        String substring = randomUUID.substring(randomUUID.length()-16, randomUUID.length());
        return substring;
    }

    public static String get32UUID() {
        String randomUUID = UUID.randomUUID().toString().replace("-", "");
        return randomUUID;
    }

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
