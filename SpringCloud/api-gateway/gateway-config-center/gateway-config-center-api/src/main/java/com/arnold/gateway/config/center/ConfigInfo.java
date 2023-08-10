package com.arnold.gateway.config.center;

import com.arnold.common.rule.Rule;
import lombok.Data;

import java.util.List;

@Data
public class ConfigInfo {
    private List<Rule> rules;
}
