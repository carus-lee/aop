package hello.aop.exam;

import hello.aop.exam.annotation.Trace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ExamRepository {

    private static int seq = 0;

    /**
     * 5번에 1번씩 에러 발생시킴
     */
    @Trace
    public String save(String itemId) {

        seq++;
        //log.info("itemId={}, curr Seq={}", itemId, seq);
        if (seq % 5 == 0) {
            throw new IllegalStateException("예외 발생");
        }
        return "ok";
    }

}
