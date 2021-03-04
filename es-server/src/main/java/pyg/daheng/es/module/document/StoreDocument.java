package pyg.daheng.es.module.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import pyg.daheng.es.module.base.StoreBaseInfo;
import pyg.daheng.es.module.tags.StoreTags;

import java.util.List;

/**
 * es 门店文档
 * @author ZhanSSH
 * @date 2021/2/20 15:58
 */
@Data
//type类型 可以理解为表名
//indexName索引名称 可以理解为数据库名 必须为小写 不然会报org.elasticsearch.indices.InvalidIndexNameException异常
@Document(indexName = "store",type = "base")

public class StoreDocument {

    @Field(type = FieldType.keyword)
    private String id;

    /**
     * 基础信息
     */
    @Field(type = FieldType.Object)
    private StoreBaseInfo baseInfo;

    /**
     * 标签
     */
    @Field(type = FieldType.Nested)
    private List<StoreTags> tags;
}
