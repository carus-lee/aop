package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

@Slf4j
public class ArgsTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        //메서드 정보 가져오기
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class); //hello 메서드는 String 반환 타입을 가지고 있다.
    }

    /**
     * 테스트를 위한 포인트컷 객체 반환 - 표현식은 파라미터로 받아서 세팅
     * @param expression
     * @return
     */
    private AspectJExpressionPointcut pointcut(String expression) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return pointcut;
    }

    @Test
    void args() {
        //공통적으로 hello(String)와 매칭
        Assertions.assertThat(this.pointcut("args(String)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();//hello()는 String 파라미터를 받으므로 당연히 true
        Assertions.assertThat(this.pointcut("args(Object)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();//부모타입(Object)도 허용
        Assertions.assertThat(this.pointcut("args()")
                .matches(helloMethod, MemberServiceImpl.class)).isFalse();//args()는 파라미터 X. hello()는 String 파라미터를 받기에 false
        Assertions.assertThat(this.pointcut("args(..)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();//args(..)는 파라미터 무제한(타입포함) 허용. 따라서 true
        Assertions.assertThat(this.pointcut("args(*)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();//args(*)는 정확히 1개의 파라미터만 허용. hello()는 String 파라미터 1개라 true
        Assertions.assertThat(this.pointcut("args(String, ..)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();//args(String, ..)는 String파라미터로 시작하고 이후는 무제한 파라미터 허용 표현식, hello()는 String 파라미터이므로 true

    }

    /**
     * execution(* *(java.io.Serializable)) : 메서드의 "시그니처"로 판단 ==> 정적
     * args(java.io.Serializable)           : 런타임에 "전달된 인수"로 판단 ==> 동적
     */
    @Test
    void argsVsExecution() {
        //args
        Assertions.assertThat(this.pointcut("args(String)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue(); //String true
        Assertions.assertThat(this.pointcut("args(java.io.Serializable)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue(); //String은 Serializable은 구현하고 있기에 true
        Assertions.assertThat(this.pointcut("args(Object)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue(); //최상위타입(Object) true

        //execution
        Assertions.assertThat(this.pointcut("execution(* *(String))")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue(); //String true
        Assertions.assertThat(this.pointcut("execution(* *(java.io.Serializable))")
                .matches(helloMethod, MemberServiceImpl.class)).isFalse(); //execution은 메서드의 시그니처로 판단하므로 정확히 일치해야 됨, 따라서, Serializable을 구현했더라도 false
        Assertions.assertThat(this.pointcut("execution(* *(Object))")
                .matches(helloMethod, MemberServiceImpl.class)).isFalse(); //execution은 메서드의 시그니처로 판단하므로 정확히 일치해야 됨, 따라서, 최상위타입(Object)라도 false
    }

}
