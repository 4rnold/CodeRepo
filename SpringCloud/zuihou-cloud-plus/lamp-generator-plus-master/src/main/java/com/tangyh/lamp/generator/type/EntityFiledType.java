package com.tangyh.lamp.generator.type;


import java.io.File;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * 实体字段类型
 *
 * @author zuihou
 * @date 2019/05/14
 */
@Data
@Builder
@ToString
@EqualsAndHashCode(of = {"name", "table"})
public class EntityFiledType {
    /**
     * 枚举类型的字段名（不是数据库字段哦！！！）
     */
    private String name;
    /**
     * 枚举类型的完整包路径 eg: com.xx.xx.Type
     */
    private String packagePath = "";
    /**
     * 表名
     */
    private String table = "";

    /**
     * 是否生成
     */
    private GenerateType gen = GenerateType.OVERRIDE;

    /**
     * 枚举类型的完整路径，会根据 packagePath 自动解析
     */
    private String path;
    /**
     * 导包
     */
    private String importPackage;
    /**
     * 枚举类型: 会根据 packagePath 自动解析
     */
    private String type;

    public String getPath() {
        if (packagePath != null && !"".equals(packagePath)) {
            this.path = packagePath.replace(".", File.separator);
        }
        return path;
    }

    public String getType() {
        if (packagePath != null && !"".equals(packagePath)) {
            this.type = packagePath.substring(packagePath.lastIndexOf(".") + 1);
        }
        return type;
    }

    public String getImportPackage() {
        if (this.importPackage == null || "".equals(this.importPackage)) {
            this.importPackage = StringUtils.substring(packagePath, 0, packagePath.lastIndexOf("."));
        }
        return this.importPackage;
    }

}

