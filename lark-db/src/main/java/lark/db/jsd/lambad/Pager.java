package lark.db.jsd.lambad;

import lombok.Data;

@Data
public class Pager {

    /**
     * 当前页
     */
    private Integer pageIndex = 1;

    /**
     * 每页显示条数
     */
    private Integer pageSize = 10;
}
