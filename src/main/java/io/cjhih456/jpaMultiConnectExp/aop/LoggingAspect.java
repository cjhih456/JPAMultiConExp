package io.cjhih456.jpaMultiConnectExp.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cjhih456.jpaMultiConnectExp.common.ParamConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@Slf4j
@Aspect
@Service
public class LoggingAspect {

    /**
     * method 실행 시간 확인, @Around : 핵심업무 전후에 자동호출, ProceedingJoinPoint
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(* io.cjhih456.jpaMultiConnectExp.service..*.*(..))")
    public Object timeLog(ProceedingJoinPoint pjp) throws Throwable {
        // 핵심업무 실행 전
        long start = System.currentTimeMillis();
        // 핵심업무 실행
        Object result = pjp.proceed();
        // 핵심업무 실행 후
        long end = System.currentTimeMillis();
        // 핵심업무 실행시간 연산
        log.info(ParamConstants.EXECUTE_START + (end - start) + ParamConstants.EXECUTE_END);
        log.info(ParamConstants.AROUND_LINE);
        return result;
    }

    /**
     * 서비스의 메소드가 실행된 후 결과를 뿌려준다
     *
     * @param joinPoint
     * @param result
     * @throws JsonProcessingException
     */
    @AfterReturning(
            pointcut = "execution(* io.cjhih456.jpaMultiConnectExp.service..*.*(..))", returning = ParamConstants.RESULT)
    public void logAfterReturning(JoinPoint joinPoint, Object result) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        log.info(ParamConstants.METHOD_RESULT);
        log.info(ParamConstants.AFTER_RETURNING);
        log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
        log.info(ParamConstants.LINE);
    }

}
