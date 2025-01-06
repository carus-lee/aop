package hello.aop.exam.aop;


import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class RetryAspect {

    //@Around("@annotation(hello.aop.exam.annotation.Retry)") //패키지 경로 맵핑
    @Around("@annotation(retry)") //파라미터 이름만 기술하여 맵핑 - 타입은 메서드(Retry retry)에서 맵핑
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        log.info("[retry] {} param retry={}", joinPoint.getSignature(), retry);
        int maxRetry = retry.value(); //retry 최대 카운트

        Exception exceptionHolder = null; //에러를 담을 holder (마지막에 터진 예외를 담기 위함)
        for (int retryCnt = 1; retryCnt <= maxRetry; retryCnt++) {
            try {
                log.info("[retry] try count={}/{}", retryCnt, maxRetry);
                return joinPoint.proceed();
            } catch (Exception e) {
                exceptionHolder = e;
            }
        }
        throw exceptionHolder;
    }
}
