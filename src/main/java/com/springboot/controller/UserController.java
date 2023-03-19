package com.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.springboot.common.CodeEnum;
import com.springboot.common.Result;
import com.springboot.controller.dto.UserDTO;
import com.springboot.entity.User;
import com.springboot.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户控制类
 *
 * @author 文涛
 * @since 2022-01-26
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 登录
     *
     * @param userDTO 用户信息
     * @return Result
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if ("".equals(username) || "".equals(password)) {
            return Result.error(CodeEnum.CODE_400.getCode(), "参数错误");
        }
        Result result = userService.login(userDTO);
        return result;
    }


    /**
     * 新增或更新
     *
     * @param user 用户信息
     * @return Result
     */
    @PostMapping
    public Result save(@RequestBody User user) {
        String username = user.getUsername();
        if ("".equals(username)) {
            return Result.error(CodeEnum.CODE_400.getCode(), "用户名不可修改");
        }
        if ("".equals(user.getNickname())) {
            user.setNickname(username);
        }
        if (user.getId() != null) {
            user.setPassword(null);
        } else {
            if (user.getPassword() == null) {
                user.setPassword("123");
            }
        }
        return Result.success("保存成功", userService.saveOrUpdate(user));
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return Result
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean value = userService.removeById(id);
        if (!value) {
            return Result.error(CodeEnum.CODE_402.getCode(), "删除失败");
        }
        return Result.success("删除成功");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return Result
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean value = userService.removeByIds(ids);
        if (!value) {
            return Result.error(CodeEnum.CODE_402.getCode(), "批量删除失败");
        }
        return Result.success("批量删除成功");
    }


    /**
     * 查询所有
     *
     * @return Result
     */
    @GetMapping
    public Result findAll() {
        List<User> userList = userService.list();
        if (userList != null) {
            return Result.success("查询成功", userList);
        }
        return Result.error(CodeEnum.CODE_400.getCode(), "未查找到数据");

    }

    /**
     * 通过id查找
     *
     * @param id
     * @return Result
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        User user = userService.getById(id);
        if (user != null) {
            return Result.success("查询成功", user);
        }
        return Result.error(CodeEnum.CODE_400.getCode(), "未查找到数据");
    }


    /**
     * 通过用户名查找
     *
     * @param username
     * @return Result
     */
    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userService.getOne(queryWrapper);
        if (user != null) {
            return Result.success("查询成功", user);
        }
        return Result.error(CodeEnum.CODE_400.getCode(), "未查找到数据");
    }


    /**
     * 分页查询，条件查询
     *
     * @param pageNum  页码
     * @param pageSize 一页展示条数
     * @param username 名字
     * @param email    邮箱
     * @param address  地址
     * @return Result
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }
        Page<User> userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        if (userPage != null) {
            return Result.success("查询成功", userPage);
        }
        return Result.error(CodeEnum.CODE_400.getCode(), "未查找到数据");
    }

}

