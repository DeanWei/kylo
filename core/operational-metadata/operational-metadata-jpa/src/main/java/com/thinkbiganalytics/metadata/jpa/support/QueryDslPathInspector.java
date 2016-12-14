package com.thinkbiganalytics.metadata.jpa.support;

import com.querydsl.core.types.dsl.EntityPathBase;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper class to inspect the QueryDSL generated Q classes for use in the GenericQueryDslFilter Created by sr186054 on 11/30/16.
 */
public class QueryDslPathInspector {


    /**
     * returns a Map of the Field Name and the respective Field object for a given class
     */
    public static Map<String, Field> getFields(Class<?> cl) {
        return Arrays.asList(cl.getDeclaredFields()).stream().collect(Collectors.toMap(f -> f.getName(), f -> f));
    }


    /**
     * for a given path (separated by dot) get the final object
     *
     * @param basePath the object to start inspecting example:  QJpaBatchJobExecution jpaBatchJobExecution
     * @param fullPath a string representing the path you wish to inspect.  example:  jobInstance.jobName
     * @return return the Object for the path.  example: will return the StringPath jobName on the QJpaBatchJobInstance class
     */
    public static Object getFieldObject(EntityPathBase basePath, String fullPath) throws IllegalAccessException {

        LinkedList<String> paths = new LinkedList<>();
        paths.addAll(Arrays.asList(StringUtils.split(fullPath, ".")));
        return getFieldObject(basePath, paths);
    }


    private static Object getFieldObject(EntityPathBase basePath, LinkedList<String> paths) throws IllegalAccessException {

        String currPath = paths.pop();
        Object o = getObjectForField(basePath, currPath);
        if (o != null && o instanceof EntityPathBase && !paths.isEmpty()) {
            return getFieldObject((EntityPathBase) o, paths);
        }
        return o;
    }

    private static Object getObjectForField(EntityPathBase basePath, String field) throws IllegalAccessException {
        Map<String, Field> fieldSet = getFields(basePath.getClass());
        if(StringUtils.isNotBlank(field)) {
            Field f = fieldSet.get(field);
            if (f != null) {
                Object o = f.get(basePath);
                return o;
            }
        }
         return null;

    }


}
