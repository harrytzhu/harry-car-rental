package com.harry.carrental.harrycarrental.mapper;

import com.harry.carrental.harrycarrental.entity.PriceEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by harryzhu on 2022/7/24
 */
@Repository
public interface PriceMapper {

    @Select({"select id, carModelId, price",
            "from Price",
            "where carModelId=#{carModelId,jdbcType=INTEGER}"})
    PriceEntity selectByCarModelId(Integer carModelId);
}
