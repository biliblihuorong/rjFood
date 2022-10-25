package top.asshell.jr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.asshell.jr.VO.R;
import top.asshell.jr.entiy.Employee;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    R query(Employee employee, HttpServletRequest request);


}
