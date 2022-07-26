package com.harry.carrental.harrycarrental.mapper;

import com.harry.carrental.harrycarrental.entity.OrderEntity;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by harryzhu on 2022/7/25
 */
@Repository
public interface OrderMapper {

    @Insert({"insert into [Order] (userId, carId, carModelId, startDate, endDate, deposit, status, createTime, enabled)",
            "values",
            "(#{userId,jdbcType=INTEGER},",
            "#{carId,jdbcType=INTEGER},",
            "#{carModelId,jdbcType=INTEGER},",
            "#{startDate,jdbcType=DATE},",
            "#{endDate,jdbcType=DATE},",
            "#{deposit,jdbcType=INTEGER},",
            "#{status,jdbcType=VARCHAR},",
            "#{createTime,jdbcType=DATE},",
            "#{enabled,jdbcType=VARCHAR}",
            ")"})
    @Options(useGeneratedKeys=true, keyProperty="id")
    void createOrder(OrderEntity orderEntity);

    @Select({"select id, userId, carId, carModelId, startDate, endDate, deposit, status, createTime",
            "from [Order]",
            "where carModelId=#{carModelId,jdbcType=INTEGER}",
            "and enabled = 'true'",
            "and endDate >= #{startDate,jdbcType=VARCHAR}",
            "and startDate <= #{endDate,jdbcType=VARCHAR}"})
    List<OrderEntity> selectByCarModelIdAndDate(@Param("carModelId") Integer carModelId,
            @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select({"select id, userId, carId, carModelId, startDate, endDate, deposit, status, createTime",
            "from [Order]",
            "where userId=#{userId,jdbcType=INTEGER}"})
    List<OrderEntity> selectByUserId(Integer userId);

    @Update({"update [Order] set status=#{status,jdbcType=VARCHAR} where id=#{id,jdbcType=INTEGER}"})
    void updateStatus(@Param("id") Integer id, @Param("status") String status);

    @Select({"select id, userId, carId, carModelId, startDate, endDate, deposit, status, createTime",
            "from [Order]",
            "where id=#{id,jdbcType=INTEGER}"})
    OrderEntity selectById(Integer id);
}
