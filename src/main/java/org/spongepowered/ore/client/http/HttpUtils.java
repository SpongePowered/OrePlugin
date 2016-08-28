package org.spongepowered.ore.client.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Nullable;

/**
 * Helper class for general HTTP handling utilities.
 */
public final class HttpUtils {

    private HttpUtils() {}

    /**
     * Encodes and rebuilds each parameter in the specified URL query string.
     *
     * @param query Query string
     * @return Query string with encoded parameters
     * @throws UnsupportedEncodingException
     */
    public static String encodeQueryStringParameters(@Nullable String query) throws UnsupportedEncodingException {
        if (query == null || query.isEmpty())
            return "";
        query = query.substring(1);
        String[] params = query.split("&");
        StringBuilder newQuery = new StringBuilder("?");
        for (int i = 0; i < params.length; i++) {
            String[] parts = params[i].split("=");
            for (int j = 0; j < parts.length; j++) {
                newQuery.append(URLEncoder.encode(parts[j], "UTF-8"));
                if (j < parts.length - 1)
                    newQuery.append('=');
            }

            if (i < params.length - 1)
                newQuery.append('&');
        }
        return newQuery.toString();
    }

}
