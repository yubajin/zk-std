//package config;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.env.EnvironmentPostProcessor;
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.springframework.core.env.PropertiesPropertySource;
//import org.springframework.core.env.PropertySource;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//
//import java.io.IOException;
//import java.util.Properties;
//
///**
// * ClassName: CustomEnvironmentPostProcessor
// * Description:
// * 通过扩展点加载配置到spring中
// * @Author: yubj45081
// * Date: 2023/4/18 0:42
// */
//public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {
//    private final Properties properties=new Properties();
//    private String propertiesFile="custom.properties";
//
//    @Override
//    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
//        Resource resource=new ClassPathResource(propertiesFile);
//        // 将配置加到environment的propertySource中
//        environment.getPropertySources().addLast(loadProperties(resource));
//    }
//
//    private PropertySource<?> loadProperties(Resource resource) {
//        if(!resource.exists()){
//            throw new RuntimeException("file not exist");
//        }
//        try {
//            //custom.properties
//            properties.load(resource.getInputStream());
//            return new PropertiesPropertySource(resource.getFilename(), properties);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
