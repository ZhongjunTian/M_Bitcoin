package com.jontian.bitcoin.price.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by zhongjun on 9/14/16.
 */
@Component
public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public static String forceGetResponse(String url, int timeout){
        while(true)
            try {
                String resp = getResponse(url,timeout);
                return resp;
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
    }
    public static String getResponse(String url, int timeout) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
            HttpGet httpget = new HttpGet(url);
            httpget.setConfig(RequestConfig.custom()
                    .setConnectionRequestTimeout(timeout)
                    .setConnectTimeout(timeout)
                    .setSocketTimeout(timeout)
                    .build());
            String responseBody = httpclient.execute(httpget, response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : "";
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }

            });
            return responseBody;
        }
    }
}
