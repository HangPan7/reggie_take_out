package com.hanghang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hanghang.reggie.common.R;
import com.hanghang.reggie.entity.Employee;
import com.hanghang.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService ;

    /**
     * 登陆页面操作
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request , @RequestBody Employee employee){
        /*
        * 1.根据页面提交的密码password进行MD5加密处理
        * 2.根据页面提交的用户username查询数据库
        * 3.如果没有查询到则返回登陆失败结果
        * 4.密码比对，如果不一致则返回登陆结果
        * 5.查询员工状态，如果为已禁用，则返回禁用结果
        * 6.登陆成功，将员工id存入session并返回登陆成功结果
        * */

     /*   * 1.根据页面提交的密码password进行MD5加密处理*/
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes()) ;

        /* * 2.根据页面提交的用户username查询数据库*/
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>() ;
        queryWrapper.eq(Employee::getUsername ,employee.getUsername()) ;
        Employee emp = employeeService.getOne(queryWrapper) ;

        /* * 3.如果没有查询到则返回登陆失败结果*/
        if(emp == null){
            return R.error("登陆失败") ;
        }

        //4.密码比对，如果不一致则返回登陆结果
        if(!emp.getPassword().equals(password)){
            return R.error("登陆失败") ;
        }

        // * 5.查询员工状态，如果为已禁用，则返回禁用结果
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        //* 6.登陆成功，将员工id存入session并返回登陆成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp) ;
    }

    /**
     * 退出页面
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理session中保存的员工id
          request.getSession().removeAttribute("employee");
        return R.success("退出成功") ;
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request , @RequestBody Employee employee){
        //设置默认密码123456并且用MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

    /*    employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());*/

        //获得当前用户id
      /*  Long empId = (Long) request.getSession().getAttribute("employee") ;*/
/*

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
*/

        employeeService.save(employee) ;

        return R.success("新增员工成功") ;
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize , String name){

        //分页构造器
        Page pageInfo = new  Page<>(page , pageSize) ;

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>() ;
        //添加过滤条件
        queryWrapper.like(StringUtils.hasLength(name) , Employee::getName, name) ;
        //添加排序条件 按添加时间排序
        queryWrapper.orderByDesc(Employee::getUpdateTime) ;

        //执行查询
        employeeService.page(pageInfo , queryWrapper) ;

        return R.success(pageInfo) ;
    }

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update (HttpServletRequest request ,@RequestBody Employee employee){


        employeeService.updateById(employee) ;
        return  R.success("员工信息修改成功") ;
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){

        Employee employee = employeeService.getById(id) ;

        return R.success(employee) ;
    }
}


















