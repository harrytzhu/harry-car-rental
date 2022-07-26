package com.harry.carrental.harrycarrental.controller.view;

import com.harry.carrental.harrycarrental.constant.BillActionEunm;
import com.harry.carrental.harrycarrental.constant.OrderActionEunm;
import com.harry.carrental.harrycarrental.controller.api.BillController;
import com.harry.carrental.harrycarrental.exception.ExceptionConstant;
import com.harry.carrental.harrycarrental.model.CommonRespModel;
import com.harry.carrental.harrycarrental.vo.ActionRequestVO;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by harryzhu on 2022/7/26
 */
@Controller
public class BillViewController {

    @Resource
    private BillController billController;

    @GetMapping("/view/selectBillInputPage")
    public String selectBillInputPage() {
        return "selectBillInput";
    }

    @GetMapping("/view/selectBillResultPage")
    public String selectOrderResultPage(Model model, @RequestParam String idNumber) {
        CommonRespModel respModel = billController.getBillsByIdNumber(idNumber);
        if (ExceptionConstant.COMMON_ERROR_STATUS == respModel.getStatus()) {
            model.addAttribute("errorMessage", respModel.getMsg());
            return "commonError";
        }
        model.addAttribute("bills", respModel.getData());
        return "selectBillResult";
    }

    @GetMapping("/view/payBillPage")
    public String payDepositPage(Model model, @RequestParam String billId) {
        ActionRequestVO actionRequestVO = new ActionRequestVO();
        actionRequestVO.setActionId(BillActionEunm.PAY_BILL.name());
        actionRequestVO.setData(Integer.valueOf(billId));
        CommonRespModel respModel = billController.billActions(actionRequestVO);
        model.addAttribute("payResult", respModel.getData());
        return "payResult";
    }
}
