package lark.db.jsd;

import lombok.Data;

import java.util.List;

@Data
public class PageEntity<T> {

    /**
     * 当前页
     */
    private Integer pageIndex;

    /**
     * 每页显示条数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pageCount;

    /**
     * 总条数
     */
    private Integer pageTotalSize;

    /**
     * 数据
     */
    private List<T> list;


}
