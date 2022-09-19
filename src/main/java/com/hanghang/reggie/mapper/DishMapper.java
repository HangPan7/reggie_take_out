package com.hanghang.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hanghang.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Update("update dish set status = #{status} where id = #{id}")
    public void update(int status , Long id) ;
}
