package org.zerock.b01.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/*외부에서 아작스로 댓글 등록 기능 호출 시 500에러(서버에러)발생한다면
호출한 측에서 서버 문제라 생각할 것이고 데이터에 문제라고 생각하지 않을 것
* 댓글 번호가 없는 곳에다가 입력 하려고 했을때 예외처리 해주는 곳
 클라이언트에 서버의 문제가 아니라 데이터의 문제가 있다고 전송하기 위해서 @rest-controlleradvice를 이용해서
 사용자에게 예외 메세지를 전송하도록 구성
* */
@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleBindException(BindException e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        if(e.hasErrors()){

            BindingResult bindingResult = e.getBindingResult();

            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }

        return ResponseEntity.badRequest().body(errorMap);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleFKException(Exception e){
        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time",""+System.currentTimeMillis());
        errorMap.put("msg", "constraint 실패"); //메세지 띄워주기
        return ResponseEntity.badRequest().body(errorMap);
    }

    //댓글 번호 예외처리 ( 없는 번호 댓글 삭제시 예외처리)
    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleNoSuchElement(Exception e){
        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time","" + System.currentTimeMillis());
        errorMap.put("msg","No Such Element Exception 댓글 번호 에러");
        return ResponseEntity.badRequest().body(errorMap);
    }

}
