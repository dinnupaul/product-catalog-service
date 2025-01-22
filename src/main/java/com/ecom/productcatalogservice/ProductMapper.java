package com.ecom.productcatalogservice;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductView toProductView(Product product) {
        ProductView productView = new ProductView();
        productView.setProductId(product.getProductid());
        productView.setProductName(product.getProductname());
        productView.setProductDesc(product.getDescription());
        productView.setPrice(product.getPrice());
        return productView;
    }
}
