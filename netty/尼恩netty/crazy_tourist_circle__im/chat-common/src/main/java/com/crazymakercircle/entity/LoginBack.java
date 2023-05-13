package com.crazymakercircle.entity;

import java.util.List;
import com.crazymakercircle.im.common.bean.UserDTO;
import lombok.Data;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
@Data
public class LoginBack
{

    List<ImNode> imNodeList;

    private String token;

    private UserDTO userDTO;

}
