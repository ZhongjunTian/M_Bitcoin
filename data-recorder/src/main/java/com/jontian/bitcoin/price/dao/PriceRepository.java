package com.jontian.bitcoin.price.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * Created by zhongjun on 9/22/16.
 */
@Transactional
public interface PriceRepository extends JpaRepository<Price,Integer> {
}
