package hello.servlet.basic.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name="requestServlet",urlPatterns="/request-servlet")
public class RequestServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException {

//        //[StartLine]
//        System.out.println("request.getMethod() = " + request.getMethod()); //GET
//        System.out.println("request.getProtocal() = " + request.getProtocol()); // HTTP / 1.1
//        System.out.println("request.getScheme() = " + request.getScheme()); //http// http://localhost:8080/request-header
//        System.out.println("request.getRequestURL() = " + request.getRequestURL());// /request-test
//        System.out.println("request.getRequestURI() = " + request.getRequestURI());//username=hi
//        System.out.println("request.getQueryString() = " + request.getQueryString());
//        System.out.println("request.isSecure() = " + request.isSecure()); //https 사용 유무
//        System.out.println();
//
//        //[Headers]
//        request.getHeaderNames().asIterator()
//                .forEachRemaining(headerName-> System.out.println("headerName = " + headerName));
//        System.out.println();
//        //[Parameter]
//        request.getParameterNames().asIterator()
//                .forEachRemaining(paraName-> System.out.println(paraName + "="+ request.getParameter(paraName)));
//        //[이름이 같은 파라미터 조회]
//        String[] usernames = request.getParameterValues("username");
//        for (String s : usernames) {
//            System.out.println("username = " + s);
//        }


        //[Body] HelloData는 username과 age를 가지는 클래스
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        System.out.println("messageBody = " + messageBody);

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        System.out.println("helloData.getUsername() = " + helloData.getUsername());
        System.out.println("helloData.getAge() = " + helloData.getAge());

    }

}
