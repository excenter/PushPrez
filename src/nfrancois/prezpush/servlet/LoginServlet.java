package nfrancois.prezpush.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LoginServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		User currentUser = userService.getCurrentUser();
		if(currentUser == null){
			resp.getWriter().println("<a href='"+ userService.createLoginURL(req.getRequestURI())+ "'> LogIn </a>");			
		} else {
			resp.getWriter().println("Welcome, " + currentUser.getNickname());
			resp.getWriter().println("<a href='"+ userService.createLogoutURL(req.getRequestURI())+ "'> LogOut </a>");			
		}
	}

}
