package xu.leyou.exception;

import xu.leyou.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LyException extends RuntimeException {
    private ExceptionEnums exceptionEnums;

}
