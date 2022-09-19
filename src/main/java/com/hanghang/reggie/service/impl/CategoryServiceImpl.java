package com.hanghang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanghang.reggie.common.CustomException;
import com.hanghang.reggie.entity.Category;
import com.hanghang.reggie.entity.Dish;
import com.hanghang.reggie.entity.Setmeal;
import com.hanghang.reggie.mapper.CategoryMapper;
import com.hanghang.reggie.mapper.SetmealMapper;
import com.hanghang.reggie.service.CategoryService;
import com.hanghang.reggie.service.DishService;
import com.hanghang.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService ;
    @Autowired
    SetmealService setmealService ;

    @Override
    public void remove(Long id){
        /*
        * 1.根据id查询当前分类石否关联了菜品dish , 如果关联就抛出异常
        * 2.根据id查询当前分类是否关联了套餐SetMeal。如果关联就抛出异常
        * 3.均没有抛出异常就调用父类方法removeById删除数据
        */

        //1.根据id查询当前分类石否关联了菜品dish
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>() ;
        dishLambdaQueryWrapper.eq(Dish::getCategoryId , id) ;
        int count1 = dishService.count(dishLambdaQueryWrapper) ;

        if(count1 > 0){
            //表明已经关联抛出异常
            throw new CustomException("当前分类关联的菜品不能删除") ;
        }

        //2.根据id查询当前分类是否关联了套餐SetMeal
        LambdaQueryWrapper<Setmeal> SetmealLambdaQueryWrapper = new LambdaQueryWrapper<>() ;
        SetmealLambdaQueryWrapper.eq(Setmeal::getCategoryId , id) ;
        int count2 = setmealService.count(SetmealLambdaQueryWrapper) ;

        if(count2 > 0){
            //表明已经关联抛出异常
            throw new CustomException("当前分类关联了套餐不能删除") ;
        }

        //3.均没有抛出异常就调用父类方法removeById删除数据
        super.removeById(id) ;
    }

}
