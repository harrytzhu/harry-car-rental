package com.harry.carrental.harrycarrental.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by harryzhu on 2022/7/25
 */
@Repository
public interface LockMapper {

    @Insert({"insert into Lock", "values", "(#{key,jdbcType=VARCHAR},#{value,jdbcType=VARCHAR})"})
    void insert(@Param("key") String key, @Param("value") String value);

    @Delete({"delete from Lock where [key]=#{key,jdbcType=VARCHAR}"})
    int delete(String key);
}
