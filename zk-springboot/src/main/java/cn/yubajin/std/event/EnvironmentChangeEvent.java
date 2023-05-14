package cn.yubajin.std.event;

import org.springframework.context.ApplicationEvent;

/**
 * ClassName: EnvironmentChangeEvent
 * Description:
 * zk的数据变更事件
 * 订阅事件的会去修改属性值
 * @author: yubj45081
 * @date: 2023/4/25 23:22
 */
public class EnvironmentChangeEvent  extends ApplicationEvent {

    public EnvironmentChangeEvent(Object source) {
        super(source);
    }
}
