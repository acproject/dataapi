package com.owiseman.dataapi.dto;

import java.util.List;

public class PageResult<T> {
    private List<T> data;
    private int currentPage;
    private int pageSize;
    private int total;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public PageResult(List<T> data, int currentPage, int pageSize, int total) {
        this.data = data;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.total = total;
    }
}
