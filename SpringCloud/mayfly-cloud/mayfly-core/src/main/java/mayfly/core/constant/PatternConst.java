package mayfly.core.constant;

/**
 * 正则模式常量
 *
 * @author meilin.huang
 * @date 2021-10-19 5:28 下午
 */
public class PatternConst {

    /**
     * 汉字
     */
    public static final String CHINESE = "^[\\u4e00-\\u9fa5]+$";

    /**
     * 手机号正则
     */
    public static final String MOBILE = "^1[3-9]\\d{9}$";

    /**
     * 整数
     */
    public static final String NUMBER = "^\\d+$";
}
