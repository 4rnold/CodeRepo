package com.heima.commons.ai;

import java.util.ArrayList;
import java.util.List;

public class AiParamFactory {

    public static List<AiParam> getLicensePlateParam() {
        return new ArrayList<AiParam>() {{
            add(new AiParam("card_number", "words_result.number"));
        }};
    }


    public static List<AiParam> getIdCardParam() {
        return new ArrayList<AiParam>() {{
            add(new AiParam("useralias", "words_result.姓名.words"));
            add(new AiParam("idCardNumber", "words_result.公民身份号码.words"));
            add(new AiParam("idCardNumber", "words_result.公民身份号码.words"));
            add(new AiParam("birth", "words_result.出生.words"));
            add(new AiParam("nation", "words_result.民族.words"));
            add(new AiParam("sex", "words_result.性别.words"));
        }};
    }

    public static List<AiParam>  getIdCardAuthParam() {
        return new ArrayList<AiParam>() {{
            add(new AiParam("authentication", "errorCode"));
            add(new AiParam("errorMgs", "errorMgs"));
        }};
    }
}
