package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ShippingMapper;
import com.neuedu.pojo.Shipping;
import com.neuedu.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        //1：参数校验
        if (shipping==null)
        {
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //2：添加
        shipping.setUserId(userId);
        shippingMapper.insert(shipping);
        //3：返回结果
        Map<String,Integer> map = Maps.newHashMap();
        map.put("shippingId",shipping.getId());
        return ServerResponse.createServerResponseBySuccess(map) ;
    }

    /**
     * 删除地址
     * @param userId
     * @param shippingId
     * @return
     */
    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        //1：参数非空校验
        if (shippingId==null)
        {
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //2：删除
        int result = shippingMapper.deleteByUserIdAndShippingId(userId, shippingId);
        if (result>0)
        {
            return ServerResponse.createServerResponseBySuccess();
        }
        //3：返回结果
        return ServerResponse.createServerResponseByError("删除失败");
    }

    /**
     * 登录状态更新地址
     * @param shipping
     * @return
     */
    @Override
    public ServerResponse update(Shipping shipping) {
        //1：非空判断
        if (shipping == null) {
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //2：更新
        int result = shippingMapper.updateByPrimaryKet(shipping);
        if (result > 0)
        {
            return ServerResponse.createServerResponseBySuccess();
        }
        //返回结果
        return ServerResponse.createServerResponseByError("更新失败");
    }

    /**
     * 查看详细地址
     * @param shippingId
     * @return
     */
    @Override
    public ServerResponse select(Integer shippingId) {
        //1：参数非空校验
        if (shippingId==null)
        {
            return  ServerResponse.createServerResponseByError("参数错误");
        }
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);

        return ServerResponse.createServerResponseBySuccess(shipping);
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectAll();
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }
}
