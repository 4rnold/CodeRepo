package com.heima.commons.ai;

public class AiParam {
    private String field;
    private String name;
    private String value;

    public AiParam(String name, String field) {
        this.name = name;
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
