package org.mike.authenticationservice;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PassEncoder {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.printf("password1: %s\n", encoder.encode("password1"));
        System.out.printf("thisissecret: %s\n", encoder.encode("thisissecret"));
    }
}
