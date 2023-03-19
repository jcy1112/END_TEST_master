package com.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.common.CodeEnum;
import com.springboot.controller.dto.UserDTO;
import com.springboot.entity.User;
import com.springboot.exception.ServiceException;
import com.springboot.mapper.UserMapper;
import com.springboot.service.IUserService;
import com.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 服务实现类
 *
 * @author
 * @since 2023-03-04
 */
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    @Resource
    private UserMapper userMapper;

    /**
     * 登录
     *
     * @param userDTO 用户信息
     * @return
     */
    @Override
    public UserDTO login(UserDTO userDTO) {
        User user = getUserInfo(userDTO);
        if (user != null) {
            BeanUtil.copyProperties(user, userDTO, true);
            //设置token
            String token = TokenUtils.genToken(user.getId().toString(), user.getPassword());
            userDTO.setToken(token);
            //密码置空，防止将密码返回前端
            userDTO.setPassword("");
            return userDTO;
        } else {
            throw new ServiceException(CodeEnum.CODE_600.getCode(), "用户名或密码错误");
        }
    }

    /**
     * 查询用户信息的方法，给login register 使用
     *
     * @param userDTO 用户信息
     * @return
     */
    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());


        queryWrapper.eq("password", userDTO.getPassword());
        User user;
        try {
            user = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(CodeEnum.CODE_500.getCode(), "系统错误");
        }
        return user;
    }
}
