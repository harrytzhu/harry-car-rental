package com.harry.carrental.harrycarrental.vo;

import java.util.Date;
import lombok.Data;

/**
 * Created by harryzhu on 2022/7/26
 */
@Data
public class BillVO {

    private Integer id;

    private Integer orderId;

    private Integer userId;

    private String username;

    private Integer carModelId;

    private String carModelName;

    private String actualReturnDate;

    private Integer rentCost;

    private Integer carDamageCost;

    private Integer fines;

    private Integer totalCost;

    private String isPaid;

    private Integer expireDays;

    private Integer addAmount;

    private Integer returnAmount;
}
