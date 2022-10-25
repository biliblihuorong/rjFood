package top.asshell.jr.controler;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.asshell.jr.VO.R;
import top.asshell.jr.entiy.Category;
import top.asshell.jr.service.CategoryService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryControler {

    @Autowired
    private CategoryService service;

    /**
     * 添加新的餐品分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> add(@RequestBody Category category){

        service.save(category);
        return R.success("成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page( int page,int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.eq(true,Category::getIsDeleted,1);
        queryWrapper.orderByAsc(Category::getSort);

        //分页查询
        service.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @DeleteMapping()
    public R<String> delete(String ids){
        System.out.println(ids);

//        Category byId = service.getById(ids);
//        byId.setIsDeleted(0);
//        service.updateById(byId);
        service.delete(ids);
        return R.success("成功");
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @PutMapping()
    public R<String> update(@RequestBody Category category){
        System.out.println(category);
        service.updateById(category);
        return R.success("成功");
    }
    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        log.info("这个是值是{}"+category.getType());
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = service.list(queryWrapper);
        return R.success(list);
    }

}
