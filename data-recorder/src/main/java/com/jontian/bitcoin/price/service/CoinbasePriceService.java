package com.jontian.bitcoin.price.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jontian.bitcoin.price.dao.Price;
import com.jontian.bitcoin.price.dao.PriceBuilder;
import com.jontian.bitcoin.price.util.HttpUtil;
import com.jontian.bitcoin.price.util.PriceParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by zhongjun on 9/14/16.
 */
public class CoinbasePriceService {
    public static final String BOOK_LIST_URL = "https://api.gdax.com/products/BTC-USD/book?level=2";
    public static final String SINGLE_BOOK_URL = "https://api.gdax.com/products/BTC-USD/book";
    //https://docs.gdax.com/#get-products
    private static Logger logger = LoggerFactory.getLogger(CoinbasePriceService.class);

    /*
    Example: current market orders
        $1.11
        $1.10
        $1.09
        -------
        $1.00

        My max sell price has to > 1.00
        So I sell at 1.09
     */
    public BigDecimal getSafeSellPrice() throws IOException {
        String json = HttpUtil.getResponse(SINGLE_BOOK_URL,1000);
        double minSellerPrice = getMinSellerPrice(json);
        logger.trace(json);
        return roundPrice(minSellerPrice);
    }
    /*
        Example: current market orders
            $1.11
            $1.10
            $1.09
            -------
            $1.00

            My max buy price has to > 1.
            So I sell at 1.09
         */
    public BigDecimal getSafeBuyPrice() throws IOException {
        String json = HttpUtil.getResponse(SINGLE_BOOK_URL,1000);
        double maxBuyerPrice = getMaxBuyerPrice(json);
        logger.trace(json);
        return roundPrice(maxBuyerPrice);
    }

    private double getMaxBuyerPrice(String json) {
        JsonNode buyerAsks = PriceParseUtil.getJsonNode(json,"bids");//买家order 最大值
        JsonNode maxBuyerPriceNode = buyerAsks.get(0);
        return maxBuyerPriceNode.get(0).asDouble();
    }

    private double getMinSellerPrice(String json) {
        JsonNode sellerBids = PriceParseUtil.getJsonNode(json,"asks");//卖家order 最小值
        JsonNode minSellerPriceNode = sellerBids.get(0);
        return minSellerPriceNode.get(0).asDouble();
    }

    private BigDecimal roundPrice(double price){
        BigDecimal bPrice = BigDecimal.valueOf(price);
        bPrice.setScale(2,BigDecimal.ROUND_HALF_UP);
        return bPrice;
    }

    protected Price getCurrentPrice() throws IOException {
        long t0 = System.currentTimeMillis();
        String json = HttpUtil.getResponse(BOOK_LIST_URL,1000);
        Price price = parsePrice(json);
        price.setTime(t0);
        return price;
    }

    private Price parsePrice(String json) {
        PriceBuilder priceList = new PriceBuilder();

        JsonNode sells = PriceParseUtil.getJsonNode(json,"asks");//卖家order 从小到大
        for(JsonNode sell:sells){
            double price = sell.get(0).asDouble();
            double amount = sell.get(1).asDouble();
            priceList.addBuy(price,amount);
        }

        JsonNode bids = PriceParseUtil.getJsonNode(json,"bids");//买家order 从大到小
        for(JsonNode bid:bids){
            double price = bid.get(0).asDouble();
            double amount = bid.get(1).asDouble();
            priceList.addSell(price,amount);
        }
        return priceList.build();
    }

    public static void main(String args[]) throws IOException {
        System.out.println(
                new CoinbasePriceService().parsePrice("{\"sequence\":1542277551,\"bids\":[[\"595.3\",\"1\",1],[\"595.18\",\"4.99559\",1],[\"595.16\",\"3.49\",2],[\"595.15\",\"1.5\",1]," +
                        "[\"595.14\",\"0.01\",1],[\"595.13\",\"0.05615824\",2],[\"595.04\",\"1.27\",1],[\"595.02\",\"0.01\",1],[\"594.88\",\"1.52\",1],[\"594.87\",\"1.52\",1]," +
                        "[\"594.86\",\"1.59\",1],[\"594.85\",\"1.62\",1],[\"594.84\",\"1.48\",1],[\"594.83\",\"1.51\",1],[\"594.82\",\"1.38\",1],[\"594.81\",\"1.51\",1]," +
                        "[\"594.8\",\"1.56\",1],[\"594.79\",\"1.67\",1],[\"594.78\",\"1.31\",1],[\"594.76\",\"1.67\",2],[\"594.75\",\"1.63914008\",3],[\"594.74\",\"1.45\",1]," +
                        "[\"594.73\",\"1.5\",1],[\"594.72\",\"1.5\",1],[\"594.71\",\"1.8\",1],[\"594.66\",\"0.03541738\",1],[\"594.63\",\"0.0692\",1],[\"594.59\",\"1.828\",2]," +
                        "[\"594.56\",\"0.01\",1],[\"594.55\",\"0.12073922\",2],[\"594.54\",\"1.57\",1],[\"594.53\",\"3.01\",2],[\"594.52\",\"3.01\",2],[\"594.51\",\"0.1\",1]," +
                        "[\"594.49\",\"3.02\",2],[\"594.48\",\"3.07\",2],[\"594.47\",\"2.92\",2],[\"594.45\",\"6.94\",1],[\"594.44\",\"2.04198\",1],[\"594.41\",\"10.8\",1]," +
                        "[\"594.38\",\"0.01\",1],[\"594.36\",\"0.021216\",1],[\"594.35\",\"0.02214\",1],[\"594.29\",\"2.26972\",1],[\"594.26\",\"8.44979426\",2]," +
                        "[\"594.2\",\"0.012\",1],[\"594.19\",\"0.01\",1],[\"594.17\",\"0.021256\",1],[\"594.16\",\"1.87756\",1],[\"594.07\",\"0.35233468\",3]]," +
                        "\"asks\":[[\"595.31\",\"32.65506975\",2],[\"595.41\",\"0.25\",1],[\"595.43\",\"0.7\",4],[\"595.47\",\"0.31\",2],[\"595.48\",\"0.06\",1]," +
                        "[\"595.49\",\"2.73624\",2],[\"595.5\",\"2.45\",2],[\"595.53\",\"1.7\",1],[\"595.63\",\"1.77\",1],[\"595.66\",\"2.2\",1],[\"595.68\",\"1.906\",1]," +
                        "[\"595.73\",\"0.01\",1],[\"595.8\",\"0.25\",1],[\"595.81\",\"1.85033\",1],[\"595.85\",\"0.01\",1],[\"595.9\",\"0.05354011\",1],[\"595.94\",\"1.788\",1]," +
                        "[\"596\",\"7.04\",1],[\"596.04\",\"1.10011\",1],[\"596.05\",\"0.01\",1],[\"596.09\",\"2.196\",1],[\"596.15\",\"0.5\",1],[\"596.21\",\"0.01002525\",1]," +
                        "[\"596.22\",\"2\",1],[\"596.23\",\"2.2\",1],[\"596.24\",\"40\",1],[\"596.25\",\"28.28801508\",1],[\"596.26\",\"0.01\",1],[\"596.31\",\"0.01\",1]," +
                        "[\"596.34\",\"0.02\",2],[\"596.37\",\"0.01\",1],[\"596.38\",\"1.766\",1],[\"596.44\",\"0.08051211\",2],[\"596.47\",\"0.09035211\",3]," +
                        "[\"596.5\",\"0.16069637\",2],[\"596.51\",\"1.93436\",1],[\"596.53\",\"0.09543211\",3],[\"596.56\",\"0.08543211\",1],[\"596.59\",\"0.08543211\",4]," +
                        "[\"596.62\",\"0.08543211\",2],[\"596.64\",\"1.7912\",1],[\"596.65\",\"0.08543211\",4],[\"596.67\",\"0.0224\",1],[\"596.68\",\"0.08364\",2]," +
                        "[\"596.69\",\"0.08749\",2],[\"596.7\",\"0.08\",1],[\"596.71\",\"0.09543211\",4],[\"596.74\",\"0.08543211\",4],[\"596.77\",\"0.36737055\",7]," +
                        "[\"596.78\",\"0.01\",1]]}")
        );
    }
}
