package top.asshell.jr.DTO;


import lombok.Data;
import top.asshell.jr.entiy.Setmeal;
import top.asshell.jr.entiy.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
