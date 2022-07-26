package com.harry.carrental.harrycarrental.controller.view;

import com.harry.carrental.harrycarrental.constant.OrderActionEunm;
import com.harry.carrental.harrycarrental.controller.api.CarModelController;
import com.harry.carrental.harrycarrental.controller.api.OrderController;
import com.harry.carrental.harrycarrental.exception.ExceptionConstant;
import com.harry.carrental.harrycarrental.model.CommonRespModel;
import com.harry.carrental.harrycarrental.vo.ActionRequestVO;
import com.harry.carrental.harrycarrental.vo.OrderVO;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by harryzhu on 2022/7/25
 */
@Controller
public class OrderViewController {

    @Resource
    private CarModelController carModelController;

    @Resource
    private OrderController orderController;

    @GetMapping("/view/selectDatePage")
    public String selectDatePage() {
        return "selectDate";
    }

    @GetMapping("/view/selectCarPage")
    public String selectCarPage(Model model, @RequestParam String startDate, @RequestParam String endDate) {
        CommonRespModel respModel = carModelController.carModels(startDate, endDate);
        if (ExceptionConstant.COMMON_ERROR_STATUS == respModel.getStatus()) {
            model.addAttribute("errorMessage", respModel.getMsg());
            return "commonError";
        }
        model.addAttribute("carModels", respModel.getData());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "selectCar";
    }

    @GetMapping("/view/createOrderPage")
    public String createOrderPage(Model model, @RequestParam String carModelId, @RequestParam String startDate,
            @RequestParam String endDate) {
        CommonRespModel respModel = carModelController.selectById(carModelId, startDate, endDate);
        if (ExceptionConstant.COMMON_ERROR_STATUS == respModel.getStatus()) {
            model.addAttribute("errorMessage", respModel.getMsg());
            return "commonError";
        }
        model.addAttribute("carModel", respModel.getData());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "createOrder";
    }

    @PostMapping("/view/confirmOrderPage")
    public String confirmOrderPage(Model model, @RequestParam String username, @RequestParam String phoneNumber,
            @RequestParam String idNumber, @RequestParam String startDate, @RequestParam String endDate,
            @RequestParam String carModelId) {
        ActionRequestVO actionRequestVO = new ActionRequestVO();
        actionRequestVO.setActionId(OrderActionEunm.CREATE_ORDER_CHECK.name());
        OrderVO orderVO = new OrderVO();
        orderVO.setUsername(username);
        orderVO.setPhoneNumber(phoneNumber);
        orderVO.setIdNumber(idNumber);
        orderVO.setStartDate(startDate);
        orderVO.setEndDate(endDate);
        orderVO.setCarModelId(Integer.valueOf(carModelId));
        actionRequestVO.setData(orderVO);
        CommonRespModel respModel = orderController.orderActions(actionRequestVO);
        if (ExceptionConstant.COMMON_ERROR_STATUS == respModel.getStatus()) {
            model.addAttribute("errorMessage", respModel.getMsg());
            return "commonError";
        }
        model.addAttribute("order", respModel.getData());
        return "confirmOrder";
    }

    @PostMapping("/view/submitOrderPage")
    public String submitOrderPage(Model model, @RequestParam String idNumber, @RequestParam String carModelId,
            @RequestParam String startDate, @RequestParam String endDate, @RequestParam String lease,
            @RequestParam String deposit, @RequestParam String username, @RequestParam String phoneNumber) {
        OrderVO orderVO = new OrderVO();
        orderVO.setUsername(username);
        orderVO.setPhoneNumber(phoneNumber);
        orderVO.setIdNumber(idNumber);
        orderVO.setStartDate(startDate);
        orderVO.setEndDate(endDate);
        orderVO.setLease(Integer.valueOf(lease));
        orderVO.setDeposit(Integer.valueOf(deposit));
        orderVO.setCarModelId(Integer.valueOf(carModelId));
        CommonRespModel respModel = orderController.createOrder(orderVO);
        if (ExceptionConstant.COMMON_ERROR_STATUS == respModel.getStatus()) {
            model.addAttribute("errorMessage", respModel.getMsg());
            return "commonError";
        }
        model.addAttribute("order", respModel.getData());
        return "submitOrderResult";
    }

    @GetMapping("/view/selectOrderInputPage")
    public String selectOrderInputPage() {
        return "selectOrderInput";
    }

    @GetMapping("/view/selectOrderResultPage")
    public String selectOrderResultPage(Model model, @RequestParam String idNumber) {
        CommonRespModel respModel = orderController.getOrdersByIdNumber(idNumber);
        if (ExceptionConstant.COMMON_ERROR_STATUS == respModel.getStatus()) {
            model.addAttribute("errorMessage", respModel.getMsg());
            return "commonError";
        }
        model.addAttribute("orders", respModel.getData());
        return "selectOrderResult";
    }

    @GetMapping("/view/payDepositPage")
    public String payDepositPage(Model model, @RequestParam String orderId) {
        ActionRequestVO actionRequestVO = new ActionRequestVO();
        actionRequestVO.setActionId(OrderActionEunm.PAY_DEPOSIT.name());
        actionRequestVO.setData(Integer.valueOf(orderId));
        CommonRespModel respModel = orderController.orderActions(actionRequestVO);
        model.addAttribute("payResult", respModel.getData());
        return "payResult";
    }
}
