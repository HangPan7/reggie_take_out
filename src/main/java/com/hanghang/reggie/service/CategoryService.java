package com.hanghang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanghang.reggie.entity.Category;
import com.hanghang.reggie.entity.Employee;

public interface CategoryService extends IService<Category> {

    void remove(Long id);
}
