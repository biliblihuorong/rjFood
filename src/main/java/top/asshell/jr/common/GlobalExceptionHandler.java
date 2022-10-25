package top.asshell.jr.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.asshell.jr.VO.R;

import java.sql.SQLIntegrityConstraintViolationException;
@Slf4j
@RestControllerAdvice (annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        if (ex.getMessage().contains(("Duplicate entry"))) {
            String[] strings = ex.getMessage().split(" ");
            String msg = strings[2]+"已经存在";
           return R.error(msg);

        }
        return R.error("未知错误");
    }
    @ExceptionHandler(CustomExceptionL.class)
    public R<String> exceptionHandler(CustomExceptionL ex){
        log.error(ex.getMessage());

        return R.error(ex.getMessage());
    }
}
