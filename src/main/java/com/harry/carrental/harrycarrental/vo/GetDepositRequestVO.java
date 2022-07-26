package com.harry.carrental.harrycarrental.vo;

import lombok.Data;

/**
 * Created by harryzhu on 2022/7/24
 */
@Data
public class GetDepositRequestVO {

    private Integer carModelId;

    private String startDate;

    private String endDate;

    private String idNumber;
}
