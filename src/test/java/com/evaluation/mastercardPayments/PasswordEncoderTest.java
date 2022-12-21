package com.evaluation.mastercardPayments;

import java.nio.charset.Charset;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoded = encoder.encode("admin");
        System.out.println(encoded);

        generateBasicAuthValue();
    }

    private static void generateBasicAuthValue(){
        String auth = "admin" + ":" + "admin";
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        System.out.println(authHeader);
    }
}
