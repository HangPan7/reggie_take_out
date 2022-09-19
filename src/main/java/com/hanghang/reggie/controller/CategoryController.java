package com.hanghang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanghang.reggie.common.R;
import com.hanghang.reggie.entity.Category;
import com.hanghang.reggie.entity.Employee;
import com.hanghang.reggie.service.CategoryService;
import com.hanghang.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 增加套餐和菜品分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category) ;
     return    R.success("新增菜品成功") ;
    }

    /**
     * 修改菜品
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update ( @RequestBody Category category){

        categoryService.updateById(category) ;
        return  R.success("信息修改成功") ;
    }

    /**
     * 菜品信息分类查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize){

        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>() ;
        //分页构造器
        Page pageInfo = new  Page<>(page , pageSize) ;

       queryWrapper.orderByDesc(Category::getSort);
        //执行查询
        categoryService.page(pageInfo , queryWrapper);

        return R.success(pageInfo) ;
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.remove(ids) ;
        return R.success("分类删除成功") ;
    }

    /**
     * 根据条件分类查询数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>() ;
        //添加条件
        queryWrapper.eq(category.getType()!=null ,Category::getType , category.getType()) ;

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list) ;
    }
}
