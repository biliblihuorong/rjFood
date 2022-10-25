package top.asshell.jr.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.asshell.jr.entiy.User;
import top.asshell.jr.mapper.UserMapper;
import top.asshell.jr.service.UserService;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
