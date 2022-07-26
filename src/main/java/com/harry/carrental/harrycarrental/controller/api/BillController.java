package com.harry.carrental.harrycarrental.controller.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.harry.carrental.harrycarrental.constant.BillActionEunm;
import com.harry.carrental.harrycarrental.constant.CommonConstant;
import com.harry.carrental.harrycarrental.entity.BillEntity;
import com.harry.carrental.harrycarrental.entity.CarModelEntity;
import com.harry.carrental.harrycarrental.entity.OrderEntity;
import com.harry.carrental.harrycarrental.entity.PriceEntity;
import com.harry.carrental.harrycarrental.entity.UserEntity;
import com.harry.carrental.harrycarrental.exception.ExceptionConstant;
import com.harry.carrental.harrycarrental.mapper.BillMapper;
import com.harry.carrental.harrycarrental.mapper.CarModelMapper;
import com.harry.carrental.harrycarrental.mapper.OrderMapper;
import com.harry.carrental.harrycarrental.mapper.PriceMapper;
import com.harry.carrental.harrycarrental.mapper.UserMapper;
import com.harry.carrental.harrycarrental.model.CommonRespModel;
import com.harry.carrental.harrycarrental.util.IDCardUtil;
import com.harry.carrental.harrycarrental.util.ObjectMapperUtils;
import com.harry.carrental.harrycarrental.vo.ActionRequestVO;
import com.harry.carrental.harrycarrental.vo.BillVO;
import com.harry.carrental.harrycarrental.vo.OrderVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
public class BillController {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private PriceMapper priceMapper;

    @Resource
    private BillMapper billMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CarModelMapper carModelMapper;

    @GetMapping("/bills/temporaryBill/orders/{orderId}")
    public CommonRespModel getTemporaryBill(@PathVariable Integer orderId) {
        OrderEntity orderEntity = orderMapper.selectById(Integer.valueOf(orderId));
        if (orderEntity == null) {
            String errMsg = String.format("Order does not exist. orderId:%s", orderId);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = new Date();
        Date orderEndDate = orderEntity.getEndDate();
        BillVO billVO = new BillVO();
        billVO.setOrderId(orderId);
        billVO.setUserId(orderEntity.getUserId());
        billVO.setCarModelId(orderEntity.getCarModelId());
        billVO.setActualReturnDate(sdf.format(nowDate));

        PriceEntity priceEntity = priceMapper.selectByCarModelId(orderEntity.getCarModelId());
        if (priceEntity == null) {
            String errMsg = String.format("Price does not exist. carModelId:%s", orderEntity.getCarModelId());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        int expireDays = (int)(nowDate.getTime() - orderEndDate.getTime()) / (3600 * 1000 * 24);
        int orderLease = dateDifferent(orderEntity.getStartDate(), orderEntity.getEndDate());
        if (expireDays > 0) {
            billVO.setExpireDays(expireDays);
            Double rentCostDouble = orderLease * priceEntity.getPrice() + expireDays * priceEntity.getPrice() * 1.5;
            billVO.setRentCost(rentCostDouble.intValue());
        }
        else {
            billVO.setExpireDays(0);
            billVO.setRentCost(orderLease * priceEntity.getPrice());
        }

        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, billVO);
    }

    @GetMapping("/bills/formalBill/orders/{orderId}")
    public CommonRespModel getFormalBill(@PathVariable Integer orderId, @RequestParam Integer carDamageCost,
            @RequestParam Integer fines) {
        OrderEntity orderEntity = orderMapper.selectById(orderId);
        if (orderEntity == null) {
            String errMsg = String.format("Order does not exist. orderId:%s", orderId);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        CommonRespModel commonRespModel = getTemporaryBill(orderId);
        if (ExceptionConstant.COMMON_ERROR_STATUS == commonRespModel.getStatus()) {
            log.error(commonRespModel.getMsg());
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, commonRespModel.getMsg(), null);
        }
        BillVO billVO = (BillVO) commonRespModel.getData();
        billVO.setCarDamageCost(carDamageCost);
        billVO.setFines(fines);
        billVO.setTotalCost(billVO.getRentCost() + billVO.getCarDamageCost() + billVO.getFines());

        if (billVO.getTotalCost() > orderEntity.getDeposit()) {
            billVO.setAddAmount(billVO.getTotalCost() - orderEntity.getDeposit());
            billVO.setReturnAmount(0);
        }
        else {
            billVO.setAddAmount(0);
            billVO.setReturnAmount(orderEntity.getDeposit() - billVO.getTotalCost());
        }
        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, billVO);
    }

    @PostMapping("/bills")
    public CommonRespModel createBill(BillVO billVO) {
        BillEntity billEntity = billMapper.selectByOrderId(billVO.getOrderId());
        if (billEntity != null) {
            String errMsg = String.format("Bill already exist. billId:%s", billEntity.getId());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        billEntity = new BillEntity();
        billEntity.setOrderId(billVO.getOrderId());
        billEntity.setUserId(billVO.getUserId());
        try {
            billEntity.setActualReturnDate(sdf.parse(billVO.getActualReturnDate()));
        } catch (ParseException e) {
            String errMsg = String.format("actualReturnDate parse failed. actualReturnDate:%s",
                    billVO.getActualReturnDate());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        billEntity.setCarModelId(billVO.getCarModelId());
        billEntity.setExpireDays(billVO.getExpireDays());
        billEntity.setRentCost(billVO.getRentCost());
        billEntity.setCarDamageCost(billVO.getCarDamageCost());
        billEntity.setFines(billVO.getFines());
        billEntity.setTotalCost(billVO.getTotalCost());
        billEntity.setAddAmount(billVO.getAddAmount());
        billEntity.setReturnAmount(billVO.getReturnAmount());
        billEntity.setIsPaid(CommonConstant.FALSE_STRING);
        int insertResult = billMapper.insert(billEntity);
        if (insertResult > 0) {
            return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, billVO);
        }
        else {
            String errMsg = String.format("Bill insert failed. orderId:%s", billEntity.getOrderId());
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
    }

    @GetMapping("/bills/idNumbers/{idNumber}")
    public CommonRespModel getBillsByIdNumber(@PathVariable String idNumber) {
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

        List<BillEntity> billEntities = billMapper.selectByUserId(userEntity.getId());
        Set<Integer> carModelIds = Sets.newHashSet();
        if (!CollectionUtils.isEmpty(billEntities)) {
            for (BillEntity billEntity : billEntities) {
                carModelIds.add(billEntity.getCarModelId());
            }
        }
        List<BillVO> result = Lists.newArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!CollectionUtils.isEmpty(carModelIds)) {
            List<CarModelEntity> carModelEntities = carModelMapper.selectByIds(carModelIds);
            Map<Integer, String> carModelNameMap = Maps.newHashMap();
            for (CarModelEntity carModelEntity : carModelEntities) {
                carModelNameMap.put(carModelEntity.getId(), carModelEntity.getName());
            }
            for (BillEntity billEntity : billEntities) {
                BillVO billVO = ObjectMapperUtils.convert(billEntity, BillVO.class);
                billVO.setCarModelName(carModelNameMap.get(billVO.getCarModelId()));
                billVO.setUsername(userEntity.getName());
                billVO.setActualReturnDate(sdf.format(billEntity.getActualReturnDate()));
                result.add(billVO);
            }
        }
        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, result);
    }

    @PostMapping("/bills/actions")
    public CommonRespModel billActions(@RequestBody ActionRequestVO actionRequestVO) {
        if (BillActionEunm.PAY_BILL.name().equals(actionRequestVO.getActionId())) {
            return payBill(actionRequestVO.getData());
        }
        return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, "Invalid action id.", null);
    }

    @GetMapping("/bills/{billId}")
    public CommonRespModel getBillById(@PathVariable Integer billId) {
        BillEntity billEntity = billMapper.selectById(billId);
        if (billEntity == null) {
            String errMsg = String.format("Bill does not exist. billId:%s", billId);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        BillVO billVO = ObjectMapperUtils.convert(billEntity, BillVO.class);
        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, billVO);
    }

    private CommonRespModel payBill(Object input) {
        // TODO 第三方支付或退款
        Integer billId = (Integer) input;
        billMapper.setPaid(billId);
        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, "Payment successful.");
    }

    private int dateDifferent(Date date1, Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)) + 1;
        return days;
    }
}
