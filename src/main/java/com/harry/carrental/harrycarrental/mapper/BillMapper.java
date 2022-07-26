package com.harry.carrental.harrycarrental.mapper;

import com.harry.carrental.harrycarrental.entity.BillEntity;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by harryzhu on 2022/7/26
 */
@Repository
public interface BillMapper {

    @Select({"select id, orderId, userId, carModelId, actualReturnDate, expireDays, rentCost, carDamageCost, fines, totalCost, isPaid, addAmount, returnAmount",
            "from Bill",
            "where orderId=#{orderId,jdbcType=INTEGER}"})
    BillEntity selectByOrderId(Integer orderId);

    @Insert({"insert into Bill",
            "values",
            "(#{orderId,jdbcType=INTEGER},",
            "#{userId,jdbcType=INTEGER},",
            "#{carModelId,jdbcType=INTEGER},",
            "#{actualReturnDate,jdbcType=DATE},",
            "#{expireDays,jdbcType=INTEGER},",
            "#{rentCost,jdbcType=INTEGER},",
            "#{carDamageCost,jdbcType=INTEGER},",
            "#{fines,jdbcType=INTEGER},",
            "#{totalCost,jdbcType=INTEGER},",
            "#{isPaid,jdbcType=VARCHAR},",
            "#{addAmount,jdbcType=INTEGER},",
            "#{returnAmount,jdbcType=INTEGER})"})
    int insert(BillEntity billEntity);

    @Select({"select id, orderId, userId, carModelId, actualReturnDate, expireDays, rentCost, carDamageCost, fines, totalCost, isPaid, addAmount, returnAmount",
            "from Bill",
            "where userId=#{userId,jdbcType=INTEGER}"})
    List<BillEntity> selectByUserId(Integer userId);

    @Update({"update Bill set isPaid='true' where id=#{billId,jdbcType=INTEGER}"})
    int setPaid(Integer billId);

    @Select({"select id, orderId, userId, carModelId, actualReturnDate, expireDays, rentCost, carDamageCost, fines, totalCost, isPaid, addAmount, returnAmount",
            "from Bill",
            "where id=#{id,jdbcType=INTEGER}"})
    BillEntity selectById(Integer id);
}
