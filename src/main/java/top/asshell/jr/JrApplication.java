package top.asshell.jr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@MapperScan("top.asshell.jr.mapper")
@EnableTransactionManagement
public class JrApplication {

    public static void main(String[] args) {
        SpringApplication.run(JrApplication.class, args);
    }

}
