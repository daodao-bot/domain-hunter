package run.ice.fun.domain.hunter.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import run.ice.lib.core.error.AppError;
import run.ice.lib.core.error.AppException;
import run.ice.lib.core.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DaoDao
 */
@Slf4j
@RestController
@ControllerAdvice
public class AppExceptionHandler {

    /**
     * 所有异常
     *
     * @param e Exception
     * @return Response
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Response<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        if (e instanceof AppException exception) {
            return new Response<>(exception);
        }
        return new Response<>(new AppException(e));
    }

    /**
     * 400 "Bad Request
     *
     * @param e MethodArgumentNotValidException
     * @return Response
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        List<String> messages = new ArrayList<>();
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> objectErrors = bindingResult.getAllErrors();
        for (ObjectError objectError : objectErrors) {
            if (objectError instanceof FieldError fieldError) {
                messages.add("[" + fieldError.getField() + "]" + ":" + "'" + fieldError.getRejectedValue() + "'" + ":" + fieldError.getDefaultMessage());
            } else {
                messages.add(objectError.toString());
            }
        }
        return new Response<>(AppError.PARAM_ERROR.code, String.join(";", messages));
    }

}
