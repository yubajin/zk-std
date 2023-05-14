package cn.yubajin.std.config;

import org.springframework.core.env.Environment;
import org.springframework.util.PropertyPlaceholderHelper;

import java.lang.reflect.Field;

/**
 * ClassName: FieldPair
 * Description:
 * 用来实现属性值的替换
 * beanPostProcessor中去设置数据到里面
 * 设置后才能将新值通过反射的方式重新设置
 * @author: yubj45081
 * @date: 2023/4/25 23:51
 */
public class FieldPair {

    private PropertyPlaceholderHelper propertyPlaceholderHelper =
            new PropertyPlaceholderHelper("${","}",":",true);

    private Object bean;
    private Field field;
    private String value;

    public FieldPair(Object bean, Field field, String value) {
        this.bean = bean;
        this.field = field;
        this.value = value;
    }

    public void resetValue(Environment environment){
        boolean access = field.isAccessible();
        if(!access){
            field.setAccessible(true);
        }
        // 注释: 占位符的使用
        // eg: value = "${name}"
        //     props = {"name" : "xiaoyuer"}
        //     helper.replacePlaceholders(value, props)
        // result: value -----> xiaoyeur
        // 用spring环境中最新的environment数据去刷新bean的属性值
        String resetValue = propertyPlaceholderHelper.replacePlaceholders(value, environment::getProperty);
        try {
            //反射修改bean的属性值
            field.set(bean, resetValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
