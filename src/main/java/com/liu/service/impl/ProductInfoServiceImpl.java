package com.liu.service.impl;

import com.liu.dataobject.ProductCategory;
import com.liu.dataobject.ProductInfo;
import com.liu.enums.ProductStatusEnum;
import com.liu.repository.ProductCategoryRepository;
import com.liu.repository.ProductInfoRepository;
import com.liu.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    private ProductInfoRepository repository;
    @Autowired
    public void setRepository(ProductInfoRepository repository){
        this.repository = repository;
    }


    @Override
    public ProductInfo findOne(String productId) {
        return repository.findById(productId).orElse(null);
    }

    @Override
    public List<ProductInfo> findOnAll() {
        return repository.findByProductStatus(ProductStatusEnum.ON.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }
}
