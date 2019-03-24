package com.liu.service.impl;

import com.liu.dataobject.ProductInfo;
import com.liu.dto.CartDTO;
import com.liu.enums.ProductStatusEnum;
import com.liu.enums.ResultEnum;
import com.liu.exception.SellException;
import com.liu.repository.ProductInfoRepository;
import com.liu.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProductInfoServiceImpl implements ProductInfoService {
    private ProductInfoRepository repository;

    @Autowired
    public void setRepository(ProductInfoRepository repository) {
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

    @Override
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDto : cartDTOList) {
            ProductInfo info = repository.findById(cartDto.getProductId()).orElse(null);
            //商品为空抛出异常
            if(info == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            int result = info.getProductStock() + cartDto.getProductQuantity();
            info.setProductStock(result);
            save(info);
        }

    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {

        for (CartDTO cartDTO : cartDTOList) {
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).orElse(null);
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if (result < 0) {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            log.info("---------result: {}----------",result);
            productInfo.setProductStock(result);

            repository.save(productInfo);
        }

    }
}
