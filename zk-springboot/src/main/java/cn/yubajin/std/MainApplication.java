package cn.yubajin.std;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * ClassName: SpringbootZKApplication
 * Description:
 *
 * @Author: yubj45081
 * Date: 2023/4/18 0:10
 */
@EnableAutoConfiguration
@ComponentScan(basePackages = {"cn.yubajin.std"})
@SpringBootConfiguration
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
