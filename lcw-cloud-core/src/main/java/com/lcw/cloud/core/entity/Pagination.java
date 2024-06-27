package com.lcw.cloud.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 *
 * @author ：yzhang
 * @since ：2022/2/15 09:16
 * 
 */
@AllArgsConstructor
@NoArgsConstructor
public class Pagination<T> implements Serializable {

    private static final long serialVersionUID = 6156265077478024716L;

    /**
     * 总页数
     */
    @Getter
    private Long total;

    /**
     * 结果集
     */
    @Getter
    private List<T> list;

    /**
     * 空对象
     */
    private static final Pagination<?> EMPTY = new Pagination(0L, new ArrayList<>());

    public static <T> Pagination<T> of(Long total, List<T> data) {
        return new Pagination<T>(total, data);
    }

    public static <T> Pagination<T> empty() {
        return (Pagination<T>) EMPTY;
    }
}
