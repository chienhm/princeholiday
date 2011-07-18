package com.morntea.web.pricemonitor;

public interface ProductFetcher {
    public Product getProduct(String pid);
    public Price getPrice(Product product);
}
