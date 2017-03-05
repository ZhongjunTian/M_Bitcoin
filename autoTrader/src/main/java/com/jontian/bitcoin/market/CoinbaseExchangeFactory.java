package com.jontian.bitcoin.market;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;


/**
 * Created by irufus on 2/25/15.
 */
public class CoinbaseExchangeFactory {
    String public_key;
    String secret_key;
    String passphrase;
    String url;

    public static CoinbaseExchange createCoinbaseExchange(String api_key, String api_secret, String passphrase, String bUrl) throws NoSuchAlgorithmException {
        Authentication auth = new Authentication(api_key, api_secret, passphrase);
        CoinbaseExchange exchange = new CoinbaseExchange(auth, bUrl);
        return exchange;
    }

    public static CoinbaseExchange createCoinbaseExchange() {
        CoinbaseExchange exchange;
        Properties prop = new Properties();
        InputStream in = CoinbaseExchangeFactory.class.getClassLoader().getResourceAsStream("gdax.properties");
        try {
            prop.load(in);
            System.out.println("Init General Tests | Order Tests");

            exchange = CoinbaseExchangeFactory.createCoinbaseExchange(
                    prop.getProperty("gdax.key"), prop.getProperty("gdax.secret"), prop.getProperty("gdax.passphrase"),
                    prop.getProperty("gdax.api"));
            return exchange;
        } catch(IOException ex){
            ex.printStackTrace();
            throw new IllegalStateException("Unable to read properties file");
        } catch(NoSuchAlgorithmException nex){
            nex.printStackTrace();
            throw new IllegalStateException("Algorithm not supported");
        }
    }
}
