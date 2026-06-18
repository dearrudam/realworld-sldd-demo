package dev.realworld.users.control;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

interface MessageDigestSupport {

    static boolean equal(String left, String right) {
        return MessageDigest.isEqual(left.getBytes(StandardCharsets.UTF_8), right.getBytes(StandardCharsets.UTF_8));
    }
}
