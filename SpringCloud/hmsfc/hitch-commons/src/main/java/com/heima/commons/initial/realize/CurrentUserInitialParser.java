package com.heima.commons.initial.realize;


import com.heima.commons.constant.HtichConstants;
import com.heima.commons.initial.InitialParser;
import com.heima.commons.initial.annotation.InitialResolver;
import com.heima.commons.utils.RequestUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class CurrentUserInitialParser implements InitialParser {
    @Override
    public boolean isMatch(Class clazz) {
        return clazz.isAssignableFrom(String.class);
    }

    @Override
    public Object getDefaultValue(Class clazz, InitialResolver initialResolver) {
        return RequestUtils.getRequestHeader(HtichConstants.HEADER_ACCOUNT_KEY);
    }
}
