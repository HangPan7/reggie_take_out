package com.hanghang.reggie.Dto;


import com.hanghang.reggie.entity.Setmeal;
import com.hanghang.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
