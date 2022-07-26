package com.harry.carrental.harrycarrental.vo;

import lombok.Data;

/**
 * Created by harryzhu on 2022/7/24
 */
@Data
public class CarVO {

    private String id;
    
    private String modelName;

    private String carDescription;

    private String carModelDescription;

    private String isReserved;

    private String enabled;
}
