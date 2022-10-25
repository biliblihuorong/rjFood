package top.asshell.jr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.asshell.jr.entiy.Category;
public interface CategoryService extends IService<Category> {
    public void delete(String id);
}
