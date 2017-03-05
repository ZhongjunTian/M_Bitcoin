package com.jontian.bitcoin.price.datamining.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zhongjun on 10/16/16.
 */
@Transactional
public interface HistoryPriceRepository extends JpaRepository<HistoryPrice,Integer> {
}
