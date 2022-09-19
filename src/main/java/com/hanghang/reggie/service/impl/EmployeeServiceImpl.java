package com.hanghang.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hanghang.reggie.entity.Employee;
import com.hanghang.reggie.mapper.EmployeeMapper;
import com.hanghang.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper , Employee> implements EmployeeService {
}
