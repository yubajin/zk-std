package cn.yubajin.std.controller;

import cn.yubajin.std.annotation.RefreshScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: controller.ConfigController
 * Description:
 * @Value是从环境中获取的
 * https://www.codenong.com/cs110823128/
 * @Author: yubj45081
 * Date: 2023/4/18 0:42
 */
@RestController
@RefreshScope
public class ConfigController {

    @Autowired
    Environment environment;

    //值可以从配置文件中获取，也会从环境中获取
    @Value("${name}")
    private String name;

    @Value("${job}")
    private String job;

//    @Value("${zookeeper}")
//    private String zookeeper;

    /**
     * zk节点 config
     * {"name":"yuerer0", "job":"teacher", "sex":"男", "zookeeper":"2.6.3"}
     * @return
     */
    @GetMapping("/env")
    public String env(){
        // @Value是从环境中获取的, getProperty从环境中获取，
        // 获取后populateBean将环境中的值设置到属性上
        // 调用顺序参考https://blog.csdn.net/yangshangwei/article/details/128057996?spm=1001.2014.3001.5506
        String name = environment.getProperty("name") + "\n" + this.name + "\n" ;
        String job = environment.getProperty("job") + "\n" + this.job + "\n" ;
        String zookeeper = environment.getProperty("zookeeper") + "\n" ;
        return name + job + zookeeper;
    }
}

