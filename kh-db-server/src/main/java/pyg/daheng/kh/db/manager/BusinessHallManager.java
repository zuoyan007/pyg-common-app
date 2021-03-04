package pyg.daheng.kh.db.manager;

import pyg.daheng.common.constants.R;
import pyg.daheng.kh.db.dto.BusinessHallDto;
import pyg.daheng.kh.db.model.result.SelectBusinessHallR;


/**
 * @Description:
 * @ClassName: BusinessHallManager
 * @Author: ZhanSSH
 * @Date: 2021/1/26 11:28
 */
public interface BusinessHallManager {

    /**
     * 查询营业厅信息
     * @param dto
     * @return
     */
    R<SelectBusinessHallR> selectBusinessHall(BusinessHallDto dto);
}
