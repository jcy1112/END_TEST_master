package com.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.entity.Goods;
import com.springboot.mapper.GoodsMapper;
import com.springboot.service.IGoodsService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author
 * @since 2023-03-4
 */
@Service
public class IGoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

}
