//package cn.yubajin.std.config;
//
//import cn.yubajin.std.annotation.RefreshScope;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * ClassName: ConfigurationPropertiesBeans
// * Description:
// *  bean的后置处理器，配置修改事件监听后，需要重置设置bean属性值
// *  此类将有RefreshScope注解的bean属性和值封装到的fieldMapper中
// *  以便设置bean属性值
// * @author: yubj45081
// * @date: 2023/4/25 23:45
// */
// TODO 模拟配置动态刷新
//@Component
//public class ConfigurationPropertiesBeans implements BeanPostProcessor {
//
//    private Map<String, List<FieldPair>> fieldMapper = new HashMap<>();
//
//    public Map<String,List<FieldPair>> getFieldMapper(){
//        return fieldMapper;
//    }
//
//    /**
//     * 解析有RefreshScope注解的类的属性值
//     * @param bean
//     * @param beanName
//     * @return
//     * @throws BeansException
//     */
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        Class clz = bean.getClass();
//        //如果某个bean声明了RefreshScop注解，说明需要进行动态更新
//        if (clz.isAnnotationPresent(RefreshScope.class)) {
//            for (Field field : clz.getDeclaredFields()) {
//                //得到带有Value注解的filed @Value("${name}")
//                Value value = field.getAnnotation(Value.class);
//                if(value == null) {
//                    continue;
//                }
//                List<String> keyList = getPropertyKey(value.value(), 0);
//                // 循环添加bean属性的值到封装的fieldMapper中
//                for (String key : keyList) {
//                    fieldMapper.computeIfAbsent(key, (k) -> new ArrayList())
//                            .add(new FieldPair(bean, field, value.value()));
//                }
//            }
//        }
//        return bean;
//    }
//
//    //@Value("${xxx:${yyy}}")
//    private List<String> getPropertyKey (String value,int begin) {
//        int start = value.indexOf("${",begin)+2;
//        if(start < 2){
//            return new ArrayList<>();
//        }
//        int middle = value.indexOf(":", start);
//        int end = value.indexOf("}", start);
//        String key;
//        if(middle > 0 && middle < end) {
//            key = value.substring(start, middle);
//        } else {
//            key = value.substring(start, end);
//        }
//        List<String> keys = getPropertyKey(value, end);
//        keys.add(key);
//        return keys;
//    }
//
//}
