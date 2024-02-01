package api.users.srpingbootHW.aop;


import api.users.srpingbootHW.exception.ExceptionResponse;
import api.users.srpingbootHW.exception.ProjectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {


    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> projectExceptionHandler(ProjectException projectException) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setInfo(projectException.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
