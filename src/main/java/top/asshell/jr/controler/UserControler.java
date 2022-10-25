package top.asshell.jr.controler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.asshell.jr.DTO.ValidateCodeUtils;
import top.asshell.jr.VO.R;
import top.asshell.jr.entiy.User;
import top.asshell.jr.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserControler {
    @Autowired
    private UserService service;

    /**
     * 发送验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            String code = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
            log.info("验证码："+code);
            session.setAttribute(phone,code);
            return R.success("成功");
        }
        return R.error("");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody HashMap map,HttpSession session){

        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session里面获取验证码
        String sessionCode = (String) session.getAttribute(phone);
        //进行比对
        if (sessionCode!=null&& sessionCode.equals(code)) {
            //如果成功，那就登录成功
            //如果是新用户，自动保存
            LambdaQueryWrapper<User> wrapper =new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone,phone);
            User one = service.getOne(wrapper);
            if (one==null){
                one =new User();
                one.setStatus(1);
                one.setPhone(phone);
                service.save(one);
            }
            session.setAttribute("user",one.getId());
            return R.success(one);
        }

        return R.error("失败");
    }

}
