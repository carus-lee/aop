package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        //메서드 정보 가져오기
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class); //hello 메서드는 String 반환 타입을 가지고 있다.
    }

    @Test
    void printMethod() {
        //메서드정보 출력
        //--> public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod={}", helloMethod);
    }

    @Test
    void exactMatch() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");//상기 패턴을 정확히 옮겨와서 맵핑

        //pointcut 표현식 매칭조건
        // - 접근제어자? : public
        // - 반환타입: String
        // - 선언타입?: hello.aop.member.MemberServiceImpl
        // - 메서드이름: hello
        // - 파라미터: (String)
        // - 예외?: 생략

        //결과 확인
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue(); //pointcut 매칭이 true가 맞는지 확인.
    }

    @Test
    void allMatch() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        //상기 표현식에서
        // 1) 필수값 3가지 (반환타입, 메서드이름, 파라미터)만 적용,
        // 2) 나머지(접근제어자, 선언타입, 예외)는 생략
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar1() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hel*(..))"); //메서드명 패턴
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar2() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *el*(..))"); //메서드명 앞뒤로 패턴
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchFalse() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* nono(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch1() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))"); //패키지 정확히 매칭
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch2() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.member.*.hello(..))"); //패키지 내 타입을 패턴 매칭
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch3() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))"); //메서드까지 패턴 매칭
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchFalse() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.*.*(..))"); //패키지 패턴 매칭 실패
        // - 매칭이 실패한 사유는 hello.aop.* 로 지정해도 하위 패키지까지 포함하지 않기 때문임.
        // - 즉, 패키지 매칭을 hello.aop 로 한다.
        // - 따라서, 하위 패키지(hello.aop.member) 까지 매칭하려면 ==> "hello.aop.." 로 매칭
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageMatchSubPackage1() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop..*.*(..))"); //하위 패키지까지 매칭
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchSubPackage2() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.memb*.*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeExactMatch() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        /**
         * 타입매칭 - 부모타입 허용 (다형성)
         *   - MemberService 처럼 부모타입을 선언해도 그 자식 타입이 매칭이 됨.
         */
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))"); //다형성 (부모타입 >= 자식타입)
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))"); //inertnal 메서드 존재
        Method internelMethod = MemberServiceImpl.class.getMethod("internel", String.class);
        Assertions.assertThat(pointcut.matches(internelMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchInteralSuperTypeMethodFalse() throws NoSuchMethodException {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        /**
         * 타입 매칭 - 부모타입에 있는 메서드만 허용 ==> "부모타입에 선언한 메서드"가 "자식타입"에도 있어야 성공
         *   - MemberService(부모 타입)    : internal 메서드 (X)
         *   - MemberServiceImpl(자식 타입): internal 메서드 (O)
         */
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))"); //MemberService에는 internal 메서드 없음
        Method internelMethod = MemberServiceImpl.class.getMethod("internel", String.class);
        Assertions.assertThat(pointcut.matches(internelMethod, MemberServiceImpl.class)).isFalse();
    }

    //String 타입의 파라미터 허용 - 심플하게 생략가능한 표현식으로 적용
    //(String)
    @Test
    void argsMatch() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *(String))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    //파라미터 없음
    //()
    @Test
    void argsMatchNoArgsFalse() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *())");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    //정확히 1개의 파라미터만 허용, 모든 타입 허용
    //(Xxx)
    @Test
    void argsOnlyOneAndStartMatch() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *(*))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    //파라미터 개수 무제한, 모든 타입 허용
    //(), (Xxx), (Xxx, Xxx)
    @Test
    void argsMatchAll() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    //String 타입으로 "시작", 파라미터 개수 무제한, 모든 타입 허용
    //(String), (String, Xxx), (String, Xxx, Xxx)
    @Test
    void argsFirstStringMatch() {
        //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *(String, ..))"); //..의 의미는 0~N
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }




}
