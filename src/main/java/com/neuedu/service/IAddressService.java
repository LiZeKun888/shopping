package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Shipping;
import org.springframework.stereotype.Service;


public interface IAddressService {

    /**
     * 添加收货地址
     */
    public ServerResponse add(Integer userId, Shipping shipping);
    /**
     * 删除地址
     */
    ServerResponse del(Integer userId,Integer shippingId);
    /**
     * 登录状态更新地址
     */
    ServerResponse update(Shipping shipping);
    /**
     * 查看详细地址
     */
    ServerResponse select(Integer shippingId);
    /**
     * 分页查询
     */
    ServerResponse list(Integer pageNum,Integer pageSize);
}
