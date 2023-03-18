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
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if ("".equals(username) || "".equals(password)) {
            return Result.error(CodeEnum.CODE_400.getCode(), "参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success("登录成功！", dto);
    }


    /**
     * 新增或更新
     *
     * @param user 用户信息
     * @return
     */
    @PostMapping
    public Result save(@RequestBody User user) {
        String username = user.getUsername();
        if ("".equals(username)) {
            return Result.error(CodeEnum.CODE_400.getCode(), "参数错误");
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
     * @return
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
     * @return
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean value = userService.removeByIds(ids);
        if (!value) {
            return Result.error(CodeEnum.CODE_402.getCode(), "删除失败");
        }
        return Result.success("删除成功");
    }


    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        return Result.success("查询成功", userService.list());
    }


    /**
     * 通过id查找
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success("查询成功", userService.getById(id));
    }


    /**
     * 通过用户名查找
     *
     * @param username
     * @return
     */
    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success("查询成功", userService.getOne(queryWrapper));
    }


    /**
     * 分页查询，条件查询
     *
     * @param pageNum  页码
     * @param pageSize 一页展示条数
     * @param username 名字
     * @param email    邮箱
     * @param address  地址
     * @return
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
        return Result.success("查询成功", userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

