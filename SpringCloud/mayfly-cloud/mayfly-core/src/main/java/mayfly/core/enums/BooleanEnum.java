package mayfly.core.enums;

import mayfly.core.util.enums.NameValueEnum;

/**
 * boolean枚举类
 *
 * @author meilin.huang
 * @version 1.0
 * @date 2019-12-25 10:28 上午
 */
public enum BooleanEnum implements NameValueEnum<Integer> {
    /**
     * true
     */
    TRUE(1, "true"),

    /**
     * false
     */
    FALSE(0, "false");

    private final Integer value;
    private final String name;

    BooleanEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    /**
     * value是否为true值，即非0值为true
     *
     * @param value value
     * @return value非0则为true
     */
    public static boolean isTrue(Integer value) {
        return !BooleanEnum.FALSE.getValue().equals(value);
    }

    /**
     * value是否为false值，即value是否等于0
     *
     * @param value value
     * @return value是否为0
     */
    public static boolean isFalse(Integer value) {
        return BooleanEnum.FALSE.getValue().equals(value);
    }
}
