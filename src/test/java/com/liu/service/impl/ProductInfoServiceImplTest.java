package com.liu.service.impl;

import com.liu.dataobject.ProductInfo;
import com.liu.dto.CartDTO;
import com.liu.enums.ProductStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceImplTest {

    @Autowired
    private ProductInfoServiceImpl productInfoService;

    private List<CartDTO> cartDTOList = new ArrayList<>();

    public void setCartDTOList(List<CartDTO> cartDTOList) {

        CartDTO cartDTO = new CartDTO("139", 1);
        this.cartDTOList.add(cartDTO);
    }

    @Test
    public void findOne() {
        ProductInfo productInfo = productInfoService.findOne("123456");
        Assert.assertEquals("123456", productInfo.getProductId());
    }

    @Test
    public void findOnAll() {
        List<ProductInfo> productInfoList = productInfoService.findOnAll();
        Assert.assertNotEquals(0, productInfoList.size());
    }

    @Test
    public void findAll() {
        PageRequest request = PageRequest.of(0,2);
        Page<ProductInfo> productInfoPage = productInfoService.findAll(request);
//        System.out.println(productInfoPage.getTotalElements());
        Assert.assertNotEquals(0, productInfoPage.getTotalElements());
    }

    @Test
    public void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("122314");
        productInfo.setProductName("cake");
        productInfo.setProductPrice(new BigDecimal(6.99));
        productInfo.setProductStock(50);
        productInfo.setProductDescription("tasty cake");
        productInfo.setProductIcon("some url");
        productInfo.setProductStatus(ProductStatusEnum.ON.getCode());
        productInfo.setCategoryType(1);

        ProductInfo productInfo1 = productInfoService.save(productInfo);
        Assert.assertNotNull(productInfo1);
    }

    @Test
    public void decreaseStockTest(List<CartDTO> cartDTOList) {

    }
}