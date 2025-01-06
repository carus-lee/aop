package hello.aop.exam;

import hello.aop.exam.annotation.CheckTime;
import hello.aop.exam.annotation.Retry;
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
    @Retry(value = 4) //retry 최대 횟수 지정
    @CheckTime(value = 2000)
    public String save(String itemId) {

        seq++;
        //log.info("itemId={}, curr Seq={}", itemId, seq);
        if (seq % 5 == 0) {
            throw new IllegalStateException("예외 발생");
        }
        sleep(); //@CheckTime 테스트를 위한 sleep (랜덤)
        return "ok";
    }

    private void sleep() {
        int[] ints = {1000, 2000,3000,4000,5000};

        double random= Math.random();
        int num = (int) Math.round(random * (ints.length-1));

        System.out.println(ints[num]);

        try {
            Thread.sleep(ints[num]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
