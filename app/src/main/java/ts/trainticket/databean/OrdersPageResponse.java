package ts.trainticket.databean;

import java.util.List;

/**
 * Created by liuZOZO on 2018/1/18.
 */
public class OrdersPageResponse {
    private boolean status;
    private String msg;
    private int currentPageNum;
    private int pageSize;
    private int totalPageNum;
    private int totalCount;
    private List<Orders> orderses;

    public OrdersPageResponse() {
    }

    public OrdersPageResponse(boolean status, String msg, List<Orders> orderses) {
        this.status = status;
        this.msg = msg;
        this.orderses = orderses;
    }

    public OrdersPageResponse(boolean status, String msg, int currentPageNum, int pageSize, int totalPageNum, int totalCount, List<Orders> orderses) {
        this.status = status;
        this.msg = msg;
        this.currentPageNum = currentPageNum;
        this.pageSize = pageSize;
        this.totalPageNum = totalPageNum;
        this.totalCount = totalCount;
        this.orderses = orderses;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public void setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Orders> getOrderses() {
        return orderses;
    }

    public void setOrderses(List<Orders> orderses) {
        this.orderses = orderses;
    }
}
