package com.harry.carrental.harrycarrental.controller.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.harry.carrental.harrycarrental.constant.CommonConstant;
import com.harry.carrental.harrycarrental.constant.OrderActionEunm;
import com.harry.carrental.harrycarrental.constant.OrderStatusEnum;
import com.harry.carrental.harrycarrental.entity.CarEntity;
import com.harry.carrental.harrycarrental.entity.CarModelEntity;
import com.harry.carrental.harrycarrental.entity.OrderEntity;
import com.harry.carrental.harrycarrental.entity.PriceEntity;
import com.harry.carrental.harrycarrental.entity.UserEntity;
import com.harry.carrental.harrycarrental.exception.ExceptionConstant;
import com.harry.carrental.harrycarrental.mapper.LockMapper;
import com.harry.carrental.harrycarrental.mapper.CarMapper;
import com.harry.carrental.harrycarrental.mapper.CarModelMapper;
import com.harry.carrental.harrycarrental.mapper.OrderMapper;
import com.harry.carrental.harrycarrental.mapper.PriceMapper;
import com.harry.carrental.harrycarrental.mapper.UserMapper;
import com.harry.carrental.harrycarrental.model.CommonRespModel;
import com.harry.carrental.harrycarrental.util.IDCardUtil;
import com.harry.carrental.harrycarrental.util.ObjectMapperUtils;
import com.harry.carrental.harrycarrental.util.PhoneUtil;
import com.harry.carrental.harrycarrental.vo.ActionRequestVO;
import com.harry.carrental.harrycarrental.vo.GetDepositRequestVO;
import com.harry.carrental.harrycarrental.vo.GetDepositResponseVO;
import com.harry.carrental.harrycarrental.vo.OrderVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by harryzhu on 2022/7/24
 */
@Slf4j
@RestController
public class OrderController {

    @Value("${deposit.days:5}")
    private String DEPOSIT_DAYS;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CarModelMapper carModelMapper;

    @Resource
    private PriceMapper priceMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CarMapper carMapper;

    @Resource
    private LockMapper lockMapper;

    @GetMapping("/orders/idNumbers/{idNumber}")
    public CommonRespModel getOrdersByIdNumber(@PathVariable String idNumber) {
        if (IDCardUtil.idCardValidate(idNumber)) {
            String errMsg = String.format("Invalid idNumber. idNumber:%s", idNumber);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        UserEntity userEntity = userMapper.selectByIdNumber(idNumber);
        if (userEntity == null) {
            String errMsg = String.format("User does not exist. idNumber:%s", idNumber);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        List<OrderEntity> orderEntities = orderMapper.selectByUserId(userEntity.getId());
        Set<Integer> carModelIds = Sets.newHashSet();
        if (!CollectionUtils.isEmpty(orderEntities)) {
            for (OrderEntity orderEntity : orderEntities) {
                carModelIds.add(orderEntity.getCarModelId());
            }
        }
        List<OrderVO> result = Lists.newArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!CollectionUtils.isEmpty(carModelIds)) {
            List<CarModelEntity> carModelEntities = carModelMapper.selectByIds(carModelIds);
            Map<Integer, String> carModelNameMap = Maps.newHashMap();
            for (CarModelEntity carModelEntity : carModelEntities) {
                carModelNameMap.put(carModelEntity.getId(), carModelEntity.getName());
            }
            for (OrderEntity orderEntity : orderEntities) {
                OrderVO orderVO = ObjectMapperUtils.convert(orderEntity, OrderVO.class);
                orderVO.setCarModelName(carModelNameMap.get(orderVO.getCarModelId()));
                orderVO.setUsername(userEntity.getName());
                orderVO.setIdNumber(userEntity.getIdNumber());
                orderVO.setPhoneNumber(userEntity.getPhoneNumber());
                orderVO.setStartDate(sdf.format(orderEntity.getStartDate()));
                orderVO.setEndDate(sdf.format(orderEntity.getEndDate()));
                int lease = dateDifferent(orderEntity.getStartDate(), orderEntity.getEndDate());
                orderVO.setLease(lease);
                result.add(orderVO);
            }
        }
        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, result);
    }

    @PostMapping("/orders/actions")
    public CommonRespModel orderActions(@RequestBody ActionRequestVO actionRequestVO) {
        if (OrderActionEunm.CREATE_ORDER_CHECK.name().equals(actionRequestVO.getActionId())) {
            return createOrderCheck(actionRequestVO.getData());
        }
        else if (OrderActionEunm.PAY_DEPOSIT.name().equals(actionRequestVO.getActionId())) {
            return payDeposit(actionRequestVO.getData());
        }
        else if (OrderActionEunm.DELIVER_CAR.name().equals(actionRequestVO.getActionId())) {
            return deliverCar(actionRequestVO.getData());
        }
        return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, "Invalid action id.", null);
    }

    @PostMapping("/orders")
    public CommonRespModel createOrder(@RequestBody OrderVO orderVO) {
        OrderVO checkOrderVO = ObjectMapperUtils.convert(orderVO, OrderVO.class);
        CommonRespModel checkResult = createOrderCheck(checkOrderVO);
        if (checkResult.getStatus() != ExceptionConstant.COMMON_SUCCESS_STATUS) {
            return checkResult;
        }
        orderVO = (OrderVO)checkResult.getData();

        // 校验租期天数
        if (orderVO.getLease() == null || !orderVO.getLease().equals(checkOrderVO.getLease())) {
            String errMsg = String.format("Invalid lease. lease:%s", orderVO.getLease());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 校验押金
        if (orderVO.getDeposit() == null || !orderVO.getDeposit().equals(checkOrderVO.getDeposit())) {
            String errMsg = String.format("Invalid deposit. deposit:%s", orderVO.getDeposit());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 查询包含请求时间段的相同车型已有订单
        List<OrderEntity> orderEntities = orderMapper.selectByCarModelIdAndDate(orderVO.getCarModelId(),
                orderVO.getStartDate(), orderVO.getEndDate());
        Set<Integer> existOrderCarIds = Sets.newHashSet();
        if (!CollectionUtils.isEmpty(orderEntities)) {
            for (OrderEntity orderEntity : orderEntities) {
                existOrderCarIds.add(orderEntity.getCarId());
            }
        }

        // 锁定车源
        List<CarEntity> carEntities = carMapper.selectByCarModelId(orderVO.getCarModelId());
        if (carEntities == null || carEntities.size() == 0) {
            String errMsg = String.format("Insufficient cars. carModelName:%s", orderVO.getCarModelName());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        Integer carId = null;
        String lockKey = null;
        for (CarEntity carEntity : carEntities) {
            // 已有时间冲突订单的车源无法锁定
            if (existOrderCarIds.contains(carEntity.getId())) {
                log.info("Exist order with this car. carId:{}", carEntity.getId());
                continue;
            }
            if (getLock(CommonConstant.CREATE_ORDER_CAR_BLOCK_PREFIX + carEntity.getId(), orderVO.getIdNumber())) {
                lockKey = CommonConstant.CREATE_ORDER_CAR_BLOCK_PREFIX + carEntity.getId();
                carId = carEntity.getId();
                break;
            }
        }
        if (carId == null) {
            String errMsg = String.format("Insufficient cars. carModelName:%s", orderVO.getCarModelName());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        else {
            try {
                // 创建订单
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setCarId(carId);
                orderEntity.setCarModelId(orderVO.getCarModelId());
                orderEntity.setUserId(orderVO.getUserId());
                try {
                    orderEntity.setStartDate(sdf.parse(orderVO.getStartDate()));
                    orderEntity.setEndDate(sdf.parse(orderVO.getEndDate()));
                }
                catch (ParseException e) {
                    String errMsg = String.format("Invalid startDate or endDate. startDate:%s, endDate:%s",
                            orderVO.getStartDate(), orderVO.getEndDate());
                    log.error(errMsg);
                    return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
                }
                orderEntity.setDeposit(orderVO.getDeposit());
                orderEntity.setEnabled(CommonConstant.TRUE_STRING);
                orderEntity.setCreateTime(new Date());
                orderEntity.setStatus(OrderStatusEnum.UNPAID.name());
                orderMapper.createOrder(orderEntity);
                orderVO.setCarId(carId);
                return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, orderVO);
            }
            finally {
                if (lockKey != null) {
                    releaseLock(lockKey);
                }
            }
        }
    }

    @GetMapping("/orders/{orderId}")
    public CommonRespModel getOrdersById(@PathVariable Integer orderId) {
        OrderEntity orderEntity = orderMapper.selectById(orderId);
        if (orderEntity == null) {
            String errMsg = String.format("Order does not exist. orderId:%s", orderId);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        UserEntity userEntity = userMapper.selectById(orderEntity.getUserId());
        CarModelEntity carModelEntity = carModelMapper.selectById(orderEntity.getCarModelId());
        OrderVO orderVO = ObjectMapperUtils.convert(orderEntity, OrderVO.class);
        orderVO.setUsername(userEntity.getName());
        orderVO.setIdNumber(userEntity.getIdNumber());
        orderVO.setPhoneNumber(userEntity.getPhoneNumber());
        orderVO.setCarModelName(carModelEntity.getName());
        orderVO.setLease(dateDifferent(orderEntity.getStartDate(), orderEntity.getEndDate()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        orderVO.setStartDate(sdf.format(orderEntity.getStartDate()));
        orderVO.setEndDate(sdf.format(orderEntity.getEndDate()));
        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, orderVO);
    }

    private CommonRespModel createOrderCheck(Object input) {
        OrderVO orderVO = ObjectMapperUtils.convert(input, OrderVO.class);

        // 校验手机号
        if (!PhoneUtil.checkRawPhoneNumber(orderVO.getPhoneNumber())) {
            String errMsg = String.format("Invalid phoneNumber. phoneNumber:%s", orderVO.getPhoneNumber());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 校验身份证号
        if (IDCardUtil.idCardValidate(orderVO.getIdNumber())) {
            String errMsg = String.format("Invalid idNumber. idNumber:%s", orderVO.getIdNumber());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 校验用户名长度
        if (orderVO.getUsername() == null || orderVO.getUsername().length() > 255) {
            String errMsg = String.format("Invalid length of username. username:%s", orderVO.getUsername());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 校验用户
        boolean userValid = false;
        if (orderVO.getIdNumber() != null) {
            UserEntity userEntity = userMapper.selectByIdNumber(orderVO.getIdNumber());
            if (userEntity != null) {
                // 校验用户姓名
                if (!userEntity.getName().equals(orderVO.getUsername())) {
                    String errMsg = String.format("Invalid username. username:%s", orderVO.getUsername());
                    log.error(errMsg);
                    return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
                }

                // 校验用户手机号
                if (!userEntity.getPhoneNumber().equals(orderVO.getPhoneNumber())) {
                    String errMsg = String.format("PhoneNumber does not match idNumber. phoneNumber:%s, idNumber:%s",
                            orderVO.getPhoneNumber(), orderVO.getIdNumber());
                    log.error(errMsg);
                    return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
                }
                userValid = true;
                orderVO.setUserId(userEntity.getId());
            }
        }
        if (!userValid) {
            String errMsg = String.format("User does not exist. idNumber:%s", orderVO.getIdNumber());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 校验车辆型号
        boolean carModelValid = false;
        if (orderVO.getCarModelId() != null) {
            CarModelEntity carModelEntity = carModelMapper.selectById(orderVO.getCarModelId());
            if (carModelEntity != null) {
                carModelValid = true;
                orderVO.setCarModelName(carModelEntity.getName());
            }
        }
        if (!carModelValid) {
            String errMsg = String.format("Car model does not exist. id:%s", orderVO.getCarModelId());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 校验开始日期和结束日期
        boolean startDateValid = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        Date nowDate = new Date();;
        if (orderVO.getStartDate() != null && orderVO.getEndDate() != null) {
            try {
                startDate = sdf.parse(orderVO.getStartDate());
                endDate = sdf.parse(orderVO.getEndDate());
            } catch (ParseException e) {
                String errMsg = String.format("StartDate or endDate format parse failed. startDate:%s, endDate:%s",
                        orderVO.getStartDate(), orderVO.getEndDate());
                log.error(errMsg);
            }
        }
        if (startDate == null || endDate == null) {
            String errMsg = String.format("StartDate or endDate format parse failed. startDate:%s, endDate:%s",
                    orderVO.getStartDate(), orderVO.getEndDate());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 开始日期不能晚于当天
        if (nowDate.getTime() - startDate.getTime() > 1000 * 3600 * 24) {
            String errMsg = String.format("Invalid startDate. startDate:%s", orderVO.getStartDate());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 结束时间不能晚于开始时间
        if (startDate.getTime() <= endDate.getTime()) {
            startDateValid = true;
        }
        else {
            String errMsg = String.format("StartDate should before endDate. startDate:%s, endDate:%s",
                    orderVO.getStartDate(), orderVO.getEndDate());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        if (!startDateValid) {
            String errMsg = String.format("Invalid startDate or endDate. startDate:%s, endDate:%s",
                    orderVO.getStartDate(), orderVO.getEndDate());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 校验价格是否存在
        PriceEntity priceEntity = priceMapper.selectByCarModelId(orderVO.getCarModelId());
        if (priceEntity == null) {
            String errMsg = String.format("Price does not exist carModelId:%s", orderVO.getCarModelId());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 计算押金
        int lease = dateDifferent(startDate, endDate);
        int depositDays = lease + Integer.valueOf(DEPOSIT_DAYS);
        int deposit = depositDays  * priceEntity.getPrice();
        orderVO.setDeposit(deposit);
        orderVO.setLease(lease);

        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, orderVO);
    }

    private CommonRespModel payDeposit(Object input) {
        // TODO 第三方支付
        Integer orderId = (Integer) input;
        orderMapper.updateStatus(orderId, OrderStatusEnum.PAID.name());
        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, "Payment successful.");
    }

    private CommonRespModel deliverCar(Object input) {
        // 判断是否移交押金
        Integer orderId = (Integer) input;
        OrderEntity orderEntity = orderMapper.selectById(orderId);
        if (!OrderStatusEnum.PAID.name().equals(orderEntity.getStatus())) {
            String errMsg = String.format("Deposit does not been paid or order status invalid. orderId:%s", orderId);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 线下交车
        orderMapper.updateStatus(orderId, OrderStatusEnum.CAR_DELIVERED.name());
        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, "Deliver car successful.");
    }

    private CommonRespModel checkDepositParam(GetDepositRequestVO requestVO) {
        // 校验用户
        boolean userValid = false;
        if (requestVO.getIdNumber() != null) {
            UserEntity userEntity = userMapper.selectByIdNumber(requestVO.getIdNumber());
            if (userEntity != null) {
                userValid = true;
            }
        }
        if (!userValid) {
            String errMsg = String.format("user does not exist. idNumber:%s", requestVO.getIdNumber());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 校验车辆型号
        boolean carModelValid = false;
        if (requestVO.getCarModelId() != null) {
            CarModelEntity carModelEntity = carModelMapper.selectById(requestVO.getCarModelId());
            if (carModelEntity != null) {
                carModelValid = true;
            }
        }
        if (!carModelValid) {
            String errMsg = String.format("Car model does not exist. id:%s", requestVO.getCarModelId());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 校验开始时间和结束时间
        boolean startDateValid = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (requestVO.getStartDate() != null && requestVO.getEndDate() != null) {
            try {
                Date startDate = sdf.parse(requestVO.getStartDate());
                Date endDate = sdf.parse(requestVO.getEndDate());
                if (startDate.getTime() <= endDate.getTime()) {
                    startDateValid = true;
                }
            } catch (ParseException e) {
                String errMsg = String.format("StartDate or endDate format parse failed. startDate:%s, endDate:%s",
                        requestVO.getStartDate(), requestVO.getEndDate());
                log.error(errMsg);
            }
        }
        if (!startDateValid) {
            String errMsg = String.format("Invalid startDate or endDate. startDate:%s, endDate:%s",
                    requestVO.getStartDate(), requestVO.getEndDate());
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // 校验价格是否存在
        PriceEntity priceEntity = priceMapper.selectByCarModelId(requestVO.getCarModelId());
        if (priceEntity == null) {
            String errMsg = String.format("Price does not exist carModelId:%s", requestVO.getCarModelId());
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, null);
    }

    private int dateDifferent(Date date1, Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)) + 1;
        return days;
    }

    private boolean getLock(String key, String value) {
        try {
            lockMapper.insert(key, value);
        }
        catch (Exception e) {
            log.info("Get lock failed.");
            return false;
        }
        return true;
    }

    private void releaseLock(String key) {
        lockMapper.delete(key);
    }
}
