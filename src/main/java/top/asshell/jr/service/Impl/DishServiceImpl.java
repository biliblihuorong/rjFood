package top.asshell.jr.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.asshell.jr.DTO.DishDto;
import top.asshell.jr.entiy.Dish;
import top.asshell.jr.entiy.DishFlavor;
import top.asshell.jr.mapper.DishMapper;
import top.asshell.jr.service.DishFlavorService;
import top.asshell.jr.service.DishService;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存餐品（Dish）的基本数据
        this.save(dishDto);
        //拿到菜品id
        String id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        //菜品口味
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public DishDto getByIdWithFlavor(String id) {
        Dish byId = this.getById(id);
        System.out.println(byId);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId,dishDto);

        LambdaQueryWrapper<DishFlavor> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        List<DishFlavor> list = dishFlavorService.list(wrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新Dish基本信息
        this.updateById(dishDto);
        //删除所有这个DishFlavor的相关数据
        LambdaQueryWrapper<DishFlavor> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(wrapper);
        //创建新的DishFlavor相关数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(flavors);

    }
}
