package com.study.iocContainer.service;

import com.study.iocContainer.exception.BeanConstrunctionError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class BeanInjector {
    public static void injectValues(Object target, Map<String, String> values) {
        doInjection(target, values, false);
    }

    public static void injectRef(Object target, Map<String, ?> values) {
        doInjection(target, values, true);
    }

    private static void doInjection(Object target, Map<String, ?> values, boolean workWithReferences) {
        for (Map.Entry<String, ?> value : values.entrySet()) {
            Method setter = getSetter(target, value.getKey());
            String parameterType = setter.getParameterTypes()[0].getCanonicalName();
            try {
                if (workWithReferences || parameterType.equals("java.lang.String")) {
                    setter.invoke(value.getValue());
                } else if (parameterType.equals("byte")) {
                    setter.invoke(Byte.parseByte((String) value.getValue()));
                } else if (parameterType.equals("short")) {
                    setter.invoke(Short.parseShort((String) value.getValue()));
                } else if (parameterType.equals("int")) {
                    setter.invoke(Integer.parseInt((String) value.getValue()));
                } else if (parameterType.equals("long")) {
                    setter.invoke(Long.parseLong((String) value.getValue()));
                } else if (parameterType.equals("float")) {
                    setter.invoke(Float.parseFloat((String) value.getValue()));
                } else if (parameterType.equals("double")) {
                    setter.invoke(Double.parseDouble((String) value.getValue()));
                } else if (parameterType.equals("char")) {
                    setter.invoke(((String) value.getValue()).toCharArray());
                } else if (parameterType.equals("boolean")) {
                    setter.invoke(Boolean.parseBoolean((String) value.getValue()));
                } else {
                    throw new BeanConstrunctionError("Unexpected argument type " + parameterType + " for method=" + setter.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new BeanConstrunctionError("Illegal access error for method=" + setter.getName());
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new BeanConstrunctionError("Invocation error for method=" + setter.getName());
            } catch (Exception e) {
                e.printStackTrace();
                throw new BeanConstrunctionError("Error for method=" + setter.getName());
            }


        }
    }

    private static Method getSetter(Object target, String name) {
        String setterName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        for (Method method : target.getClass().getDeclaredMethods()) {
            if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                return method;
            }
        }
        throw new BeanConstrunctionError("Setter not found for property=" + name);
    }
}

