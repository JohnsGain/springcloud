package eureka.repository;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author zhangjuwa
 * @description
 * @date 2018/9/17
 * @since jdk1.8
 */
@Repository
public interface OrderRepository {

    @Update("update `order` set total = #{total} where id = #{id}")
    Integer decreaseOrder(@Param("id")Integer id, @Param("total")Integer total);

    @Select("select total from `order` where id = #{id}")
    Integer findById(@Param("id")Integer id);
}
