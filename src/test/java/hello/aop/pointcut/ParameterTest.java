package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.anntation.ClassAop;
import hello.aop.member.anntation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(ParameterTest.ParameterAspect.class)
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {}

        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{}, arg1={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }

        /** args 포인트컷 지시자 활용 - 상기 예제보다 파라미터 처리가 유용
         *  - 메서드에서 전달받는 파라미터의 타입을 지정
         */
        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{}, arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        @Before(("allMember() && args(arg,..)")) //@Before는 반환타입이 필수가 아님.
        public void logArgs3(String arg) {
            log.info("[logArgs3] arg={}", arg);
        }

        /** this vs target
         *  - this  : 현재 객체(프록시 - 스프링 컨테이너에 등록된 객체)
         *  - target: 대상 객체(프록시가 호출하는 실제 대상 객체 - MemberServiceImpl)
         */
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[thisArgs]{}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[targetArgs]{}, obj={}", joinPoint.getSignature(), obj.getClass());
        }

        /** @target, @within */
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[atTarget]{}, obj={}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[atWithin]{}, obj={}", joinPoint.getSignature(), annotation);
        }

        @Before("allMember() && @annotation(annotation)") //@annotation은 메서드의 파라미터의 값을 꺼내올 수 있다.
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[atAnnotation]{}, annotationValue={}", joinPoint.getSignature(), annotation.value());
        }

    }

}
