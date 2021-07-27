package hello.servlet.web.springmvc.old;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component("/springmvc/old-controller")// 컴포넌트의 이름을 등록함, 스프링 빈의 이름으로 접속 가능 localhost:8080/springmvc/old-controller
public class OldController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return new ModelAndView("new-form");
        /**
         *  뷰 리졸버 없이 사용할 경우 오류
         *  application.properties 에 prefix 와 suffix 추가로 viewResolver 처럼 사용 가능
         */
    }
}
