package hello.aop.exam;

import hello.aop.exam.aop.CheckTimeAspect;
import hello.aop.exam.aop.RetryAspect;
import hello.aop.exam.aop.TraceAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import({TraceAspect.class, RetryAspect.class, CheckTimeAspect.class})
public class ExamTest {

    @Autowired
    ExamService examService;

    @Test
    void test1() {
        for (int i = 0; i < 5; i++) {
            log.info("client request i={}", i);
            examService.request("data" + i);
        }
    }
}
