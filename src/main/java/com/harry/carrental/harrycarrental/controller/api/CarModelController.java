package com.harry.carrental.harrycarrental.controller.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.harry.carrental.harrycarrental.entity.CarModelEntity;
import com.harry.carrental.harrycarrental.entity.OrderEntity;
import com.harry.carrental.harrycarrental.mapper.CarModelMapper;
import com.harry.carrental.harrycarrental.mapper.OrderMapper;
import com.harry.carrental.harrycarrental.model.CommonRespModel;
import com.harry.carrental.harrycarrental.vo.CarModelVO;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by harryzhu on 2022/7/24
 */
@Slf4j
@RestController
public class CarModelController {

    @Resource
    private CarModelMapper carModelMapper;

    @Resource
    private OrderMapper orderMapper;

    @GetMapping("/carModels")
    public CommonRespModel<List<CarModelVO>> carModels(@RequestParam String startDate, @RequestParam String endDate) {
        List<CarModelEntity> carModelEntities = carModelMapper.selectCarModels();
        if (!CollectionUtils.isEmpty(carModelEntities)) {
            for (CarModelEntity carModelEntity : carModelEntities) {
                List<OrderEntity> orderEntities = orderMapper.selectByCarModelIdAndDate(carModelEntity.getId(),
                        startDate, endDate);
                Set<Integer> existOrderCarIds = Sets.newHashSet();
                if (!CollectionUtils.isEmpty(orderEntities)) {
                    for (OrderEntity orderEntity : orderEntities) {
                        existOrderCarIds.add(orderEntity.getCarId());
                    }
                }
                carModelEntity.setCarCount(carModelEntity.getCarCount() - existOrderCarIds.size());
            }
        }
        List<CarModelVO> carModels = convertEntities2VOs(carModelEntities);
        return new CommonRespModel(0, null, carModels);
    }

    @GetMapping("/carModels/{carModelId}")
    public CommonRespModel<CarModelVO> selectById(@PathVariable String carModelId, @RequestParam String startDate,
            @RequestParam String endDate) {
        CarModelEntity carModelEntity = carModelMapper.selectById(Integer.valueOf(carModelId));
        if (carModelEntity == null) {
            String errMsg = String.format("Car model does not exist. carModelId:%s", carModelId);
            log.error(errMsg);
            return new CommonRespModel(0, errMsg, null);
        }
        List<OrderEntity> orderEntities = orderMapper.selectByCarModelIdAndDate(carModelEntity.getId(),
                startDate, endDate);
        Set<Integer> existOrderCarIds = Sets.newHashSet();
        if (!CollectionUtils.isEmpty(orderEntities)) {
            for (OrderEntity orderEntity : orderEntities) {
                existOrderCarIds.add(orderEntity.getCarId());
            }
        }
        carModelEntity.setCarCount(carModelEntity.getCarCount() - existOrderCarIds.size());
        return new CommonRespModel(0, null, convertEntity2VO(carModelEntity));
    }

    private List<CarModelVO> convertEntities2VOs(List<CarModelEntity> carModelEntities) {
        List<CarModelVO> result = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(carModelEntities)) {
            for (CarModelEntity entity : carModelEntities) {
                result.add(convertEntity2VO(entity));
            }
        }
        return result;
    }

    private CarModelVO convertEntity2VO(CarModelEntity entity) {
        CarModelVO carModelVO = new CarModelVO();
        carModelVO.setId(entity.getId());
        carModelVO.setName(entity.getName());
        carModelVO.setDescription(entity.getDescription());
        carModelVO.setCarCount(entity.getCarCount());
        carModelVO.setBrand(entity.getName().substring(0, entity.getName().indexOf(" ")));
        return carModelVO;
    }
}
