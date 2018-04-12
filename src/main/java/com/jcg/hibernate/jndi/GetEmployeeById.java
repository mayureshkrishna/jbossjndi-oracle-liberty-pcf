package com.jcg.hibernate.jndi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;

import com.jcg.hibernate.jndi.model.Employee;

@WebServlet("/GetEmployeeById")
public class GetEmployeeById extends HttpServlet {
	
	private static Employee employeeObj;	
	private static final long serialVersionUID = 1L;
	public final Logger logger = Logger.getLogger(GetEmployeeById.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int employeeId = Integer.parseInt(request.getParameter("empId"));
		logger.info("Selected Employee Id?= "+ employeeId);

		SessionFactory sessionFactoryObj = (SessionFactory) request.getServletContext().getAttribute("SessionFactory");

		// Get Current Session For Performing The Transaction Queries
		Session sessionObj = sessionFactoryObj.getCurrentSession();

		// Begin Transaction
		Transaction transObj = sessionObj.beginTransaction();
		if(sessionObj.isOpen() && sessionObj.isConnected()) {
			employeeObj = (Employee) sessionObj.get(Employee.class, employeeId);
			transObj.commit();
		}

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		if(employeeObj != null) {
			out.print("<html><title>Hibernate Jndi Example</title><body><center><h2>Hibernate Jndi Example</h2></center><br /><h3>Employee Details</h3>");
			out.print("<table id='employeeTable' cellspacing=10 cellpadding=5><thead>");
			out.print("<th>Id</th>");
			out.print("<th>Name</th>");
			out.print("<th>Role</th>");

			out.print("</thead><tbody><tr>");
			out.print("<td>" + employeeId + "</td>");
			out.print("<td>" + employeeObj.getEmpName() + "</td>");
			out.print("<td>" + employeeObj.getEmpRole() + "</td>");
			out.print("</tr></tbody>");
			out.print("</table></body>");

			out.print("</html>");
		} else {
			out.print("<html><body><h2>No Employee Found with ID= "+ employeeId +"</h2></body></html>");
		}
	}
}