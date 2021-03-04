package pyg.daheng.es.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pyg.daheng.common.constants.R;
import pyg.daheng.es.manager.ElasticSearchManager;
import pyg.daheng.es.module.vo.CreateIndexVo;
import pyg.daheng.es.module.vo.QueryContentByKeyWordsVo;

import java.util.List;

/**
 * @author ZhanSSH
 * @date 2021/2/4 17:31
 */
@RestController
@RequestMapping("/es")
@Api(tags = "ES搜索引擎相关API")
public class ElasticSearchController {

    @Autowired
    private ElasticSearchManager searchManager;

    @RequestMapping("/getContentByKeyWords")
    @ApiOperation(value = "根据关键字搜索方法")
    public R<List<Object>> getContentByKeyWords(@RequestBody @Validated QueryContentByKeyWordsVo vo){

        return searchManager.getContentByKeyWords(vo);
    }

    @RequestMapping("/createIndex")
    @ApiOperation(value = "创建索引")
    public R<String> createIndex(@RequestBody @Validated CreateIndexVo vo){

        return searchManager.createIndex(vo);
    }

}
