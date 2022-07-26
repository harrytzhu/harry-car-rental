package com.harry.carrental.harrycarrental.mapper;

import com.harry.carrental.harrycarrental.entity.CarModelEntity;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Created by harryzhu on 2022/7/24
 */
@Repository
public interface CarModelMapper {

    @Select({"select a.id, a.name, a.description, sum(case when b.id is null then 0 else 1 end) as carCount",
            "from CarModel a left outer join Car b on a.id=b.carModelId",
            "group by a.id, a.name, a.description"})
    List<CarModelEntity> selectCarModels();

    @Select({"select a.id, a.name, a.description, sum(case when b.id is null then 0 else 1 end) as carCount",
            "from CarModel a left outer join Car b on a.id=b.carModelId",
            "where a.id=#{id,jdbcType=INTEGER}",
            "group by a.id, a.name, a.description"})
    CarModelEntity selectById(Integer id);

    @Select({"<script>",
            "select a.id, a.name, a.description, sum(case when b.id is null then 0 else 1 end) as carCount",
            "from CarModel a left outer join Car b on a.id=b.carModelId",
            "where a.id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id,jdbcType=INTEGER}",
            "</foreach>",
            "group by a.id, a.name, a.description",
            "</script>"})
    List<CarModelEntity> selectByIds(Collection<Integer> ids);
}
