package cn.gov.zcy.paas.dto.page;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by zhouzongkun on 2018/10/15.
 */
@Data
public class PagePO implements Serializable {
    private static final long serialVersionUID = 1886107927368103115L;

    /**
     * 初始默认页面大小:10
     */
    private static final Integer DEFAULT_INIT_PAGE_SIZE = 10;
    /**
     * 初始默认页码:1
     */
    private static final Integer DEFAULT_INIT_PAGE_NO = 1;

    /** 正序排序*/
    private static final String SORT_BY_ASC = "ASC";

    /** 倒序排序*/
    private static final String SORT_BY_DESC = "DESC";

    /**
     * 页面大小
     */
    private Integer pageSize = DEFAULT_INIT_PAGE_SIZE;
    /**
     * 页码
     */
    private Integer pageNo = DEFAULT_INIT_PAGE_NO;

    /** 排序字段*/
    private String sortField;

    /**
     * 排序规则：DESC || ASC
     */
    private String sortBy;


    public PagePO() {
        setPageSize(DEFAULT_INIT_PAGE_SIZE);
        setPageNo(DEFAULT_INIT_PAGE_NO);
    }

    private PagePO(Integer pageNo, Integer pageSize) {
        setPageNo(pageNo);
        setPageSize(pageSize);
    }


    public void setPageSize(Integer pageSize) {
        if (Objects.isNull(pageSize)) {
            this.pageSize = DEFAULT_INIT_PAGE_SIZE;
        } else if (pageSize <= 0) {
            this.pageSize = DEFAULT_INIT_PAGE_SIZE;
        } else {
            this.pageSize = pageSize;
        }
    }

    public void setPageNo(Integer pageNo) {
        if (Objects.isNull(pageNo)) {
            this.pageNo = DEFAULT_INIT_PAGE_NO;
        } else if (pageSize <= 0) {
            this.pageNo = DEFAULT_INIT_PAGE_NO;
        } else {
            this.pageNo = pageNo;
        }
    }


    /**
     * @param pageNo：       当前页码
     * @param pageSize：页面大小
     * @return
     */
    public static PagePO valueOf(Integer pageNo, Integer pageSize) {
        return new PagePO(pageNo, pageSize);
    }


    /**
     * @return
     */
    public static PagePO defaultPage() {
        return new PagePO(DEFAULT_INIT_PAGE_NO, DEFAULT_INIT_PAGE_SIZE);
    }

}
