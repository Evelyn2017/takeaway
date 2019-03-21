package com.liu.repository;

import com.liu.dataobject.OrderMain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMainRepository extends JpaRepository<OrderMain, String> {
    Page<OrderMain> findByBuyerOpenId(String buyerOpenId, Pageable pageable);

}
