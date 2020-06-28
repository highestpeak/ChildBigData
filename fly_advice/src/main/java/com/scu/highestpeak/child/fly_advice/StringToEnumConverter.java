package com.scu.highestpeak.child.fly_advice;

import com.scu.highestpeak.child.fly_advice.domain.CABIN_CLASS;
import org.springframework.core.convert.converter.Converter;

/**
 * @author highestpeak
 */
public class StringToEnumConverter implements Converter<String, CABIN_CLASS> {
    @Override
    public CABIN_CLASS convert(String source) {
        try {
            return CABIN_CLASS.valueOf(source.toUpperCase());
            // future: 自定义异常处理，以针对不同错误返回
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
