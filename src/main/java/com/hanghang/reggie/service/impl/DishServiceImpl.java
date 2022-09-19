package com.hanghang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanghang.reggie.Dto.DishDto;
import com.hanghang.reggie.entity.Dish;
import com.hanghang.reggie.entity.DishFlavor;
import com.hanghang.reggie.mapper.DishMapper;
import com.hanghang.reggie.service.DishFlavorService;
import com.hanghang.reggie.service.DishService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper , Dish> implements DishService {
    @Autowired
    DishMapper dishMapper ;


    @Autowired
    DishFlavorService dishFlavorService ;

    /**
     * 根据id查询对应的菜品信息和口味信息存入dishDao中
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        DishDto dishDto = new DishDto();

        Dish dish = this.getById(id) ;

        BeanUtils.copyProperties(dish , dishDto);

        //要根据dishId查询对应的口味就需要使用条件，创建条件构造器
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>() ;
        queryWrapper.eq(DishFlavor::getDishId,id) ;
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        log.info("hhhhhhhh"+String.valueOf(flavors));
        dishDto.setFlavors(flavors);

        return dishDto ;
    }

    /**
     * 更新口味表和菜品表
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        this.updateById(dishDto) ;

        //更新口味表 可以先根据dishId删除之前对应的口味 ，然后再添加后来的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>() ;
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper) ;

        //插入口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        //将菜品id和DishDao中的Flavor遍历保存到集合中可以用for循环
        flavors = flavors.stream().map((item)-> {
            item.setDishId(dishDto.getId());
            return  item ;
        }) .collect(Collectors.toList()) ;
        dishFlavorService.saveBatch(flavors) ;
    }

    /**
     * 根据id更新状态数据
     * @param status
     * @param id
     */
    @Override
    public void update(int status, Long id) {
        dishMapper.update(status , id);
    }

    /**
     * 新增菜品同时保存对应的口味数据，需要操作两张表。dish，dishFlavor
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {

        //保存菜品的基本信息到菜品表dish中
       this.save(dishDto) ;

       //获取菜品id保存到dishFlavor表中
        Long id = dishDto.getId();

        //将菜品id和DishDao中的Flavor遍历保存到集合中可以用for循环
       List<DishFlavor> flavors = dishDto.getFlavors() ;
        flavors = flavors.stream().map((item)-> {
            item.setDishId(id);
            return  item ;
        }) .collect(Collectors.toList()) ;
        //保存菜品口味数据到菜品口味表中
      dishFlavorService.saveBatch(flavors) ;
    }
}
