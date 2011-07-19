package com.morntea.web.pricemonitor;

import com.morntea.web.pricemonitor.data.Product;
import com.morntea.web.pricemonitor.data.Price;

public interface ProductFetcher {
    public Product getProduct(Product product);
    public Price getPrice(Product product);
}
