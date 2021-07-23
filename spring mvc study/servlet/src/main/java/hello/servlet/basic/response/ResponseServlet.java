package hello.servlet.basic.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "responseServlet",urlPatterns = "/response-servlet")
public class ResponseServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //[Status]
        response.setStatus(HttpServletResponse.SC_OK);//SC_OK == 200
        //[Header]
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//쿠키 사용 막기
        response.setHeader("Pragma", "no-cache");//쿠키 사용 막기
        //response.sendRedirect("리다이렉트 할 주소");
//        response.setContentType("application/json"); Content-Type 지정
//        response.setCharacterEncoding("utf-8"); CharacterEncoding 지정

//        HelloData helloData = new HelloData(); Json 으로 보낼 객체 생성
//        helloData.setUsername("kim"); 내용 생성
//        helloData.setAge(20);
//
//        //{"username":"kim", "age":20} <- 이형태로 도달
//        String result = objectMapper.writeValueAsString(helloData); objectMapper.writeValueAsString 을 통해 Json 으로 변환
//        response.getWriter().write(result); <- 보내기


    }
}
