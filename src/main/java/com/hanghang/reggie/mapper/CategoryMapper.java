package com.hanghang.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanghang.reggie.entity.Category;
import com.hanghang.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
