package com.neuedu.portal;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    IUserService userService;
    /***
     * 登录
     * 接口里定义形参的名字和接口文档里定义的名字相同可以省略
     * @RequestParam注解
     */
    @RequestMapping(value = "/login.do")
    public ServerResponse login(HttpSession session, @RequestParam(value = "username")String username, @RequestParam("password")String password)
    {
        ServerResponse serverResponse = userService.login(username, password);
        if (serverResponse.isSucess())
        {
            //登录成功
            UserInfo userInfo =(UserInfo) serverResponse.getData();
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }
        return serverResponse;
    }
    /***
     * 注册
     * 接口里定义形参的名字和接口文档里定义的名字相同可以省略
     * @RequestParam注解
     * springMVC对象绑定 通过userInfo绑定 传的参数名和userInfo一致
     */
    @RequestMapping(value = "/register.do")
    public ServerResponse register(HttpSession session,UserInfo userInfo)
    {
        ServerResponse serverResponse = userService.register(userInfo);

        return serverResponse;
    }
    /***
     * 根据用户名查询密保
     * 接口里定义形参的名字和接口文档里定义的名字相同可以省略
     * @RequestParam注解
     * springMVC对象绑定 通过userInfo绑定 传的参数名和userInfo一致
     */
    @RequestMapping(value = "/forget_get_question.do")
    public ServerResponse forget_get_question(String username)
    {
        ServerResponse serverResponse = userService.forget_get_question(username);

        return serverResponse;
    }
    /**
     * 提交问题答案
     *
     */
    @RequestMapping(value = "/forget_check_question.do")
    public ServerResponse forget_check_question(String username,String question,String answer)
    {
        ServerResponse serverResponse = userService.forget_check_question(username,question,answer);

        return serverResponse;
    }
    /**
     * 忘记密码的重置密码
     *
     */
    @RequestMapping(value = "/forget_reset_question.do")
    public ServerResponse forget_reset_question(String username,String passwordNew,String forgetToken)
    {
        ServerResponse serverResponse = userService.forget_reset_question(username,passwordNew,forgetToken);

        return serverResponse;
    }
    /**
     * 检查用户名或者邮箱是否有效
     *
     */
    @RequestMapping(value = "/check_valid.do")
    public ServerResponse check_valid(String str,String type)
    {
        ServerResponse serverResponse = userService.check_valid(str,type);
        return serverResponse;
    }
    /**
     * 获取登录用户信息
     *
     */
    @RequestMapping(value = "/get_user_info.do")
    public ServerResponse get_user_info(HttpSession session)
    {
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null)
        {
            return ServerResponse.createServerResponseByError("用户未登录");
        }
        userInfo.setPassword("");
        userInfo.setQuestion("");
        userInfo.setAnswer("");
        userInfo.setRole(null);
        return ServerResponse.createServerResponseBySuccess(userInfo);
    }

    /**
     * 登录状态下重置密码
     */
    @RequestMapping(value = "/reset_password.do")
    public ServerResponse reset_password(HttpSession session,String passwordOld,String passwordNew)
    {
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null)
        {
            return ServerResponse.createServerResponseByError("用户未登录");
        }
        return userService.reset_password(userInfo.getUsername(),passwordOld,passwordNew);
    }
    /**
     * 登录状态更新个人信息
     */
    @RequestMapping(value = "/update_information.do")
    public ServerResponse update_information(HttpSession session,UserInfo user)
    {
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null)
        {
            return ServerResponse.createServerResponseByError("用户未登录");
        }
        user.setId(userInfo.getId());
        ServerResponse serverResponse =  userService.update_information(user);
        if (serverResponse.isSucess())
        {
            //更新session中用户的信息
            UserInfo userInfo1 = userService.findUserInfoByUserid(userInfo.getId());
            session.setAttribute(Const.CURRENTUSER,userInfo1);
        }
        return serverResponse;
    }
    /**
     * 获取登录用户详细信息
     *
     */
    @RequestMapping(value = "/get_inforamtion.do")
    public ServerResponse get_inforamtion(HttpSession session)
    {
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if (userInfo==null)
        {
            return ServerResponse.createServerResponseByError("用户未登录");
        }
        userInfo.setPassword("");
        return ServerResponse.createServerResponseBySuccess(null,userInfo);
    }
    /**
     * 退出登录
     *
     */
    @RequestMapping(value = "/logout.do")
    public ServerResponse logout(HttpSession session)
    {
       session.removeAttribute(Const.CURRENTUSER);
        return ServerResponse.createServerResponseBySuccess();
    }

}
