package com.harry.carrental.harrycarrental.checker;

import com.harry.carrental.harrycarrental.exception.ExceptionConstant;
import com.harry.carrental.harrycarrental.model.CommonRespModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by harryzhu on 2022/7/26
 */
@Slf4j
public class DateChecker {

    public static CommonRespModel checkStartDateAndEndDate(String startDateStr, String endDateStr) {
        // Verification start date and end date
        boolean startDateValid = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        Date nowDate = new Date();;
        if (startDateStr != null && endDateStr != null) {
            try {
                startDate = sdf.parse(startDateStr);
                endDate = sdf.parse(endDateStr);
            } catch (ParseException e) {
                String errMsg = String.format("StartDate or endDate format parse failed. startDate:%s, endDate:%s",
                        startDateStr, endDateStr);
                log.error(errMsg);
            }
        }
        if (startDate == null || endDate == null) {
            String errMsg = String.format("StartDate or endDate format parse failed. startDate:%s, endDate:%s",
                    startDateStr, endDateStr);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        if (nowDate.getTime() - startDate.getTime() > 1000 * 3600 * 24) {
            String errMsg = String.format("Invalid startDate. startDate:%s", startDateStr);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // The end time cannot be later than the start time
        if (startDate.getTime() <= endDate.getTime()) {
            startDateValid = true;
        }
        else {
            String errMsg = String.format("StartDate should before endDate. startDate:%s, endDate:%s",
                    startDateStr, endDateStr);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        if (!startDateValid) {
            String errMsg = String.format("Invalid startDate or endDate. startDate:%s, endDate:%s",
                    startDateStr, endDateStr);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }

        // The end time cannot be later than 1 year
        if (endDate.getTime() > nowDate.getTime() + (3600 * 1000 * 24 * 365)) {
            String errMsg = String.format("The end date cannot exceed one year, endDate:%s", endDateStr);
            log.error(errMsg);
            return new CommonRespModel(ExceptionConstant.COMMON_ERROR_STATUS, errMsg, null);
        }
        return new CommonRespModel(ExceptionConstant.COMMON_SUCCESS_STATUS, null, null);
    }
}
