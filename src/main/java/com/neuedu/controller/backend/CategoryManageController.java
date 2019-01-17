package com.neuedu.controller.backend;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/***
 *
 */
@RestController
@RequestMapping(value = "/manage/category")
public class CategoryManageController {
    @Autowired
    ICategoryService CategoryService;


    /***
     * 获取一个子节点
     */
    @RequestMapping(value = "/get_category.do")
    public ServerResponse get_category(HttpSession session,Integer categoryId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null)
        {
            return ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.NEED_LOGIN.getCode(),Const.ReponseCodeEnum.NEED_LOGIN.getDesc());
        }
            //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode())
        {
            return ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return CategoryService.get_category(categoryId);
    }

    /***
     * 增加节点
     */
    @RequestMapping(value = "/add_category.do")
    public ServerResponse add_category(HttpSession session,@RequestParam(required = false,defaultValue = "0") Integer parentId,String categoryName){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null)
        {
            return ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.NEED_LOGIN.getCode(),Const.ReponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode())
        {
            return ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return CategoryService.add_category(parentId,categoryName);
    }

    /**
     * 修改节点
     */
    @RequestMapping(value = "/set_categor_name.do")
    public ServerResponse set_categor_name(HttpSession session,Integer categoryId,String categoryName){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null)
        {
            return ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.NEED_LOGIN.getCode(),Const.ReponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode())
        {
            return ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return CategoryService.set_categor_name(categoryId,categoryName);
    }
    /**
     * 获取当前id及递归的子节点
     */
    @RequestMapping(value = "/get_deep_category.do")
    public ServerResponse get_deep_category(HttpSession session,Integer categoryId){
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null)
        {
            return ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.NEED_LOGIN.getCode(),Const.ReponseCodeEnum.NEED_LOGIN.getDesc());
        }
        //判断用户权限
        if (userInfo.getRole()!=Const.RoleEnum.ROLE_ADMIN.getCode())
        {
            return ServerResponse.createServerResponseByError(Const.ReponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ReponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return CategoryService.get_deep_category(categoryId);
    }
}
