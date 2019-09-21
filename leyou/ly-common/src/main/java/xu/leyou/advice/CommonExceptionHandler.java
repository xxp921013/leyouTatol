package xu.leyou.advice;

import xu.leyou.enums.ExceptionEnums;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import xu.leyou.exception.LyException;
import xu.leyou.vo.ExceptionResult;

//通用异常处理器
//默认拦截所有controller
@ControllerAdvice
public class CommonExceptionHandler {
    //处理异常
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handleException(LyException e) {
        ExceptionEnums exceptionEnums = e.getExceptionEnums();
        return ResponseEntity.status(exceptionEnums.getCode()).body(new ExceptionResult(exceptionEnums));
    }
}
