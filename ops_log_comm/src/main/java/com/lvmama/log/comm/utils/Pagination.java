package com.lvmama.log.comm.utils;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页返回的对象
 */
public class Pagination<T> implements Serializable{

    private static final long serialVersionUID = 6394363457272818698L;
    // 当前页
    private Integer curPage = null;
    //页面大小
    private Integer pageSize = null;
    //总条数
    private Integer totalRows = null;
    //数据内容
    private List<T> itemList = null;

    public Pagination(int curPage, int pageSize) {
        this.curPage = curPage;
        this.pageSize = pageSize;
        this.totalRows = 0;
        this.itemList = Collections.emptyList();
    }

    /**
     * 获取开始行
     * @return
     */
    public Integer getStartRow(){
        Integer curRow = (this.curPage-1) * this.pageSize;
        curRow = curRow > 0 ? curRow + 1 : curRow;
        return curRow;
    }

    public Integer getEndRow(){
        return this.curPage * this.pageSize - 1 + 1;
    }

    public boolean isEmpty(){
        return CollectionUtils.isEmpty(this.getItemList());
    }

    public boolean isNotEmpty(){
        return !this.isEmpty();
    }

    public Integer getCurPage() {
        return curPage;
    }

    public void setCurPage(Integer curPage) {
        this.curPage = curPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public List<T> getItemList() {
        return itemList;
    }

    public void setItemList(List<T> itemList) {
        this.itemList = itemList;
    }
}
