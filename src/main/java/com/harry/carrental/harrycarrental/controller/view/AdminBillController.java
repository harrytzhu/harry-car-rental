package com.harry.carrental.harrycarrental.controller.view;

import com.harry.carrental.harrycarrental.controller.api.BillController;
import com.harry.carrental.harrycarrental.controller.api.OrderController;
import com.harry.carrental.harrycarrental.exception.ExceptionConstant;
import com.harry.carrental.harrycarrental.model.CommonRespModel;
import com.harry.carrental.harrycarrental.vo.BillVO;
import com.harry.carrental.harrycarrental.vo.OrderVO;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by harryzhu on 2022/7/26
 */
@Controller
public class AdminBillController {

    @Resource
    private OrderController orderController;

    @Resource
    private BillController billController;

    @GetMapping("/admin/view/createBillPage")
    public String createBillPage(Model model, @RequestParam Integer orderId) {
        CommonRespModel orderRespModel = orderController.getOrdersById(orderId);
        if (ExceptionConstant.COMMON_ERROR_STATUS == orderRespModel.getStatus()) {
            model.addAttribute("errorMessage", orderRespModel.getMsg());
            return "commonError";
        }
        CommonRespModel billRespModel = billController.getTemporaryBill(orderId);
        if (ExceptionConstant.COMMON_ERROR_STATUS == billRespModel.getStatus()) {
            model.addAttribute("errorMessage", billRespModel.getMsg());
            return "commonError";
        }
        OrderVO orderVO = (OrderVO)orderRespModel.getData();
        BillVO billVO = (BillVO) billRespModel.getData();
        orderVO.setLease(orderVO.getLease() + billVO.getExpireDays());
        model.addAttribute("order", orderVO);
        model.addAttribute("bill", billVO);
        return "adminCreateBill";
    }

    @PostMapping("/admin/view/confirmBillPage")
    public String confirmBillPage(Model model, @RequestParam Integer orderId, @RequestParam Integer carDamageCost,
            @RequestParam Integer fines) {
        CommonRespModel orderRespModel = orderController.getOrdersById(orderId);
        if (ExceptionConstant.COMMON_ERROR_STATUS == orderRespModel.getStatus()) {
            model.addAttribute("errorMessage", orderRespModel.getMsg());
            return "commonError";
        }
        CommonRespModel billRespModel = billController.getFormalBill(orderId, carDamageCost, fines);
        if (ExceptionConstant.COMMON_ERROR_STATUS == billRespModel.getStatus()) {
            model.addAttribute("errorMessage", billRespModel.getMsg());
            return "commonError";
        }
        OrderVO orderVO = (OrderVO)orderRespModel.getData();
        BillVO billVO = (BillVO) billRespModel.getData();
        model.addAttribute("order", orderVO);
        model.addAttribute("bill", billVO);
        return "adminConfirmBill";
    }

    @PostMapping("/admin/view/submitBillPage")
    public String submitBillPage(Model model, @RequestParam Integer orderId, @RequestParam Integer carDamageCost,
            @RequestParam Integer fines) {
        CommonRespModel billRespModel = billController.getFormalBill(orderId, carDamageCost, fines);
        if (ExceptionConstant.COMMON_ERROR_STATUS == billRespModel.getStatus()) {
            model.addAttribute("errorMessage", billRespModel.getMsg());
            return "commonError";
        }
        BillVO billVO = (BillVO) billRespModel.getData();
        CommonRespModel createBillRespModel = billController.createBill(billVO);
        if (ExceptionConstant.COMMON_ERROR_STATUS == createBillRespModel.getStatus()) {
            model.addAttribute("errorMessage", createBillRespModel.getMsg());
            return "commonError";
        }
        CommonRespModel orderRespModel = orderController.getOrdersById(orderId);
        if (ExceptionConstant.COMMON_ERROR_STATUS == orderRespModel.getStatus()) {
            model.addAttribute("errorMessage", orderRespModel.getMsg());
            return "commonError";
        }
        OrderVO orderVO = (OrderVO)orderRespModel.getData();
        model.addAttribute("order", orderVO);
        model.addAttribute("bill", billVO);
        return "adminSubmitBillResult";
    }
}
