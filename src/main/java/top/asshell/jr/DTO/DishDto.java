package top.asshell.jr.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import top.asshell.jr.entiy.Dish;
import top.asshell.jr.entiy.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    //菜品对应的口味数据
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
