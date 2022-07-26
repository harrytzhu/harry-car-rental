package com.harry.carrental.harrycarrental.entity;

import java.util.Date;
import lombok.Data;

/**
 * Created by harryzhu on 2022/7/25
 */
@Data
public class OrderEntity {

    private Integer id;

    private Integer userId;

    private Integer carId;

    private Integer carModelId;

    private Date startDate;

    private Date endDate;

    private Integer deposit;

    private String status;

    private Date createTime;

    private String enabled;
}
