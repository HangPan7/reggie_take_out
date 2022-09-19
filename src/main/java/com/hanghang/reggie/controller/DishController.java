package com.hanghang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanghang.reggie.Dto.DishDto;
import com.hanghang.reggie.common.R;
import com.hanghang.reggie.entity.Category;
import com.hanghang.reggie.entity.Dish;
import com.hanghang.reggie.entity.Employee;
import com.hanghang.reggie.mapper.DishMapper;
import com.hanghang.reggie.service.CategoryService;
import com.hanghang.reggie.service.DishFlavorService;
import com.hanghang.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService ;

    @Autowired
    private DishFlavorService dishFlavorService ;

    @Autowired
    private CategoryService categoryService ;
    /**
     * 菜品新增
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);

        return R.success("添加成功") ;
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize , String name){

        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>() ;
        //分页构造器
        Page<Dish> pageInfo = new  Page<>(page , pageSize) ;
        Page<DishDto> dishDtoPage = new Page<>() ;
        //如果name不为空就执行
        queryWrapper.like(StringUtils.hasLength(name) , Dish::getName, name) ;
        queryWrapper.orderByDesc(Dish::getSort);

        //执行查询
        dishService.page(pageInfo , queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo , dishDtoPage ,"records");
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            //获取categoryId根据id获取category对象
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dishDto.setCategoryName(category.getName());

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list) ;
        return R.success(dishDtoPage) ;
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(String ids) {
        String[] split =ids.split(",");
        dishService.removeByIds(Arrays.asList(split)) ;
        return R.success("删除成功") ;
    }

    /**
     * 启用禁用菜品
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable int status , String ids){
        String[] split = ids.split(",");
        //构造条件构造器
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        List<String> strings = Arrays.asList(split);
        updateWrapper.set(Dish::getStatus , status) ;
        for (String id : strings) {
            dishService.update(status , Long.valueOf(id)) ;
        }
        return   R.success("修改成功") ;
    }

    @GetMapping("/{id}")
    public R<DishDto> updata(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto) ;
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        Properties p = new Properties() ;
      return  R.success("修改菜品成功") ;
    }

    /**
     * 根据条件查询对应的菜品对象
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){

        //构造条件查询
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>() ;
        queryWrapper.eq(dish.getCategoryId() != null , Dish::getCategoryId , dish.getCategoryId()) ;
        queryWrapper.eq(Dish::getStatus , 1) ;
        //构造排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime) ;

        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list) ;
    }
}














