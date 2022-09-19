package com.hanghang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hanghang.reggie.Dto.SetmealDto;
import com.hanghang.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto) ;

    void removeWithDish(List<Long> ids) ;

    SetmealDto putById(Long id) ;

    void updateWithDish(SetmealDto setmealDto) ;
}
