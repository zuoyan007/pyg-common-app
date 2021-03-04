package pyg.daheng.es.module.vo;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author ZhanSSH
 * @date 2021/2/20 16:25
 */
@Data
@Document(indexName = "store2",type = "base")
public class CreateIndexVo {

    @Field(type = FieldType.keyword)
    private String keyWord;


}
