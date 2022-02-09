package org.sinosoft.webservice.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


public class Base64Util {

    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder encoder = Base64.getEncoder();


    public static String encodeUtil(String str){
        String s = encoder.encodeToString(str.getBytes(StandardCharsets.UTF_8));
        return s;
    }

    public static String decodeUtil(String str) throws UnsupportedEncodingException {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        String name = new String(decoder.decode(bytes), "utf-8");
        return name;
    }
}
