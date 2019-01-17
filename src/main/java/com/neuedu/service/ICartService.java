package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import org.springframework.stereotype.Service;


public interface ICartService {
    /**
     * 购物车中添加商品
     */
    ServerResponse add(Integer userId,Integer productId,Integer count);
    /**
     *购物车列表
      */
    ServerResponse list(Integer userId);
    /**
     * 更新购物车某个商品的数量
     */
     ServerResponse update(Integer userId,Integer productId,Integer count);
    /**
     * 更新购物车某个商品的数量
     */
    ServerResponse delete_product(Integer userId,String productIds);
    /**
     * 选中购物车中某个商品
     */
    ServerResponse select(Integer userId,Integer productId,Integer check);
    /**
     *查询购物车中产品的数量
     */
    ServerResponse get_cart_product_count(Integer userId);
}
