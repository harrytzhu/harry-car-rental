package com.harry.carrental.harrycarrental.entity;

import java.util.Date;
import lombok.Data;

/**
 * Created by harryzhu on 2022/7/26
 */
@Data
public class BillEntity {

    private Integer id;

    private Integer orderId;

    private Integer userId;

    private Integer carModelId;

    private Date actualReturnDate;

    private Integer expireDays;

    private Integer rentCost;

    private Integer carDamageCost;

    private Integer fines;

    private Integer totalCost;

    private String isPaid;

    private Integer addAmount;

    private Integer returnAmount;
}
