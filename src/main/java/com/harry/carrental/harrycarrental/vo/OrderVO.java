package com.harry.carrental.harrycarrental.vo;

import lombok.Data;

/**
 * Created by harryzhu on 2022/7/25
 */
@Data
public class OrderVO {

    private Integer id;

    private Integer userId;

    private String username;

    private String idNumber;

    private String phoneNumber;

    private Integer carModelId;

    private String carModelName;

    private Integer carId;

    private Integer lease;

    private Integer deposit;

    private String startDate;

    private String endDate;

    private String status;
}
