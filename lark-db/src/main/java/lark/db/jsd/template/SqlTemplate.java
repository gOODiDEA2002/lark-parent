package lark.db.jsd.template;

import lark.db.jsd.result.BuildResult;

/**
 * Created by Administrator on 2015/11/18.
 */
public interface SqlTemplate {
    BuildResult execute(Object obj);
}
