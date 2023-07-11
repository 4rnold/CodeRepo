package mayfly.core.util;

import org.springframework.http.server.PathContainer;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Map;
import java.util.Optional;

/**
 * @author meilin.huang
 * @date 2022-03-25 09:57
 */
public class PathUtils {

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 判断指定的路径 path 是否是一个 pattern(模式)
     * 如果返回值是 false，也就是说 path 不是一个模式，而是一个静态路径(真正的路径字符串),
     * 那么就不用调用方法 {@link #match}了，因为对于静态路径的匹配，直接使用字符串等号比较就足够了。
     *
     * @param path 路径
     * @return 是否为路径模式
     */
    public static boolean isPattern(String path) {
        return PATH_MATCHER.isPattern(path);
    }

    /**
     * 根据路径匹配策略，检查指定的路径{@code path}和指定的模式{@code pattern}是否匹配
     *
     * @param pattern 路径模式
     * @param path    路径
     * @return 是否匹配
     */
    public static boolean match(String pattern, String path) {
        return PathPatternParser.defaultInstance.parse(pattern).matches(PathContainer.parsePath(path));
    }

    /**
     * 获取路径变量给定一个模式和一个路径，提取其中的 URI 模板变量信息。URI模板变量表达式格式为 "{variable}"
     * 例子 : pattern  为 "/hotels/{hotel}" ，路径为 "/hotels/1", 则该方法会返回一个 map ，
     * 内容为 : "hotel"->"1".
     *
     * @param pattern 路径模式
     * @param path    路径
     * @return 路径变量
     */
    public Map<String, String> extractUriTemplateVariables(String pattern, String path) {
        return Optional.ofNullable(PathPatternParser.defaultInstance.parse(pattern).matchAndExtract(PathContainer.parsePath(path)))
                .map(PathPattern.PathMatchInfo::getUriVariables).orElse(null);
    }
}
