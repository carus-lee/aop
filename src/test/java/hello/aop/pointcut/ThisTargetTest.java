package hello.aop.pointcut;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * application.properties
 * - spring.aop.proxy-target-class=true   --> CGLIB 프록시 (default)
 * - spring.aop.proxy-target-class=false  --> JDK 동적 프록시
 */
@Slf4j
@SpringBootTest(properties = "spring.aop.proxy-target-class=false") //JDK 동적 프록시
//@SpringBootTest(properties = "spring.aop.proxy-target-class=true") //CGLIB 프록시
@Import(ThisTargetTest.ThisTargetAspect.class)
public class ThisTargetTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ThisTargetAspect {

        //부모타입 허용
        @Around("this(hello.aop.member.MemberService)") //[this]MemberService 지정
        public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        //부모타입 허용
        @Around("target(hello.aop.member.MemberService)") //[target]MemberService 지정
        public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }


        @Around("this(hello.aop.member.MemberServiceImpl)") //[this]MemberServiceImpl 지정
        public Object doThisImpl(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("target(hello.aop.member.MemberServiceImpl)") //[target]MemberServiceImpl 지정
        public Object doTargetImpl(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }


    }
}

