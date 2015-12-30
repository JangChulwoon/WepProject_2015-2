package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DB_set;
import dao.UserDao;

/**
 * Servlet implementation class JoinController
 */
@WebServlet("/JoinController")
public class IdcheckController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IdcheckController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String id = request.getParameter("id");
		PrintWriter out = response.getWriter();
		if (!(id.length() == 0 && id == null)) {
			// 여기서 디비를 가져와서 실행
			System.out.println(id);
			UserDao userdao = new UserDao();
			DB_set dbset = new DB_set();
			Connection conn = dbset.dbinit(); // 디비 커넥션 연결
			if (userdao.idcheck(conn, id)) {
				request.setAttribute("check", 1);
			} else {
				request.setAttribute("check", 2);
			}
			RequestDispatcher rd = request.getRequestDispatcher("/view/check/Join_idcheck.jsp");
			rd.forward(request, response);
		} else {
			out.println("<script>");
			out.println("alert('what ? nope!');");
			out.println("window.close();");
			out.println("</script>");
		}
	}

}
