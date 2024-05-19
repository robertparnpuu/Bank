package com.rparnp.bank.util;

import com.rparnp.bank.enums.CurrencyType;
import org.springframework.util.ObjectUtils;

import java.util.List;

public final class CurrencyUtil {

    public static boolean isInvalid(String currencyString) {
        return !ObjectUtils.containsConstant(CurrencyType.values(), currencyString);
    }

    public static boolean anyInvalid(List<String> currencyStrings) {
        return currencyStrings.stream().anyMatch(CurrencyUtil::isInvalid);
    }
}
