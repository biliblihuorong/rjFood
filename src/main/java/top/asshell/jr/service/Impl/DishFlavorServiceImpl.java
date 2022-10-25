package top.asshell.jr.service.Impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.asshell.jr.entiy.DishFlavor;
import top.asshell.jr.mapper.DishFlavorMapper;
import top.asshell.jr.service.DishFlavorService;
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper,DishFlavor> implements DishFlavorService {
}
