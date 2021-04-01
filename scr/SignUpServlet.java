import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import user.*;

/**
 * Servlet implementation class loginservlet
 */
@WebServlet("/loginservlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String JDBCUrl = "jdbc:mysql://localhost:3306";
        String username = "root";
        String password = "test";
        PrintWriter out = response.getWriter();
        try {
            System.out.println("\nConnecting to the SSD Database......");
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(JDBCUrl, username, password);
        }
        catch (Exception e) {
            System.out.println("\nAn error has occurred during the connection phase!  This is most likely due to your CLASSPATH being set wrong and the"
                    + "  classes unable to be found.  Otherwise the database itself may be down.  Try telneting to port 3306 and see if it is up!");
            e.printStackTrace();
            System.exit(0);
        }   
		
        String fname=request.getParameter("firstname");
        String lname=request.getParameter("lastname");
        String uname=request.getParameter("username");
        String pass=request.getParameter("password");
        String confirm_pass=request.getParameter("confirm_password");

		int id;
		boolean admin;
        if(pass.equals(confirm_pass)) {
	        try {
			     System.out.println("\nConnection Successful..... creating statement....");
		     	     stmt = con.createStatement();
			     //rs = stmt.executeQuery("INSERT INTO mydata.PROFILES VALUES(5,\'" + uname + "\');");//,\'" + pass + "\',\'" + fname + "\',\'" + lname +"\',\'Galway\', 0);");
		     	    PreparedStatement pstmt = con.prepareStatement("INSERT INTO mydata.profiles(USER_id,username,Passwrd,Firstname,Lastname) VALUES (?,?,?,?,?)");
					  pstmt.clearParameters();       // Clears any previous parameters
					  id = (int)(Math.random() * 1000000);
					  pstmt.setInt(1, id);
					  pstmt.setString(2, uname);
					  pstmt.setString(3, pass);
					  pstmt.setString(4, fname);
					  pstmt.setString(5, lname);
			
					  
					  pstmt.executeUpdate();
					  
					  User newUser = new User(id, uname, pass, fname, lname);
					  
					  HttpSession session = request.getSession();
						 //Add the user
					  session.setAttribute("theUser", newUser);
					// Set session expiry time
						session.setMaxInactiveInterval(600);
						request.getRequestDispatcher("home.jsp").include(request, response);
	        }
		    catch (SQLException e) {
			     System.out.println("\nAn error has occurred during the Statement/ResultSet phase.  Please check the syntax and study the Exception details!");
		            while (e != null) {
			         System.out.println(e.getMessage());
		                e = e.getNextException();
		            }
		    }
        }
		out.close();
	}

}
