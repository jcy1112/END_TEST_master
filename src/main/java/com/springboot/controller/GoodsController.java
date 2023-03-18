package com.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springboot.common.Result;
import com.springboot.common.AuthAccess;
import com.springboot.service.GoodsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.springboot.entity.Goods;

import org.springframework.web.bind.annotation.RestController;

/**
 * 商品控制类
 *
 * @author 文涛
 * @since 2023-03-04
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    /**
     * 前台商品列表，
     *
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     * @AuthAccess 自定义注解 放行权限
     */
    @AuthAccess
    @GetMapping("/front")
    public Result frontAll(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {

        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if (!"".equals(name)) {
            queryWrapper.like("name", name);
        }
        return Result.success(goodsService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * 前台按照id查询商品
     *
     * @param id
     * @return
     */
    @AuthAccess
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success("查询成功", goodsService.getById(id));
    }
}

