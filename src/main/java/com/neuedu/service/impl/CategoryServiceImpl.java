package com.neuedu.service.impl;

import com.google.common.collect.Sets;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    /***
     * 获取品类的子节点（平级）
     */
    @Override
    public ServerResponse get_category(Integer categoryId) {
        //1:非空校验
        if (categoryId==null)
        {
            return ServerResponse.createServerResponseByError("参数不能为空");
        }
        //2：根据categoryid查询类别
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null)
        {
            return ServerResponse.createServerResponseByError("查询的类别不存在");
        }
        //3：查询子类别
        List<Category> categorieList = categoryMapper.findChildCategory(categoryId);

        //4：返回结果
        return ServerResponse.createServerResponseBySuccess(categorieList);
    }

    /**
     *添加节点
     *
     */
    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {
        //1:非空校验
        if(categoryName==null||categoryName.equals(""))
        {
            return ServerResponse.createServerResponseByError("类别名称不能为空");
        }

        //2：添加节点
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(1);
        int result = categoryMapper.insert(category);
        if (result>0)
        {
            return ServerResponse.createServerResponseBySuccess();
        }
        //3：返回结果

        return ServerResponse.createServerResponseByError("添加失败");
    }

    /**
     * 修改节点
     */
    @Override
    public ServerResponse set_categor_name(Integer categoryId, String categoryName) {
        //1：参数非空校验
        if(categoryId==null||categoryId.equals(""))
        {
            return ServerResponse.createServerResponseByError("类别ID不能为空");
        }
        if(categoryName==null||categoryName.equals(""))
        {
            return ServerResponse.createServerResponseByError("类别名称不能为空");
        }
        //2;根据categoryId查询
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category==null)
        {
            return ServerResponse.createServerResponseByError("修改的类别不存在");
        }
        //3：修改
        category.setName(categoryName);
        int result = categoryMapper.updateByPrimaryKey(category);
        if (result>0)
        {
            //修改成功
            return ServerResponse.createServerResponseBySuccess();
        }
        //4：返回结果

        return ServerResponse.createServerResponseByError("修改失败");
    }

    @Override
    public ServerResponse<Set<Integer>> get_deep_category(Integer categoryId) {
        //1:参数的非空校验
        if (categoryId==null)
        {
            return ServerResponse.createServerResponseByError("类别id不能为空");
        }
        //2：查询
        Set<Category> categorySet = Sets.newHashSet();
        categorySet = findAllChildCategory(categorySet, categoryId);
        Set<Integer> integerSet = Sets.newHashSet();
        Iterator<Category> categoryIterator = categorySet.iterator();
        while (categoryIterator.hasNext())
        {
            Category category= categoryIterator.next();
            integerSet.add(category.getId());
        }
        return ServerResponse.createServerResponseBySuccess(integerSet);
    }
    private Set<Category> findAllChildCategory(Set<Category> categorySet, Integer categoryId){//
    //查找等节点
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category!=null)
        {
            categorySet.add(category);
        }
        //查找categoryId下的子节点（平级）
        List<Category> categoryList = categoryMapper.findChildCategory(categoryId);
        if (categoryList!=null&&categoryList.size()>0)
        {
            for (Category category1:categoryList) {
                findAllChildCategory(categorySet,category1.getId());
            }
        }
        return categorySet;
    }
}
