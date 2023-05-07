import cn.hutool.core.lang.Console;
import com.tangyh.lamp.generator.VueGenerator;
import com.tangyh.lamp.generator.config.CodeGeneratorConfig;
import com.tangyh.lamp.generator.config.FileCreateConfig;
import com.tangyh.lamp.generator.model.GenTableColumn;
import com.tangyh.lamp.generator.type.EntityFiledType;
import com.tangyh.lamp.generator.type.EntityType;
import com.tangyh.lamp.generator.type.GenerateType;
import com.tangyh.lamp.generator.type.HtmlType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.tangyh.lamp.generator.model.GenTableColumn.NO;
import static com.tangyh.lamp.generator.model.GenTableColumn.YES;

/**
 * 生成 lamp-web 的前端代码
 *
 * @author zuihou
 * @date 2019/05/25
 */
public class TestVueGenerator {
    /***
     * 注意，想要在这里直接运行，需要手动增加 mysql 驱动
     * @param args
     */
    public static void main(String[] args) {
        // 生成前端页面，一定要设置成true
        FileCreateConfig fileCreateConfig = new FileCreateConfig(null, true);
//        FileCreateConfig fileCreateConfig = new FileCreateConfig(GenerateType.OVERRIDE, true);

//        CodeGeneratorConfig build = buildListEntity(fileCreateConfig);
        CodeGeneratorConfig build = buildTreeEntity(fileCreateConfig);

        //mysql 账号密码
        build.setUsername("root");
        build.setPassword("root");

        // 文件生成策略
        build.setFileCreateConfig(fileCreateConfig);

        // 前端代码的绝对路径
        String vuePath = "/Users/tangyh/github/lamp-web";
        build.setProjectRootPath(vuePath);
        Console.log("代码输出路径：{}", vuePath);

        //手动指定枚举类生成的路径， 不配置，则默认跟实体类平级，存放在enumeration包下
        Set<EntityFiledType> filedTypes = new HashSet<>();
        filedTypes.addAll(Arrays.asList(
        ));
        build.setFiledTypes(filedTypes);

        // 自定义前端页面字段的显示演示， 不填写时，默认生成全字段
//        buildVue(build);

        //生成代码
        VueGenerator.run(build);
    }

    /**
     * 程序默认规则如下：
     * isInsert/isUpdate/isList: 排除Entity 和 SuperEntity 字段外的所有字段
     * isQuery: 字段为 必填的
     *
     * @param build
     */
    private static void buildVue(CodeGeneratorConfig build) {
        CodeGeneratorConfig.Vue vue = new CodeGeneratorConfig.Vue();
        // 生成的代码位于前端项目 src 下的什么路径？  默认是:  src/views/lamp
//        vue.setViewsPath("views" + File.separator + "lamp");

        // 程序自动根据 表设计情况 为每个字段选择合适显示规则， 若不满足，则在此添加字段后修改即可
        Map<String, Map<String, GenTableColumn>> map = new HashMap<>();
        //字段名 对应的显示方式
        Map<String, GenTableColumn> keyField = new HashMap<>();
//        keyField.put("key", new GenTableColumn("key", YES, YES, YES, YES, HtmlType.INPUT));
        keyField.put("name", new GenTableColumn("name", YES, YES, YES, YES, HtmlType.INPUT));
//        keyField.put("value", new GenTableColumn("value", YES, YES, YES, YES, HtmlType.INPUT));
//        keyField.put("describe_", new GenTableColumn("describe_", YES, YES, YES, NO, HtmlType.TEXTAREA));
//        keyField.put("status_", new GenTableColumn("status_", YES, YES, YES, NO, HtmlType.RADIO_BUTTON));
//        keyField.put("readony_", new GenTableColumn("readony_", NO, NO, YES, NO, HtmlType.RADIO_BUTTON));
        // 指定 type3 字段使用 radio-button 样式， 拉取字典 EDUCATION 的值
        keyField.put("type3", new GenTableColumn("type3", YES, YES, YES, NO, HtmlType.RADIO_BUTTON).setDictType("EDUCATION"));
        // 指定 type2 字段使用 select 样式， 拉取枚举 ProductType2Enum 的值
        keyField.put("type2", new GenTableColumn("type2", YES, YES, YES, NO, HtmlType.SELECT).setEnumType("ProductType2Enum"));
        // 指定 type_ 字段使用 radio 样式， 拉取枚举 ProductType 的值
        keyField.put("type_", new GenTableColumn("type_", YES, YES, YES, NO, HtmlType.RADIO).setEnumType("ProductType"));
        // 指定 status 字段使用 SWITCH 样式
        keyField.put("status", new GenTableColumn("status", NO, NO, YES, NO, HtmlType.SWITCH));
        //表名
        map.put("m_product", keyField);

        vue.setTableFieldMap(map);
        build.setVue(vue);
    }


    /**
     * 生成 table 分页 型页面
     * <p>
     * 生成代码后，会生成如下代码：
     * src/api/Xxx.js
     * src/views/lamp/base/xxx/Index.vue
     * src/views/lamp/base/xxx/Edit.vue
     * src/lang/lang.*.js (该文件中的代码分别复制到 en.js 和 zh.js， 然后删除自己！)
     *
     * @return
     */
    public static CodeGeneratorConfig buildListEntity(FileCreateConfig fileCreateConfig) {
        // 配置需要生成的表
        List<String> tables = Arrays.asList(
                "b_product"
        );
        CodeGeneratorConfig build = CodeGeneratorConfig.
                buildVue("mall",  // 服务名 必填
                        "b_",            // 表前缀
                        tables);

//        build.setLikeTable(new LikeTable("b\\_", SqlLike.RIGHT));

        //父类是Entity
        build.setSuperEntity(EntityType.ENTITY);

        //生成的前端页面位于 src/${build.getVue().getViewsPath()}/${childPackageName} 目录下
        build.setChildPackageName("more");

        // 数据库信息
        build.setUrl("jdbc:mysql://127.0.0.1:3306/lamp_extend_0000?serverTimezone=CTT&characterEncoding=utf8&useUnicode=true&useSSL=false&autoReconnect=true&zeroDateTimeBehavior=convertToNull");

        fileCreateConfig.setGenerateApi(GenerateType.OVERRIDE);
        fileCreateConfig.setGeneratePageIndex(GenerateType.OVERRIDE);
        fileCreateConfig.setGenerateEdit(GenerateType.OVERRIDE);

        fileCreateConfig.setGenerateTreeIndex(GenerateType.IGNORE);
        return build;
    }

    /**
     * 生成 tree 树结构页面
     * 1, 前提条件： m_product 表一定要有 TreeEntity实体中的字段(label, parentId, sortValue, id, createTime,createUser, updateTime, updateUser)！
     * <p>
     * 生成代码后，会生成如下代码：
     * src/api/Xxx.js
     * src/views/lamp/base/xxx/Tree.vue
     * src/lang/lang.js (该文件中的代码分别复制到 en.js 和 zh.js， 然后删除自己！)
     *
     * @return
     */
    public static CodeGeneratorConfig buildTreeEntity(FileCreateConfig fileCreateConfig) {
        // 配置需要生成的表
        List<String> tables = Arrays.asList(
                "b_product"
        );
        CodeGeneratorConfig build = CodeGeneratorConfig.
                buildVue("mall",  // 服务名 必填
                        "b_",            // 表前缀
                        tables);

        //父类是TreeEntity
        build.setSuperEntity(EntityType.TREE_ENTITY);

        //生成的前端页面位于 src/${build.getVue().getViewsPath()}/${childPackageName} 目录下
        build.setChildPackageName("more");
        build.setUrl("jdbc:mysql://127.0.0.1:3306/lamp_extend_0000?serverTimezone=CTT&characterEncoding=utf8&useUnicode=true&useSSL=false&autoReconnect=true&zeroDateTimeBehavior=convertToNull");

        fileCreateConfig.setGenerateApi(GenerateType.OVERRIDE);

        fileCreateConfig.setGeneratePageIndex(GenerateType.IGNORE);
        fileCreateConfig.setGenerateEdit(GenerateType.IGNORE);
        fileCreateConfig.setGenerateTreeIndex(GenerateType.OVERRIDE);

        return build;
    }

}
