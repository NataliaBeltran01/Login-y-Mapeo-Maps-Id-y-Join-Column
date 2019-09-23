package roseindia.requestDispatcherExample;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name= "RequestForwardExample",
		urlPatterns= {"/RequestForwardExample"})
public class RequestForwardExample extends HttpServlet
{
 public void doGet(HttpServletRequest request, HttpServletResponse response)
  throws IOException, ServletException
  {
	 response.setContentType("text/html");
	 PrintWriter out= response.getWriter();
	 	 
	 String name= request.getParameter("name");
	 String psw= request.getParameter("psw");
	 
	 String className= "com.mysql.jdbc.Driver";
	 String url= "jdbc:mysql://192.168.10.13/data";
	 String user= "root";
	 String password= "root";
	 
	 Connection con;
	 PreparedStatement ps;
	 ResultSet rs;
	 
	 try
	 {
		 Class.forName(className);
		 con= DriverManager.getConnection(url, user, password);
		 
		 String sql= "SELECT name FROM login WHERE name = '"+name+"' AND password = '"+psw+"'";
		 ps= con.prepareStatement(sql);
		 rs= ps.executeQuery();
			if(rs.next())
			{
				HttpSession session= request.getSession();
				String n= rs.getString("name");
				session.setAttribute("name", n);
				
				ServletContext context= getServletContext();
				RequestDispatcher rd= context.getRequestDispatcher("/ForwardTesterServlet");
				rd.forward(request, response);
			}
			else
			{
				ServletContext context= getServletContext();
				RequestDispatcher rd= context.getRequestDispatcher("/login1.html");
				out.println("<font color=red>invalid user name or password</font>");
				rd.include(request, response);
			}
			
			con.close();
			ps.close();
			rs.close();		 
	 }
	 catch(ClassNotFoundException cx)
	 {
		 out.println();
	 }
	 catch(SQLException sx)
	 {
		 out.println();
	 }
  }
}
