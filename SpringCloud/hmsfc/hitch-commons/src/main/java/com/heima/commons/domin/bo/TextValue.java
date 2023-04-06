package com.heima.commons.domin.bo;

//distance":{"text":"24.2公里","value":24157},"duration":{"text":"15分钟","value":905}}
public class TextValue {
    private String text;
    private Integer value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
