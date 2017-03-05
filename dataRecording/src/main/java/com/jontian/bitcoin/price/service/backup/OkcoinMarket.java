package com.jontian.bitcoin.price.service.backup;

import com.jontian.bitcoin.price.dao.Price;
import com.jontian.bitcoin.price.util.HttpUtil;
import com.jontian.bitcoin.price.util.PriceParseUtil;

/**
 * Created by zhongjun on 9/14/16.
 */
public class OkcoinMarket {
    //https://www.okcoin.com/about/rest_api.do
    private final static String OK_COIN = "https://www.okcoin.cn/api/v1/ticker.do?symbol=btc_cny";


    public Price getCurrentPrice() {
        String response = HttpUtil.forceGetResponse(OK_COIN, 1500);
        Price price = new Price();
        double buy = PriceParseUtil.getDouble(response, "ticker.buy");
        double sell = PriceParseUtil.getDouble(response, "ticker.sell");
        long date = PriceParseUtil.getLong(response, "date");
        price.setBuy(buy);
        price.setSell(sell);
        price.setTime(date);
        return price;
    }
}
