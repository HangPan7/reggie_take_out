package com.hanghang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanghang.reggie.Dto.SetmealDto;
import com.hanghang.reggie.common.CustomException;
import com.hanghang.reggie.entity.Dish;
import com.hanghang.reggie.entity.Setmeal;
import com.hanghang.reggie.entity.SetmealDish;
import com.hanghang.reggie.mapper.DishMapper;
import com.hanghang.reggie.mapper.SetmealMapper;
import com.hanghang.reggie.service.SetmealDishService;
import com.hanghang.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper , Setmeal>  implements SetmealService {

    @Autowired
    SetmealDishService setmealDishService ;

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存菜品的基本信息
        this.save(setmealDto) ;

        //保存套餐和菜品的关联信息。
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        //1.遍历setmealDishs 给里面的每一个setmealDish中的setmealId赋值
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item ;
        }).collect(Collectors.toList()) ;
        //2.保存setmealDishs
        setmealDishService.saveBatch(setmealDishes) ;
    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    @Override
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper() ;
        queryWrapper.in(Setmeal ::getId ,ids) ;
        queryWrapper.eq(Setmeal :: getStatus , 1) ;
        int count = this.count(queryWrapper);

        if(count > 0) throw new CustomException("套餐正在售卖") ;

        //如果可以删除，先删除套餐表，再删除关系表
        this.removeByIds(ids) ;

        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper() ;
        queryWrapper1.in(SetmealDish ::getSetmealId , ids) ;
        setmealDishService.remove(queryWrapper1) ;
    }

    /**
     * 根据id查询套餐表和关联数据表
     * @param id
     * @return
     */
    @Override
    public SetmealDto putById(Long id) {
        SetmealDto setmealDto = new SetmealDto() ;
        //套餐表

        Setmeal setmeal = this.getById(id);
        //关联数据表
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper() ;
        queryWrapper.eq(SetmealDish::getSetmealId , id) ;
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        //将两个表中的数据存入setmealDto返回
        BeanUtils.copyProperties(setmeal , setmealDto);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    /**
     * 修改数据
     * @param setmealDto
     */
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //修改套餐表
        this.updateById(setmealDto) ;

        //修改关联数据表
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper() ;
        queryWrapper.eq(SetmealDish::getSetmealId , setmealDto.getId()) ;
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //先删除之前对应的口味
        setmealDishService.remove(queryWrapper) ;
        //再加入修改后的口味
        List<SetmealDish> list = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(list) ;
    }
}
