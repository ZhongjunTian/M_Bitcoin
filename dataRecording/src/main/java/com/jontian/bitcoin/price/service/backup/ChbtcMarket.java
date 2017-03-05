package com.jontian.bitcoin.price.service.backup;

import com.jontian.bitcoin.price.dao.Price;
import com.jontian.bitcoin.price.util.PriceParseUtil;

/**
 * Created by zhongjun on 9/14/16.
 */
public class ChbtcMarket extends Market {
    @Override
    public String getUrl() {
        return "http://api.chbtc.com/data/ticker";
    }

    @Override
    public Price parsePrice(String json) {
//        return PriceParseUtil.getDouble(json,"ticker.buy","ticker.sell")
//                .setMarket("ChBTC");

        return null;
    }
}
