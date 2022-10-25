package top.asshell.jr.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.asshell.jr.common.CustomExceptionL;
import top.asshell.jr.entiy.Category;
import top.asshell.jr.entiy.Dish;
import top.asshell.jr.entiy.Setmeal;
import top.asshell.jr.mapper.CategoryMapper;
import top.asshell.jr.mapper.DishMapper;
import top.asshell.jr.mapper.SetmealMapper;
import top.asshell.jr.service.CategoryService;
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private CategoryMapper category;
    @Override
    public void delete(String id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper =new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        Long cong = dishMapper.selectCount(dishLambdaQueryWrapper);
        if (cong>0){
        throw new CustomExceptionL("当前分类不能删除，当前分类还有绑定的菜品");
        }
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        Long aLong = setmealMapper.selectCount(lambdaQueryWrapper);
        if (aLong>0){
            throw new CustomExceptionL("当前分类不能删除，当前分类还有绑定的套餐");
        }
        Category category1 = category.selectById(id);
        category1.setIsDeleted(0);
        category.updateById(category1);
    }
}
