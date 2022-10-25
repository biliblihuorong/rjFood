package top.asshell.jr.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import top.asshell.jr.VO.R;
import top.asshell.jr.entiy.Employee;
import top.asshell.jr.mapper.EmployeeMapper;
import top.asshell.jr.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;



    @Override
    public R query(Employee employee, HttpServletRequest request) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        QueryWrapper<Employee> wrapper =new QueryWrapper<>();
        wrapper.eq("username",employee.getUsername());
        Employee one = employeeMapper.selectOne(wrapper);
        if (one==null)
            return  R.error("用户名不可用");
        if (one.getStatus()==0)
            return  R.error("您的账户已经被禁用请联系管理员");
        if (one.getPassword().equals(password)){
            request.getSession().setAttribute("employee",one.getId());
            log.info("已经成功登录");
            return R.success(one);
        }
        return R.error("密码不可用");
    }
}
