package com.harry.carrental.harrycarrental.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.springframework.util.Assert;

/**
 * Created by harryzhu on 2022/7/25
 */
public class ObjectMapperUtils {
    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <T, S> T convert(S source, T target) throws MappingException {
        if (source == null) {
            return null;
        }
        Assert.notNull(target, "target instance required");
        mapper.map(source, target);
        return target;
    }

    public static <T, S> T convert(S source, Class<T> targetClass) throws MappingException {
        if (source == null) {
            return null;
        }

        Assert.notNull(targetClass, "targetClass required");
        T target = mapper.map(source, targetClass);
        return target;
    }

    public static <T, S> List<T> convert(List<S> source, Class<T> targetClass) throws MappingException {
        if (source == null) {
            return null;
        }

        List<T> targetList = new ArrayList<T>();
        Iterator<S> iterator = source.iterator();
        while (iterator.hasNext()) {
            T target = mapper.map(iterator.next(), targetClass);
            targetList.add(target);
        }
        return targetList;
    }
}
