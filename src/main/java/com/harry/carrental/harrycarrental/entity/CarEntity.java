package com.harry.carrental.harrycarrental.entity;

import lombok.Data;

/**
 * Created by harryzhu on 2022/7/24
 */
@Data
public class CarEntity {

    private Integer id;

    private Integer carModelId;

    private String description;

    private String isReserved;

    private String enabled;
}
