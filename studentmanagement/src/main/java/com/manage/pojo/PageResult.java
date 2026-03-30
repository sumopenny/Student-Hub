package com.manage.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private Long total; //总记录数  
    private List<T> rows; //当前页数据列表

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
}