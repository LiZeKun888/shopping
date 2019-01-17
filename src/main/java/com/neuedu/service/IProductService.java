package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


public interface IProductService {

    /**
     * 新增or更新产品
     */
    ServerResponse saveORUpdate(Product product);

    /**
     * 产品的上下架
     *
     * @param productId 商品的ID
     * @param status    商品的状态
     */
    ServerResponse set_sale_status(Integer productId, Integer status);

    /**
     * 后台查看商品的详情
     */
    ServerResponse detail(Integer productId);

    /**
     * 查看商品列表
     */
    ServerResponse list(Integer pageNum, Integer pageSize);

    /**
     * 搜索商品
     */
    ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize);

    /**
     * 图片上传
     */
    ServerResponse upload(MultipartFile file, String path);

    /***
     * 前台商品查询
     */
    ServerResponse detail_portal(Integer productId);

    /***
     *前台商品搜索
     */
    ServerResponse list_portal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);


}