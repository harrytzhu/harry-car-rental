package com.harry.carrental.harrycarrental.mapper;

import com.harry.carrental.harrycarrental.entity.UserEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by harryzhu on 2022/7/24
 */
@Repository
public interface UserMapper {

    @Select({"select id, name, phoneNumber, idNumber",
            "from [User]",
            "where id=#{id,jdbcType=INTEGER}"})
    UserEntity selectById(Integer id);

    @Select({"select id, name, phoneNumber, idNumber",
            "from [User]",
            "where idNumber=#{idNumber,jdbcType=VARCHAR}"})
    UserEntity selectByIdNumber(String idNumber);
}
