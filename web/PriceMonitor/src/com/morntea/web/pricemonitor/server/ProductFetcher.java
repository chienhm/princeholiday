package com.morntea.web.pricemonitor.server;

public interface ProductFetcher {
    public Product getProduct(String pid);
    public Price getPrice(Product product);
}
