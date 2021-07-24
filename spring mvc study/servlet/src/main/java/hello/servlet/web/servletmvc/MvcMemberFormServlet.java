package hello.servlet.web.servletmvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp"; // jsp 가 있는 위치 지정
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);//컨트롤러에서 뷰 이동시 사용
        dispatcher.forward(request, response);  // redirect 가 아닌 foward 를 사용함
        //변수를 넘길 경우 request.setAttribute("변수명",넘길변수) 형태로 넘길 수 있음
    }
}
