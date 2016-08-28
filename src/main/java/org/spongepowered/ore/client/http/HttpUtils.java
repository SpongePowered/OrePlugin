package org.spongepowered.ore.client.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

public final class HttpUtils {

    private HttpUtils() {}

    public static String encodeQueryStringParameters(String query) throws UnsupportedEncodingException {
        query = query.substring(1);
        String[] params = query.split("&");
        StringBuilder newQuery = new StringBuilder("?");
        for (int i = 0; i < params.length; i++) {
            System.out.println("encoding param " + params[i]);
            String[] parts = params[i].split("=");
            System.out.println("parts = " + Arrays.toString(parts));
            for (int j = 0; j < parts.length; j++) {
                System.out.println("encoding part " + parts[j]);
                newQuery.append(URLEncoder.encode(parts[j], "UTF-8"));
                if (j < parts.length - 1)
                    newQuery.append('=');
            }

            if (i < params.length - 1)
                newQuery.append('&');
        }
        System.out.println("result = " + newQuery.toString());
        return newQuery.toString();
    }

}
