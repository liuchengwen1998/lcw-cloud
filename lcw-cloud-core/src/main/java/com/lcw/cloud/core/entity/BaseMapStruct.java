package com.lcw.cloud.core.entity;

import java.util.List;

/**
 * MapStruct 基类，定义常用转换方法
 *
 * @author yzhang
 * @since 1.0
 */
public interface BaseMapStruct<S, T> {

    /**
     * 原对象转换为目标对象
     *
     * @param source 原始对象
     * @return 目标对象
     */
    T toTarget(S source);

    /**
     * 原对象转换为目标对象
     *
     * @param source 原始对象集合
     * @return 目标对象
     */
    List<T> toTarget(List<S> source);

    /**
     * 目标对象反向转为源对象
     *
     * @param target 原始对象集合
     * @return 目标对象
     */
    S toSource(T target);

    /**
     * 目标对象集合反向转为源对象集合
     *
     * @param target 原始对象集合
     * @return 目标对象
     */
    List<S> toSource(List<T> target);
}
