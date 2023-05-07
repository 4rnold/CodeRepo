package ${package.Entity};

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
<#list table.importPackages as pkg>
import ${pkg};
</#list>
<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import java.time.LocalDateTime;
<#if entityLombokModel>
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.tangyh.lamp.common.constant.DictionaryType;
import static com.tangyh.basic.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;
</#if>
<#if cfg.filedTypes??>
<#list cfg.filedTypes as fieldType>
    <#list table.fields as field>
        <#if field.propertyName == fieldType.name && table.name==fieldType.table && field.propertyType=="String">
import ${fieldType.packagePath};
            <#break>
        </#if>
    </#list>
</#list>
</#if>

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

<#assign tableComment = "${table.comment!''}"/>
<#if table.comment?? && table.comment!?contains('\n')>
    <#assign tableComment = "${table.comment!?substring(0,table.comment?index_of('\n'))?trim}"/>
</#if>
/**
 * <p>
 * 实体类
 * ${table.comment!?replace("\n","\n * ")}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
@NoArgsConstructor
@ToString(callSuper = true)
<#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
<#else>
@EqualsAndHashCode(callSuper = false)
</#if>
@Accessors(chain = true)
</#if>
<#if table.convert>
@TableName("${table.name}")
</#if>
<#if swagger2>
@ApiModel(value = "${entity}", description = "${tableComment}")
</#if>
<#if superEntityClass??>
@AllArgsConstructor
<#assign hasCustomAnno="0"/>
<#if superEntityClass?? && superEntityClass=="TreeEntity">
    <#assign hasCustomAnno="1"/>
</#if>
public class ${entity} extends ${superEntityClass}<#if activeRecord><${entity}></#if><#list table.commonFields as field><#if field.keyFlag><<#if hasCustomAnno == "1">${entity}, </#if>${field.propertyType}></#if></#list> {
<#elseif activeRecord>
@AllArgsConstructor
public class ${entity} extends Model<${entity}> {
<#else>
public class ${entity} implements Serializable {
</#if>

    private static final long serialVersionUID = 1L;

<#setting number_format="0">
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#-- 如果有父类，排除公共字段 -->
    <#if (superEntityClass?? && cfg.superExtend?? && field.propertyName !="id") || (superEntityClass?? && field.propertyName !="id" && field.propertyName !="createTime" && field.propertyName != "updateTime" && field.propertyName !="createUser" && field.propertyName !="updateUser") || !superEntityClass??>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>
    <#assign fieldComment="${field.comment!}"/>
    <#if field.comment!?length gt 0>
    /**
     * ${field.comment!?replace("\n","\n     * ")}
     */
    <#if field.comment!?contains("\n") >
        <#assign fieldComment="${field.comment!?substring(0,field.comment?index_of('\n'))?replace('\r\n','')?replace('\r','')?replace('\n','')?trim}"/>
    </#if>
    </#if>
    <#if swagger2>
    @ApiModelProperty(value = "${fieldComment}")
    </#if>
    <#assign myPropertyType="${field.propertyType}"/>
    <#assign isEnumType="1"/>
    <#list cfg.filedTypes as fieldType>
        <#if fieldType.name == field.propertyName && table.name==fieldType.table && field.propertyType=="String">
            <#assign myPropertyType="${fieldType.type}"/>
            <#assign isEnumType="2"/>
        </#if>
    </#list>
    <#if field.customMap.Null == "NO" >
        <#if (field.columnType!"") == "STRING" && isEnumType == "1">
    @NotEmpty(message = "${fieldComment}不能为空")
        <#else>
    @NotNull(message = "${fieldComment}不能为空")
        </#if>
    </#if>
    <#if (field.columnType!"") == "STRING" && isEnumType == "1">
        <#assign max = 255/>
        <#if field.type?starts_with("varchar") || field.type?starts_with("char")>
            <#if field.type?contains("(")>
                <#assign max = field.type?substring(field.type?index_of("(") + 1, field.type?index_of(")"))/>
            </#if>
    @Length(max = ${max}, message = "${fieldComment}长度不能超过${max}")
        <#elseif field.type?starts_with("text")>
        <#assign max = 65535/>
    @Length(max = ${max}, message = "${fieldComment}长度不能超过${max}")
        <#elseif field.type?starts_with("mediumtext")>
        <#assign max = 16777215/>
    @Length(max = ${max}, message = "${fieldComment}长度不能超过${max}")
        <#elseif field.type?starts_with("longtext")>

        </#if>
    <#else>
        <#if field.propertyType?starts_with("Short")>
    @Range(min = Short.MIN_VALUE, max = Short.MAX_VALUE, message = "${fieldComment}长度不能超过"+Short.MAX_VALUE)
        </#if>
        <#if field.propertyType?starts_with("Byte")>
    @Range(min = Byte.MIN_VALUE, max = Byte.MAX_VALUE, message = "${fieldComment}长度不能超过"+Byte.MAX_VALUE)
        </#if>
        <#if field.propertyType?starts_with("Short")>
    @Range(min = Short.MIN_VALUE, max = Short.MAX_VALUE, message = "${fieldComment}长度不能超过"+Short.MAX_VALUE)
        </#if>
    </#if>
    <#if field.keyFlag>
    <#-- 主键 -->
        <#if field.keyIdentityFlag>
    @TableId(value = "${field.name}", type = IdType.AUTO)
        <#elseif idType??>
    @TableId(value = "${field.name}", type = IdType.${idType})
        <#elseif field.convert>
    @TableId("${field.name}")
        </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- -----   存在字段填充设置   ----->
        <#if field.convert>
    @TableField(value = "${field.name}", fill = FieldFill.${field.fill})
        <#else>
    @TableField(fill = FieldFill.${field.fill})
        </#if>
    <#elseif field.convert>
        <#if (field.type?starts_with("varchar") || field.type?starts_with("char")) && myPropertyType == "String">
    @TableField(value = "${field.name}", condition = LIKE)
        <#else>
    @TableField("${field.name}")
        </#if>
    </#if>
    <#-- 乐观锁注解 -->
    <#if (versionFieldName!"") == field.name>
    @Version
    </#if>
    <#-- 逻辑删除注解 -->
    <#if (logicDeleteFieldName!"") == field.name>
    @TableLogic
    </#if>
    <#assign myPropertyName="${field.propertyName}"/>
    <#-- 自动注入注解 -->
    <#if field.customMap.annotation??>
    ${field.customMap.annotation}
    @ExcelEntity
        <#assign myPropertyType="${field.customMap.type}"/>
        <#if field.propertyName?ends_with("Id")>
            <#assign myPropertyName="${field.propertyName!?substring(0,field.propertyName?index_of('Id'))}"/>
        </#if>
    </#if>
    @Excel(name = "${fieldComment}"<#if myPropertyType!?contains("Local")>, format = DEFAULT_DATE_TIME_FORMAT, width = 20</#if><#if myPropertyType!?contains("Boolean")>, replace = {"是_true", "否_false", "_null"}</#if><#if isEnumType=="2">, replace = {<#list field.customMap.enumCustom.list?keys as key>"${field.customMap.enumCustom.list[key][0]}_${key?upper_case}", </#list> "_null"}</#if>)
    private ${myPropertyType} ${myPropertyName};
    </#if>

</#list>
<#------------  END 字段循环遍历  ---------->
<#if !entityLombokModel>

    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
    public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
    }

        <#if entityBuilderModel>
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
        <#else>
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
        </#if>
        this.${field.propertyName} = ${field.propertyName};
        <#if entityBuilderModel>
        return this;
        </#if>
    }
    </#list>
</#if>

<#-- 如果有父类，自定义无全参构造方法 -->
    @Builder
    public ${entity}(<#list table.commonFields as cf><#if cf.propertyName!="tenantCode">${cf.propertyType} ${cf.propertyName}, </#if></#list>
                    <#list table.fields as field><#assign myPropertyType="${field.propertyType}"/><#if field.customMap.annotation??><#assign myPropertyType="${field.customMap.type}"/></#if><#list cfg.filedTypes as fieldType><#if fieldType.name == field.propertyName && table.name==fieldType.table && field.propertyType=="String"><#assign myPropertyType="${fieldType.type}"/></#if></#list><#if field_has_next>${((field_index + 1) % 6 ==0)?string('\r\n                    ', '')}${myPropertyType} ${field.propertyName}, <#else>${myPropertyType} ${field.propertyName}</#if></#list>) {
    <#list table.commonFields as cf>
        <#if cf.propertyName!="tenantCode">
        this.${cf.propertyName} = ${cf.propertyName};
        </#if>
    </#list>
    <#list table.fields as field>
        <#assign myPropertyName="${field.propertyName}"/>
        <#-- 自动注入注解 -->
        <#if field.customMap.annotation?? && field.propertyName?ends_with("Id")>
            <#assign myPropertyName="${field.propertyName!?substring(0,field.propertyName?index_of('Id'))}"/>
        </#if>
        this.${myPropertyName} = ${field.propertyName};
    </#list>
    }
<#if activeRecord>

    @Override
    protected Serializable pkVal() {
    <#if keyPropertyName??>
        return this.${keyPropertyName};
    <#else>
        return null;
    </#if>
    }
</#if>
<#if !entityLombokModel>

    @Override
    public String toString() {
        return "${entity}{" +
    <#list table.fields as field>
        <#if field_index==0>
        "${field.propertyName}=" + ${field.propertyName} +
        <#else>
        ", ${field.propertyName}=" + ${field.propertyName} +
        </#if>
    </#list>
        "}";
    }
</#if>

}
