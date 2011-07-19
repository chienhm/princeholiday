package com.morntea.web.pricemonitor;

import com.morntea.web.pricemonitor.data.Product;

public class ProductFetcherFactory {
    public static ProductFetcher createPriceFetcher(Product product) {
        if (product.getType().equals("JD_BOOK")) {
            return new JDBookProductFetcher();
        }
        return null;
    }
}