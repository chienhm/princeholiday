package com.morntea.web.pricemonitor;

import com.morntea.web.pricemonitor.data.Product;
import com.morntea.web.pricemonitor.data.Price;

public interface ProductFetcher {
    public Product fetchInfo(Product product);
    public Price fetchPrice(Product product);
}
