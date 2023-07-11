package mayfly.sys.api.enums;

import mayfly.core.util.enums.NameValueEnum;

/**
 * api关联code的类型
 *
 * @author meilin.huang
 * @date 2020-03-05 5:01 下午
 */
public enum ApiCodeTypeEnum implements NameValueEnum<Integer> {
    /**
     * 角色
     */
    ROLE(1, "角色"),
    RESOURCE(2, "资源");

    private final Integer value;
    private final String name;

    ApiCodeTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
