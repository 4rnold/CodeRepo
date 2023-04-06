package com.heima.commons.ai;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AIResult {
    private List<AiParam> aiParams = new ArrayList<>();

    public AIResult(List<AiParam> aiParams) {
        this.aiParams = aiParams;
    }

    public static AIResult build(List<AiParam> aiParams) {
        return new AIResult(aiParams);
    }

    public String getParameter(String key) {
        if (null == aiParams) {
            return null;
        }
        for (AiParam aiParam : aiParams) {
            if (key.equals(aiParam.getName())) {
                return aiParam.getValue();
            }
        }
        return null;
    }

    public void addParameter(String parameterName, String parameterValue) {
        aiParams.add(new AiParam(parameterName, parameterValue));
    }

    public void addResult(AIResult aiResult) {
        if (this.equals(aiResult)) {
            return;
        }
        List<AiParam> list = aiResult.getAiParams();
        if (null != list && !list.isEmpty()) {
            for (AiParam aiParam : list) {
                addParameter(aiParam.getName(), aiParam.getValue());
            }
        }
    }

    public List<AiParam> getAiParams() {
        return aiParams;
    }

    public void setAiParams(List<AiParam> aiParams) {
        this.aiParams = aiParams;
    }
}
