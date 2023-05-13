package com.crazymakercircle.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crazymakercircle.Balance.ImLoadBalance;
import com.crazymakercircle.controller.utility.BaseController;
import com.crazymakercircle.entity.ImNode;
import com.crazymakercircle.entity.LoginBack;
import com.crazymakercircle.im.common.bean.UserDTO;
import com.crazymakercircle.mybatis.entity.UserPO;
import com.crazymakercircle.service.UserService;
import com.crazymakercircle.util.JsonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * WEB GATE
 * Created by 尼恩 at 疯狂创客圈
 */

//@EnableAutoConfiguration
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api("User 相关的api")
public class UserAction extends BaseController
{
    @Resource
    private UserService userService;
    @Resource
    private ImLoadBalance imLoadBalance;

    /**
     * Web短连接登录
     *
     * @param username 用户名
     * @param password 命名
     * @return 登录结果
     */
    @ApiOperation(value = "登录", notes = "根据用户信息登录")
    @RequestMapping(value = "/login/{username}/{password}", method = RequestMethod.GET)
    public String loginAction(
            @PathVariable("username") String username,
            @PathVariable("password") String password)
    {
        UserPO user = new UserPO();
        user.setUserName(username);
        user.setPassWord(password);
        user.setUserId(user.getUserName());

//        User loginUser = userService.login(user);

        LoginBack back = new LoginBack();
        /**
         * 取得最佳的Netty服务器-->取得所有的节点
         */
        //ImNode bestWorker = imLoadBalance.getBestWorker();
        //back.setImNode(bestWorker);
        List<ImNode> allWorker = imLoadBalance.getWorkers();
        back.setImNodeList(allWorker);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        back.setUserDTO(userDTO);
        back.setToken(user.getUserId().toString());
        String r = JsonUtil.pojoToJson(back);
        return r;
    }


    /**
     * 从zookeeper中删除所有IM节点
     *
     * @return 删除结果
     */
    @ApiOperation(value = "删除节点", notes = "从zookeeper中删除所有IM节点")
    @RequestMapping(value = "/removeWorkers", method = RequestMethod.GET)
    public String removeWorkers()
    {
        imLoadBalance.removeWorkers();
        return "已经删除";
    }

}