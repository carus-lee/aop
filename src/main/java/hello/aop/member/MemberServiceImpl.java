package hello.aop.member;

import hello.aop.member.anntation.ClassAop;
import hello.aop.member.anntation.MethodAop;
import org.springframework.stereotype.Component;

@ClassAop
@Component
public class MemberServiceImpl implements MemberService {

    @Override
    @MethodAop("test value")
    public String hello(String param) {
        return "ok";
    }

    public String internel(String param) {
        return "ok";
    }
}
