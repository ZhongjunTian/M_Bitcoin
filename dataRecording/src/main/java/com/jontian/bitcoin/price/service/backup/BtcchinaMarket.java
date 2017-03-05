package com.jontian.bitcoin.price.service.backup;

import com.jontian.bitcoin.price.dao.Price;
import com.jontian.bitcoin.price.util.PriceParseUtil;

/**
 * Created by zhongjun on 9/14/16.
 */
public class BtcchinaMarket extends Market {
    @Override
    public String getUrl() {
        return "https://data.btcchina.com/data/ticker?service=btccny";
    }

    @Override
    public Price parsePrice(String json) {

//        return PriceParseUtil.getDouble(json,"ticker.buy","ticker.sell")
//                .setMarket("BTCC");
        return null;
    }
}
