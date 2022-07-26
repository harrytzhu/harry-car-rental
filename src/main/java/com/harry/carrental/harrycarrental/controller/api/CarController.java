package com.harry.carrental.harrycarrental.controller.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.harry.carrental.harrycarrental.model.CommonRespModel;
import com.harry.carrental.harrycarrental.vo.CarVO;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by harryzhu on 2022/7/24
 */
@RestController
public class CarController {

    @GetMapping("/cars")
    public CommonRespModel cars() {
        List<CarVO> cars = Lists.newArrayList();
        CarVO carVO = new CarVO();
        carVO.setModelName("Toyota Camry");
        cars.add(carVO);
        Map<String, Object> dataResult = Maps.newHashMap();
        dataResult.put("cars", cars);
        return new CommonRespModel(0, null, dataResult);
    }
}
