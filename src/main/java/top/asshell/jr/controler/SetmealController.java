package top.asshell.jr.controler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import top.asshell.jr.DTO.DishDto;
import top.asshell.jr.DTO.SetmealDto;
import top.asshell.jr.VO.R;
import top.asshell.jr.entiy.Dish;
import top.asshell.jr.entiy.Setmeal;
import top.asshell.jr.entiy.SetmealDish;
import top.asshell.jr.service.SetmealDishService;
import top.asshell.jr.service.SetmealService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService service;

    @Autowired
    private SetmealDishService dishService;

    @GetMapping("/page")
    public R<Page> get(Integer page, Integer pageSize){
        Page setmealPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Setmeal> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.eq(true,Setmeal::getIsDeleted,0);

//        dishLambdaQueryWrapper.eq(true,Dish::getStatus,1);

        service.page(setmealPage,dishLambdaQueryWrapper);

        return R.success(setmealPage);
    }
    @PostMapping("/status/{id}")
    public R status(@RequestParam List<String> ids, @PathVariable Integer id){
        if (ids.size()==0)
            return R.error("未知异常");
        if (id==1){
            int temp=1;
            for (String s : ids) {
                Setmeal byId = service.getById(s);
                byId.setStatus(1);
                service.updateById(byId);
                temp++;
            }
            return R.success("成功起售"+temp+"个");
        }
        if (id==0){
            int temp=1;
            for (String s : ids) {
                Setmeal byId = service.getById(s);
                byId.setStatus(0);
                service.updateById(byId);
                temp++;
            }
            return R.success("停售成功"+temp+"个");
        }


        return R.error("未知异常");

    }

    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){


        service.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);

        service.removeWithDish(ids);

        return R.success("套餐数据删除成功");
    }

    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = service.list(queryWrapper);

        return R.success(list);
    }
    @GetMapping("{id}")
    public R getIDQuery(@PathVariable String id){
        Setmeal byId = service.getById(id);
        return R.success(byId);
    }

    @GetMapping("/dish/{id}")
    public R<Setmeal> getSetmealDto(@PathVariable String id){
        Setmeal byId = service.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(byId,setmealDto);
        LambdaQueryWrapper<SetmealDish> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId,byId.getId());
        List<SetmealDish> list = dishService.list(wrapper);
        setmealDto.setSetmealDishes(list);
        return R.success(byId);
    }
}
