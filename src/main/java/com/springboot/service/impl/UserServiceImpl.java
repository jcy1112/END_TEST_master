package com.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.common.CodeEnum;
import com.springboot.common.Result;
import com.springboot.controller.dto.UserDTO;
import com.springboot.entity.User;
import com.springboot.exception.ServiceException;
import com.springboot.mapper.UserMapper;
import com.springboot.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Log LOG = Log.get();

    /**
     * 登录
     * @param userDTO 前端传回的信息
     * @return
     */
    @Override
    public Result login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        User user = getOne(queryWrapper);
        //判断是否存在此用户
        if (user != null){
            //判断密码是否正确
            if (userDTO.getPassword().equals(user.getPassword())){
                BeanUtil.copyProperties(user, userDTO, true);
                //设置token
                String token = TokenUtils.genToken(user.getId().toString(), user.getPassword());
                userDTO.setToken(token);
                //密码置空，防止将密码返回前端
                userDTO.setPassword("");
                return Result.success("登录成功",userDTO);
            }else{
                return Result.error(CodeEnum.CODE_600.getCode(), "用户名或密码错误");
            }
        }else {
            return Result.error(CodeEnum.CODE_600.getCode(), "用户不存在，请注册");
        }
    }

}
