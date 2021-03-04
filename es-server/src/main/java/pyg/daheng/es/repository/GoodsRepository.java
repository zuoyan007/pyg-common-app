package pyg.daheng.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;
import pyg.daheng.es.module.base.GoodsInfo;

/**
 * @author ZhanSSH
 * @date 2021/2/22 15:01
 */
@Component
public interface GoodsRepository extends ElasticsearchRepository<GoodsInfo,Long>{}
