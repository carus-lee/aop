package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CalllServioceV2 {

    //private final ApplicationContext applicationContext; // 너무 많은 기능이 포함되어 있음.
    private final ObjectProvider<CalllServioceV2> calllServioceProvoider;

    public CalllServioceV2(ObjectProvider<CalllServioceV2> calllServioceProvoider) {
        this.calllServioceProvoider = calllServioceProvoider;
    }

    public void external() {
        log.info("call external");
        CalllServioceV2 object = calllServioceProvoider.getObject();
        object.internal(); //외부 메서드 호출
    }

    public void internal() {
        log.info("call internal");
    }

}
