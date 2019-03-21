package com.liu.service;

import com.liu.dataobject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductInfoService {
    ProductInfo findOne(String productId);

    List<ProductInfo> findOnAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //加减库存
}
