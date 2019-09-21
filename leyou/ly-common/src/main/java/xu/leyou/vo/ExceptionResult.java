package xu.leyou.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import xu.leyou.enums.ExceptionEnums;

@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnums e) {
        this.status = e.getCode();
        this.message = e.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
