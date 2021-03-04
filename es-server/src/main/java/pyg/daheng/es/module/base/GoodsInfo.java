package pyg.daheng.es.module.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @author ZhanSSH
 * @date 2021/2/22 14:58
 */
@Data
@Document(indexName = "testgoods",type = "goods")
@Builder
public class GoodsInfo implements Serializable{
    private Long id;
    private String name;
    private String description;
}
