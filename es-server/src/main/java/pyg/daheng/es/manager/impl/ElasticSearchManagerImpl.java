package pyg.daheng.es.manager.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import pyg.daheng.common.constants.R;
import pyg.daheng.common.utils.JsonUtils;
import pyg.daheng.es.manager.ElasticSearchManager;
import pyg.daheng.es.module.base.GoodsInfo;
import pyg.daheng.es.module.vo.CreateIndexVo;
import pyg.daheng.es.module.vo.QueryContentByKeyWordsVo;
import pyg.daheng.es.repository.GoodsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ZhanSSH
 * @date 2021/2/4 17:33
 */
@Service
@Slf4j
public class ElasticSearchManagerImpl implements ElasticSearchManager {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public R<List<Object>> getContentByKeyWords(QueryContentByKeyWordsVo vo) {
        List<GoodsInfo> result = new ArrayList<>();
        Iterable<GoodsInfo> infos = goodsRepository.findAll();
        while(infos.iterator().hasNext()){
            result.add(infos.iterator().next());
        }
        log.info("查询结果==>{}",JsonUtils.toJsonString(result));
        return R.success();
    }

    @Override
    public R<String> createIndex(CreateIndexVo vo) {

        GoodsInfo goodsInfo = GoodsInfo.builder()
                                   .id(System.currentTimeMillis())
                                   .name(vo.getKeyWord())
                                   .description(System.currentTimeMillis() + "创建了该商品")
                                   .build();
        GoodsInfo re = goodsRepository.save(goodsInfo);
        return R.success("success!", JsonUtils.toJsonString(re));
    }

}
