package top.asshell.jr.controler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.asshell.jr.DTO.DishDto;
import top.asshell.jr.VO.R;
import top.asshell.jr.entiy.Category;
import top.asshell.jr.entiy.Dish;
import top.asshell.jr.entiy.DishFlavor;
import top.asshell.jr.service.CategoryService;
import top.asshell.jr.service.DishFlavorService;
import top.asshell.jr.service.DishService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService service;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> get(Integer page, Integer pageSize){
        Page dishPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(true,Dish::getIsDeleted,0);

//        dishLambdaQueryWrapper.eq(true,Dish::getStatus,1);

        service.page(dishPage,dishLambdaQueryWrapper);

        return R.success(dishPage);
    }

    /**
     *  批量（单个）起售或者停售
     * @param ids
     * @param id
     * @return
     */
    @PostMapping("/status/{id}")
    public R status(@RequestParam List<String> ids, @PathVariable Integer id){
        if (ids.size()==0)
            return R.error("未知异常");
        if (id==1){
            int temp=1;
            for (String s : ids) {
                Dish byId = service.getById(s);
                byId.setStatus(1);
                service.updateById(byId);
                temp++;
            }
            return R.success("成功起售"+temp+"个");
        }
        if (id==0){
            int temp=1;
            for (String s : ids) {
                Dish byId = service.getById(s);
                byId.setStatus(0);
                service.updateById(byId);
                temp++;
            }
            return R.success("停售成功"+temp+"个");
        }


        return R.error("未知异常");

    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param ids
     * @return
     */
    @DeleteMapping()
    public R delete(@RequestParam List<String> ids){
        if (ids.size()==0)
            return R.error("未知异常");
            for (String s : ids) {
                Dish byId = service.getById(s);
                byId.setIsDeleted(1);
                service.updateById(byId);
            }
            return R.success("删除成功");
    }

    /**
     * 新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish、dish_flavor
     * @param dishDto
     * @return
     */
    @PostMapping()
    public R<String>  addDish(@RequestBody DishDto dishDto){

        service.saveWithFlavor(dishDto);
        return R.success("成功");
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R<DishDto> getDishQuery(@PathVariable String id){
        DishDto byIdWithFlavor = service.getByIdWithFlavor(id);
        return R.success(byIdWithFlavor);
    }

    /**
     * 更新菜品信息，同时更新对应的口味信息
     * @param dishDto
     * @return
     */
    @PutMapping()
    public R<String> update(@RequestBody DishDto dishDto){
        service.updateWithFlavor(dishDto);
        return R.success("保存成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = service.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = Long.valueOf(item.getCategoryId());//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = Long.valueOf(item.getId());
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}
