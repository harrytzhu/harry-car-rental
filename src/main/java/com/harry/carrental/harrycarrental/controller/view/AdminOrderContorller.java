package com.harry.carrental.harrycarrental.controller.view;

import com.harry.carrental.harrycarrental.constant.OrderActionEunm;
import com.harry.carrental.harrycarrental.controller.api.BillController;
import com.harry.carrental.harrycarrental.controller.api.OrderController;
import com.harry.carrental.harrycarrental.exception.ExceptionConstant;
import com.harry.carrental.harrycarrental.model.CommonRespModel;
import com.harry.carrental.harrycarrental.vo.ActionRequestVO;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by harryzhu on 2022/7/25
 */
@Slf4j
@Controller
public class AdminOrderContorller {

    @Resource
    private OrderController orderController;

    @GetMapping("/admin/view/selectOrderInputPage")
    public String selectOrderInputPage() {
        return "adminSelectOrderInput";
    }

    @GetMapping("/admin/view/selectOrderResultPage")
    public String selectOrderResultPage(Model model, @RequestParam String idNumber) {
        CommonRespModel respModel = orderController.getOrdersByIdNumber(idNumber);
        if (ExceptionConstant.COMMON_ERROR_STATUS == respModel.getStatus()) {
            model.addAttribute("errorMessage", respModel.getMsg());
            return "commonError";
        }
        model.addAttribute("orders", respModel.getData());
        return "adminSelectOrderResult";
    }

    @GetMapping("/admin/view/deliverCarPage")
    public String payDepositPage(Model model, @RequestParam String orderId) {
        ActionRequestVO actionRequestVO = new ActionRequestVO();
        actionRequestVO.setActionId(OrderActionEunm.DELIVER_CAR.name());
        actionRequestVO.setData(Integer.valueOf(orderId));
        CommonRespModel respModel = orderController.orderActions(actionRequestVO);
        model.addAttribute("deliverResult", respModel.getData());
        return "deliverResult";
    }

    @GetMapping("/admin/view/returnCarPage")
    public String returnCarPage(Model model, @RequestParam String orderId) {
        ActionRequestVO actionRequestVO = new ActionRequestVO();
        actionRequestVO.setActionId(OrderActionEunm.RETURN_CAR.name());
        actionRequestVO.setData(Integer.valueOf(orderId));
        CommonRespModel respModel = orderController.orderActions(actionRequestVO);
        model.addAttribute("deliverResult", respModel.getData());
        return "deliverResult";
    }
}
