package com.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.controller.dto.UserDTO;
import com.springboot.entity.User;

/**
 *  服务类
 *
 * @author 文涛
 * @since 2023-03-04
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param userDTO
     * @return
     */
    UserDTO login(UserDTO userDTO);



}
