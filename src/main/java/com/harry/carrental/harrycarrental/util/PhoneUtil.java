package com.harry.carrental.harrycarrental.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by harryzhu on 2022/7/25
 */
@Slf4j
public class PhoneUtil {
    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static List<String> supportCountryCodes;

    /**
     * 校验手机号是否有效
     *
     * @param rawPhoneNumber
     * @return
     */
    public static boolean checkRawPhoneNumber(String rawPhoneNumber) {
        if (rawPhoneNumber == null) {
            return false;
        }
        PhoneNumber pn = null;
        try {
            pn = phoneNumberUtil.parse(rawPhoneNumber, "CN");
            return phoneNumberUtil.isValidNumber(pn);
        } catch (NumberParseException e) {
            log.error("parse raw phone number failed. rawPhoneNumber:{}", rawPhoneNumber);
        }
        return false;
    }

    /**
     * 从原始电话号码里获取country code
     *
     * @param rawPhoneNumber
     * @return
     */
    public static String getCountryCodeFromRawData(String rawPhoneNumber) {
        PhoneNumber pn = null;
        try {
            pn = phoneNumberUtil.parse(rawPhoneNumber, "CN");
            return "+" + pn.getCountryCode();
        } catch (NumberParseException e) {
            log.error("parse raw phone number failed. rawPhoneNumber:{}", rawPhoneNumber);
        }
        return null;
    }

    /**
     * 从原始电话号码里获取phone number
     *
     * @param rawPhoneNumber
     * @return
     */
    public static String getPhoneNumberFromRawData(String rawPhoneNumber) {
        PhoneNumber pn = null;
        try {
            pn = phoneNumberUtil.parse(rawPhoneNumber, "CN");
            return "" + pn.getNationalNumber();
        } catch (NumberParseException e) {
            log.error("parse raw phone number failed. rawPhoneNumber:{}", rawPhoneNumber);
        }
        return null;
    }

    /**
     * 获取当前country code
     *
     * @return
     */
    public static String getCurrentCountryCode(HttpServletRequest request){
        if (request == null || request.getLocale() == null || request.getLocale().getCountry() == null) {
            return "+" + phoneNumberUtil.getCountryCodeForRegion(Locale.getDefault().getCountry());
        }
        else {
            return "+" + phoneNumberUtil.getCountryCodeForRegion(request.getLocale().getCountry());
        }
    }

    /**
     * 获取当前支持的country code列表
     *
     * @return
     */
    public static List<String> getSupportedCountryCodes() {
        if (supportCountryCodes == null){
            Set<String> supportCountryCodesSet = Sets.newHashSet();
            Set<String> supportedRegions = phoneNumberUtil.getSupportedRegions();
            for (String region : supportedRegions) {
                supportCountryCodesSet.add("+" + phoneNumberUtil.getCountryCodeForRegion(region));
            }
            supportCountryCodes = Lists.newArrayList(supportCountryCodesSet);
            Collections.sort(supportCountryCodes);
        }

        return supportCountryCodes;
    }
}
