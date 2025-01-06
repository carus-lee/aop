package hello.aop.exam.aop;

import hello.aop.exam.annotation.CheckTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
public class CheckTimeAspect {

    @Around("@annotation(checkTime)")
    public void checkTimer(ProceedingJoinPoint joinPoint, CheckTime checkTime) throws Throwable {

        int mills = checkTime.value();
        int maxMills = 4000;

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        joinPoint.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();

        if (totalTimeMillis <= mills) {
            log.info("methodName: {}, 실행시간 = {}ms", methodName, totalTimeMillis);
        } else if (totalTimeMillis <= maxMills) {
            log.warn("methodName: {}, 실행시간 = {}ms", methodName, totalTimeMillis);
        } else {
            log.error("methodName: {}, 실행시간 = {}ms", methodName, totalTimeMillis);
        }
    }
}
