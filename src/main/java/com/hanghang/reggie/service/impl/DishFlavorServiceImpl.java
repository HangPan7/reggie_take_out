package com.hanghang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanghang.reggie.entity.DishFlavor;
import com.hanghang.reggie.mapper.DishFlavorMapper;
import com.hanghang.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper , DishFlavor> implements DishFlavorService {
}
