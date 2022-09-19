package com.hanghang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanghang.reggie.Dto.DishDto;
import com.hanghang.reggie.Dto.SetmealDto;
import com.hanghang.reggie.common.R;
import com.hanghang.reggie.entity.Category;
import com.hanghang.reggie.entity.Setmeal;
import com.hanghang.reggie.service.CategoryService;
import com.hanghang.reggie.service.SetmealDishService;
import com.hanghang.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    SetmealDishService setmealDishService ;

    @Autowired
    SetmealService setmealService ;

    @Autowired
    CategoryService categoryService ;
    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    /**
     * 分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize ,String name){
        //构造分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>() ;
        //添加查询条件，根据name进行like查询
        queryWrapper.like(name != null , Setmeal::getName , name) ;
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime) ;
        setmealService.page(pageInfo , queryWrapper) ;
        //对象拷贝
        BeanUtils.copyProperties(pageInfo , dtoPage , "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询对象
            Category byId = categoryService.getById(categoryId);
            if (byId != null) {
                setmealDto.setCategoryName(byId.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list) ;
        return R.success(dtoPage);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    /**
     * 根据id查找并返回数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> putByID(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.putById(id);
        return R.success(setmealDto) ;
    }

    /**
     * 修改数据
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDish(setmealDto);
        return R.success("修改成功") ;
    }

    /**
     * 修改状态
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> upStatus(@RequestParam List<Long> ids , @PathVariable int status){
        LambdaUpdateWrapper<Setmeal> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        for (Long id : ids) {
            lambdaUpdateWrapper.eq(Setmeal::getId , id).set(Setmeal::getStatus,status);
            setmealService.update(lambdaUpdateWrapper);
        }
        return R.success("禁用成功") ;
    }
}
