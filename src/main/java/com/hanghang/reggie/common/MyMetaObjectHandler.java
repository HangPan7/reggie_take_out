package com.hanghang.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.PriorityQueue;


/**
 * 自定义元数据对象处理器
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    BaseContext baseContext ;
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime" , LocalDateTime.now());
        metaObject.setValue("updateTime" , LocalDateTime.now());
        metaObject.setValue("createUser" , baseContext.getId());
        metaObject.setValue("updateUser" , baseContext.getId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime" , LocalDateTime.now());
        metaObject.setValue("updateUser" , baseContext.getId());

        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return 0 ;
            }
        }) ;
    }
}
