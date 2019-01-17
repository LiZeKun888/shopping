package com.neuedu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtils;
import com.neuedu.utils.FTPUtil;
import com.neuedu.utils.PropertiesUtils;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ICategoryService categoryService;

    @Override
    public ServerResponse saveORUpdate(Product product) {
        //1:参数的非空校验
        if (product==null)
        {
            return ServerResponse.createServerResponseByError("参数为空");
        }
        //2：设计商品组图
        String subImages = product.getSubImages();
        if (subImages!=null&&!subImages.equals(""))
        {
            String[] subImagesArr = subImages.split(",");
            if (subImagesArr.length>0)
            {
                //设置商品的主图
                product.setMainImage(subImagesArr[0]);
            }
        }
        //3：添加商品 save or update
        if (product.getId()==null)
        {
            //添加
            int result = productMapper.insert(product);
            if (result>0)
            {
                return ServerResponse.createServerResponseBySuccess("添加成功");
            }else
            {
                return ServerResponse.createServerResponseByError("添加失败");
            }
        }else
        {
            //更新
            int result = productMapper.updateByPrimaryKey(product);
            if (result>0)
            {
                return ServerResponse.createServerResponseBySuccess("更新成功");
            }else
            {
                return ServerResponse.createServerResponseByError("更新失败");
            }
        }
    }

    /**
     * 产品的上下架
     * @param  productId 商品的ID
     * @param  status 商品的状态
     */
    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {
        //1:参数非空校验
        if (productId==null)
        {
            return ServerResponse.createServerResponseByError("商品ID不能为空");
        }
        if (status==null)
        {
            return ServerResponse.createServerResponseByError("商品状态参数不能为空");
        }
        //2：更新商品的状态
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int result = productMapper.updateProdictKeySelective(product);

        //3.返回结果
        if (result>0)
        {
            return ServerResponse.createServerResponseBySuccess();
        }else
        {
            return ServerResponse.createServerResponseByError("更新失败");
        }
    }

    @Override
    public ServerResponse detail(Integer productId) {
        //1:参数的非空校验
        if (productId==null)
        {
            return ServerResponse.createServerResponseByError("商品ID参数不能为空");
        }

        //2：根据商品ID查询商品
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null)
        {
            return ServerResponse.createServerResponseByError("商品不存在");
        }
        //3：商品转成product——》productDeailVO
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        //4：返回结果
        return ServerResponse.createServerResponseBySuccess(productDetailVO);
    }

    private ProductDetailVO assembleProductDetailVO(Product product)
    {

        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category!=null)
        {
            productDetailVO.setParentCategoryId(category.getParentId());
        }else
        {
            //默认根节点
            productDetailVO.setParentCategoryId(0);

        }

        return productDetailVO;
    }
/*
*
* 查询商品列表，分页
* */
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        //1:查询商品数据  select * from product limit(pageNum -1)*pageSize,page
        List<Product> productList = productMapper.selectAll();
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList!=null&&productList.size()>0)
        {
            for (Product product:productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);

        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

    private ProductListVO assembleProductListVO(Product product)
    {
        ProductListVO productListVO = new ProductListVO();
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setId(product.getId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());

        return productListVO;
    }
    @Override
    public ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if (productName!=null&&!productName.equals(""))
        {
            productName="%"+productName+"%";
        }
        List<Product> productList = productMapper.findProductByProductIdAndProductName(productId, productName);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList!=null&&productList.size()>0)
        {
            for (Product product:productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse upload(MultipartFile file, String path) {
       if (file==null)
       {
           return ServerResponse.createServerResponseByError();
       }
       //1:获取图片名字
        String originalFilename = file.getOriginalFilename();
       //2:获取图片扩展名
        String exName = originalFilename.substring(originalFilename.lastIndexOf("."));//.jsg
        //3:为图片生成新的唯一的名字
        String newFileName = UUID.randomUUID().toString()+exName;
        File pathFile = new File(path);
        if (!pathFile.exists())
        {
            pathFile.setWritable(true);
            pathFile.mkdirs();
        }
        File file1 = new File(path, newFileName);
        try {
            file.transferTo(file1);
            //将图片上传到图片服务器
            FTPUtil.uploadFile(Lists.newArrayList(file1));
            Map<String,String> map = Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readByKey("imageHost")+"/"+newFileName);
            //删除应用服务器上的图片
            file1.delete();
            return ServerResponse.createServerResponseBySuccess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 前台商品查询
     * @param productId
     * @return
     */
    @Override
    public ServerResponse detail_portal(Integer productId) {
        //1：非空校验
        if (productId==null)
        {
            return ServerResponse.createServerResponseByError("商品id不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        //2：查询商品product
        if (product==null)
        {
            return ServerResponse.createServerResponseByError("商品不存在");
        }
        //3：校验商品的状态
        if (product.getStatus()!= Const.ProductStatusEnum.PRODUCT_ONLINE.getCode())
        {
            return ServerResponse.createServerResponseByError("商品下架或删除");
        }
        //4：获取porductDetailVO
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);

        //5：返回结果
        return ServerResponse.createServerResponseBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse list_portal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //1：参数校验categoryId和keyword不能同时为空
        if (categoryId == null && (keyword == null || keyword.equals(""))) {
            return ServerResponse.createServerResponseByError("参数错误");
        }

        Set<Integer> integerSet = Sets.newHashSet();
        //2:categoryId
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && (keyword == null || keyword.equals(""))) {
                //说明没有商品数据
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOList);
                return ServerResponse.createServerResponseBySuccess(pageInfo);
            }
            ServerResponse serverResponse = categoryService.get_deep_category(categoryId);

            if (serverResponse.isSucess()) {
                integerSet = (Set<Integer>)serverResponse.getData();
            }
        }
        //3:keyword
        if (keyword != null && !keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }else
        {
            keyword=null;
        }
        if (orderBy.equals("")) {
            PageHelper.startPage(pageNum, pageSize);
        } else {
            String[] orderBYArr = orderBy.split("_");
            if (orderBYArr.length > 1) {
                PageHelper.startPage(pageNum, pageSize, orderBYArr[0] + " " + orderBYArr[1]);
            } else {
                PageHelper.startPage(pageNum, pageSize);
            }
        }

        List<Product> productList = productMapper.searchProduct(integerSet,keyword);

        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList!=null&&productList.size()>0)
        {
            for (Product product:productList) {
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }


        //4:LIst<Product>
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(productListVOList);
        return ServerResponse.createServerResponseBySuccess(pageInfo);
    }

}
