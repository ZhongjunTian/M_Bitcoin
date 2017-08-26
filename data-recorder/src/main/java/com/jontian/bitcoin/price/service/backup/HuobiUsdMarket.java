package com.jontian.bitcoin.price.service.backup;

import com.jontian.bitcoin.price.dao.Price;
import com.jontian.bitcoin.price.util.PriceParseUtil;

/**
 * Created by zhongjun on 9/14/16.
 */
public class HuobiUsdMarket extends Market {
    @Override
    public String getUrl() {
        return "http://api.huobi.com/usdmarket/detail_btc_json.js";
    }

    @Override
    public Price parsePrice(String json) {
//        Price price = PriceParseUtil.getDouble(json,"buys.0.price","sells.0.price");
//        return null;

        return null;
    }
}
