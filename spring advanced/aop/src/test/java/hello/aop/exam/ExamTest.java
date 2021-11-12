package hello.aop.exam;

import hello.aop.exam.aspect.RetryAspect;
import hello.aop.exam.aspect.TraceAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Import({TraceAspect.class, RetryAspect.class})
@SpringBootTest
class ExamTest {
    @Autowired
    ExamService examService;

    @Test
    void test(){
        for(int i=0; i<5;i++){
            log.info(Integer.toString(i));
            examService.request("data" + i);
        }
    }

}
