package com.hanghang.reggie.common;

import org.springframework.stereotype.Component;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登陆用户id
 */
@Component
public class BaseContext {

    private Long id ;

    public  void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
