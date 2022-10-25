package top.asshell.jr.controler;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import top.asshell.jr.VO.R;
import top.asshell.jr.common.CustomExceptionL;
import top.asshell.jr.entiy.Employee;
import top.asshell.jr.service.EmployeeService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeControler {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 员工登录方法
     * @param employee
     * @param request
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request){
        return employeeService.query(employee,request);
    }

    /**
     * 员工退出方法
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("已经成功退出");
    }

    /**
     * 添加员工
     * @param e
     * @param request
     * @return
     */
    @PostMapping
    public R<String> add(@RequestBody Employee e,HttpServletRequest request){
        e.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        String empId = (String) request.getSession().getAttribute("employee");
        if (!(empId.equals("1")))
            new CustomExceptionL("您不是管理员无权操作");

//        e.setCreateTime(LocalDateTime.now());
//        e.setUpdateTime(LocalDateTime.now());
//        String empId = (String) request.getSession().getAttribute("employee");
//        e.setCreateUser(Long.valueOf(empId));
//        e.setUpdateUser(Long.valueOf(empId));

        boolean save = employeeService.save(e);
        return R.success("操作成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 注销员工
     * @param employee
     * @param request
     * @return
     */
    @PutMapping("")
    public R update(@RequestBody Employee employee,HttpServletRequest request){
        String empId = (String) request.getSession().getAttribute("employee");
        if (!(empId.equals("1")))
            new CustomExceptionL("您不是管理员无权操作");
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        if (employee.getStatus()==1){
//            employee.setUpdateTime(LocalDateTime.now());
//            employee.setUpdateUser(Long.valueOf(empId));

            employeeService.updateById(employee);
            System.out.println(employee);
            return R.success("启用成功");
        }
        if (employee.getStatus()==0){
//            employee.setUpdateUser(Long.valueOf(empId));
//            employee.setUpdateTime(LocalDateTime.now());
            Employee byId = employeeService.getById(employee.getId());

            if (employee.getId().equals("1"))
                throw new CustomExceptionL("您不能禁用您自己");
            employeeService.updateById(employee);
            return R.success("禁用成功");
        }

        return R.error("未知错误");

    }

    /**
     * 根据员工id进行查询
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<Employee> queryId(@PathVariable String id){
        Employee employee = employeeService.getById(id);
        System.out.println(employee);
        return R.success(employee);
    }


}
