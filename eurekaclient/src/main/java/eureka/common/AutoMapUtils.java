package eureka.common;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author ""
 * @description 自动对象映射工具，通常用于实体和DTO参数转换，避免暴露隐秘数据
 * @date 2018/4/28
 * @since jdk1.8
 */
public class AutoMapUtils {

    private static DozerBeanMapper mapper = new DozerBeanMapper();

    /**
     * 把源对象的属性映射到目标对象，根据相同属性名映射，包含普通POJO和集合对象
     * @param source 源对象
     * @param destinationClass  得到源对象数据的目标类
     * @param <T> 目标类对象
     * @return 标类型的实例对象
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    /**
     * 把一个集合每个元素的数据映射到另一个集合，根据相同属性名映射，包含普通POJO和集合对象
     * @param source 源对象集合
     * @param destinationClass 目标元素类型
     * @param <T> 类型参数
     * @return 目标类型元素的集合
     */
    public static <T> List<T> mapList(Collection source, Class<T> destinationClass) {
        List<T> result = Lists.newArrayList();
        Iterator iterator = source.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            result.add(mapper.map(next, destinationClass));
        }
        return result;
    }
}
