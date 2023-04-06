package com.heima.commons.initial.factory;

import com.heima.commons.domin.po.PO;
import com.heima.commons.domin.vo.VO;
import com.heima.commons.groups.Group;
import com.heima.commons.initial.InitialParser;
import com.heima.commons.initial.annotation.InitialResolver;
import com.heima.commons.initial.annotation.RequestInitial;
import com.heima.commons.utils.CommonsUtils;
import com.heima.commons.utils.reflect.ReflectUtils;
import com.heima.commons.utils.reflect.wrap.WrapObject;
import com.heima.commons.utils.reflect.wrap.WrapObjectFactory;
import com.heima.commons.utils.reflect.wrap.WrapProperty;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class InitialParserFactory {

    public static PO initialDefValueForPO(PO po) {
        Object vo = ReflectUtils.newInstance(po.getVO());
        initialDefValue(vo, Group.All.class);
        return CommonsUtils.toPO((VO) vo);
    }

    public static void initialDefValueForVO(VO vo) {
        initialDefValue(vo, Group.All.class);
    }

    public static void initialDefValue(Object[] objArray, RequestInitial requestInitial) {
        if (null != objArray && objArray.length > 0) {
            for (Object obj : objArray) {
                if (obj instanceof VO) {
                    //request可能有多个入参，只对VO类赋值
                    initialDefValue(obj, requestInitial);
                }
            }
        }
    }

    public static void initialDefValue(Object obj, RequestInitial requestInitial) {
        initialDefValue(obj, requestInitial.groups());
    }

    public static void initialDefValue(Object obj, Class<?>... groups) {
        //Wrapper赋值工具
        WrapObject wrapObject = WrapObjectFactory.getWarpObject(obj);
        InitialParserFactory initialParserFactory = new InitialParserFactory();
        for (WrapProperty property : wrapObject.getPropertyList()) {
            Object value = wrapObject.getValue(property.getPropName());
            if (null != value) {
                //属性有值的话，跳过
                continue;
            }
            //从VO的中拿到Resolver注解定义的生成器
            InitialResolver initialResolver = getInitialResolver(property);
            if (initialResolver == null) {
                continue;
            }
            //判断注解的group信息，resolver和controller匹配上就反射生成resolver（属性值生成器）
            InitialParser initialParser = initialParserFactory.getInitialParser(property, initialResolver, groups);
            if (null != initialParser && initialParser.isMatch(property.getDataType())) {
                //获取值，通过wrapper工具赋值上去（反射）。
                wrapObject.setValue(property.getPropName(), initialParser.getDefaultValue(property.getDataType(), initialResolver));
            }
        }
    }


    public static InitialResolver getInitialResolver(WrapProperty property) {
        Annotation[] annotations = property.getAnnotations();
        return getInitialResolverAnnotation(annotations);
    }


    public static InitialParser getInitialParser(WrapProperty wrapProperty, InitialResolver initialResolver, Class[] groups) {
        Class<?>[] groupArray = initialResolver.groups();
        if (groupArray == null || groups == null) {
            return null;
        }
        if (groupArray.length == 0 || groups.length == 0) {
            return null;
        }
        boolean flag = false;
        if (groups[0] == Group.All.class) {
            flag = true;
        } else {
            flag = CollectionUtils.containsAny(Arrays.asList(groupArray), groups);
        }
        return flag ? ReflectUtils.newInstance(initialResolver.resolver().getResolverClass()) : null;
    }


    private static InitialResolver getInitialResolverAnnotation(Annotation[] annotations) {
        if (null != annotations && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof InitialResolver) {
                    return (InitialResolver) annotation;
                }
            }
        }
        return null;
    }


}
