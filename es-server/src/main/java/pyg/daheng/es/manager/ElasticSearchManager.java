package pyg.daheng.es.manager;

import org.apache.poi.ss.formula.functions.T;
import pyg.daheng.common.constants.R;
import pyg.daheng.es.module.vo.CreateIndexVo;
import pyg.daheng.es.module.vo.QueryContentByKeyWordsVo;

import java.util.List;

/**
 * @author ZhanSSH
 * @date 2021/2/4 17:33
 */
public interface ElasticSearchManager {

    /**
     * 根据关键字搜索
     * @param vo 入参
     * @return 出参
     */
    R<List<Object>> getContentByKeyWords(QueryContentByKeyWordsVo vo);

    /**
     * 创建索引
     * @param vo 入参
     * @return 出参
     */
    R<String> createIndex(CreateIndexVo vo);

}
