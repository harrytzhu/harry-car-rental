package com.harry.carrental.harrycarrental.mapper;

import com.harry.carrental.harrycarrental.entity.CarEntity;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by harryzhu on 2022/7/24
 */
@Repository
public interface CarMapper {

    @Select({"select id, carModelId, description, isReserved, enabled",
            "from car",
            "where id=#{id,jdbcType=INTEGER}"})
    CarEntity selectById(Integer id);

    @Select({"select id, carModelId, description, isReserved, enabled",
            "from car",
            "where carModelId=#{carModelId,jdbcType=INTEGER}"})
    List<CarEntity> selectByCarModelId(Integer carModelId);

    @Update({"update Car set carModelId=#{carModelId,jdbcType=INTEGER}"})
    void update(CarEntity carEntity);
}
