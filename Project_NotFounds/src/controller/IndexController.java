package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import bean.User;
import dao.UserDao;
import sha.SHA;

/**
 * Servlet implementation class IndexController
 */
public class IndexController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(IndexController.class);

	public IndexController() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String action = request.getParameter("action");
		
		if (action == null || action.length() == 0) {
			RequestDispatcher rd = request.getRequestDispatcher("/view/index.jsp");
			rd.forward(request, response);
		}else if ("facebook".equals(action)) {
			input_Session(request, request.getParameter("email"), request.getParameter("name"));
			response.sendRedirect("/NotFound/main.do");
		}else if ("logout".equals(action)) {
			session.invalidate();
			response.sendRedirect("/NotFound/view/index.jsp");
		} 
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<Map<String, String>> list = null;
		String action = request.getParameter("action");
		UserDao userdao = new UserDao();
	
		if (action == null || action.length() == 0) {
			userdao.jsback(response);
		} else if (action.equals("join")) {
			String pass  = request.getParameter("joinpass");
			String bcr_pass = BCrypt.hashpw(pass, BCrypt.gensalt());
			User user = new User(request.getParameter("joinid"), bcr_pass, request.getParameter("joinname"));
			userdao.user_insert(user);
			input_Session(request, request.getParameter("joinid"), request.getParameter("joinname"));
			response.sendRedirect("/NotFound/main.do");
		} else if (action.equals("login")) {
			// login ..
			String email = request.getParameter("userid");
			String pass = request.getParameter("userpd");
			list = userdao.user_login(email);
			boolean passcheck =  BCrypt.checkpw(pass,list.get(0).get("pass"));
			
			if (list.size() != 0 && passcheck) {
				input_Session(request, email, list.get(0).get("name"));
				log.info("login info" + list.get(0));
				response.sendRedirect("/NotFound/main.do");
			} else {
				userdao.jsback(response);
			}
		}  else {
			userdao.jsback(response);
		}
	}

	private void input_Session(HttpServletRequest request, String email, String name) {
		HttpSession session = request.getSession();
		session.setAttribute("logincheck", "login");
		session.setAttribute("id", email);
		session.setAttribute("name", name);
	}
	


}
