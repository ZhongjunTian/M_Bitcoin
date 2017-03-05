package com.jontian.bitcoin.price.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jontian.bitcoin.price.dao.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by zhongjun on 9/14/16.
 */
public class PriceParseUtil {
    private static Logger logger = LoggerFactory.getLogger(PriceParseUtil.class);
    public static void main(String args[]) throws IOException {
        double price = getDouble(
                "{\"ticker\":{\"high\":\"25.94\",\"low\":\"25.39\",\"buy\":\"25.49\",\"sell\":\"25.59\",\"last\":\"25.49\",\"vol\":\"3407.67300000\",\"date\":1473903093,\"vwap\":\"25.49\",\"prev_close\":\"25.59\",\"open\":\"25.5\"}}"
                ,"ticker.buy");
        System.out.println(price);
    }

    public static double getDouble(String json, String path){
        JsonNode node = getJsonNode(json,path);
        return node == null ? 0:node.asDouble();
    }

    public static long getLong(String json, String path){
        JsonNode node = getJsonNode(json,path);
        return node == null ? 0:node.asLong();
    }

    public static JsonNode getJsonNode(String json, String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readValue(json, JsonNode.class);
            return node = getJsonNode(node, path);
        } catch (IOException e) {
            logger.error(e.toString()+" | "+e.getMessage());
        } catch (Exception e){
            logger.error(e.toString()+" | "+e.getMessage());
        }
        return null;
    }

    public static JsonNode getJsonNode(JsonNode node, String path) {
        while(!path.isEmpty()){
            if(path.contains(".")) {
                int firstDot = path.indexOf(".");
                String end = path.substring(firstDot + 1);
                String front = path.substring(0, firstDot);
                if(front.contentEquals("0")){
                    node = node.get(0);
                }else{
                    node = node.get(front);
                }
                path = end;
            }else{
                if(path.contentEquals("0")){
                    node = node.get(0);
                }else{
                    node = node.get(path);
                }
                break;
            }
        }
        return node;
    }
}
