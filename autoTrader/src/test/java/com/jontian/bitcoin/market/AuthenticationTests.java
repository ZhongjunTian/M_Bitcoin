package com.jontian.bitcoin.market;


import com.jontian.bitcoin.entity.Account;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Created by irufus (sakamura@gmail.com)
 * @Description The primary function of this class is to run through basic tests for the Authentication and CoinbaseExchange classes
 */
public class AuthenticationTests {
    @BeforeClass
    public static void oneTimeSetup(){
        System.out.println("Init General Tests | Authentication Tests");
    }
    @AfterClass
    public static void oneTimeTearDown(){
        System.out.println("Clean up | Authentication Tests");
    }

    @Test
    public void simpleAuthenticationTest(){
        try {
            CoinbaseExchange exchange =  CoinbaseExchangeFactory.createCoinbaseExchange();
            Account[] accounts = exchange.getAccounts();
            assertTrue(accounts.length > 0);
        }
        catch(Exception ex){
            ex.printStackTrace();
            fail();
        }
    }

}
