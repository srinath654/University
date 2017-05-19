package com.ars.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ars.dao.ProfileDao;
import com.ars.model.Student;
import com.ars.model.User;

public class ProfileServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException {  

		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  
		String viewType=request.getParameter("viewType");
		HttpSession session = request.getSession(false);
		String finalView="";
		RequestDispatcher rd=null;
		ProfileDao profileDao = new ProfileDao();
		
	 if(viewType!=null && "register".equalsIgnoreCase(viewType)) {
			Student user = new Student();
			user.setUserName(request.getParameter("username")); 
			user.setNewPass(request.getParameter("userpass"));
			user.setSecurityQuestion(request.getParameter("securityquestion"));  
			user.setSecurityAnswer(request.getParameter("securityanswer")); 
			user.setFirstName(request.getParameter("firstname"));
			user.setMiddleName(request.getParameter("middlename"));
			user.setLastName(request.getParameter("lastname"));
			user.setContactNo(request.getParameter("mobileno"));
			user.setEmailId(request.getParameter("emailid"));
			user.setDeptName(request.getParameter("deptname"));
			user.setMmr(request.getParameter("mmr"));
			user.setTbTest(request.getParameter("tb"));
			user.setMailed(request.getParameter("mailed"));
			user.setUserType(request.getParameter("usertype"));
			if(!user.getUserType().equals("Domestic")) {
			user.setStatus("Pending");
			}else {
				user.setStatus("Approved");
			}
			try {
				if(!profileDao.insertProfileDetails(user)) {
					request.setAttribute("errorMsg", "Failed to register Student. Please try again!");
				}else {
				request.setAttribute("errorMsg", "Student Registered Successfully. Please log in!"); 
				}
			}catch(Exception e) {
				request.setAttribute("errorMsg", "Failed to register Student. Please try again!");
			}
			finalView="login.jsp";
		}else if(viewType!=null && "viewprofile".equalsIgnoreCase(viewType)) {
			
			try {
				Student user = (Student) session.getAttribute("user");
				//User user = profileDao.getProfileDetails(userName);
				if(user == null ) {
					request.setAttribute("errorMsg", "Failed to get user details. Please try again!");
				}else {
					request.setAttribute("user",user);
				}
			}catch(Exception e) {
				request.setAttribute("errorMsg", "Failed to get user details. Please try again!");
			}
			finalView="manageprofile.jsp";
		}else if(viewType!=null && "updateprofile".equalsIgnoreCase(viewType)) {
			Student user = new Student();
			user.setStudentId(Integer.valueOf(request.getParameter("studentid")));
			user.setUserName(request.getParameter("username")); 
			user.setOldUserName(request.getParameter("oldusername"));
			user.setNewPass(request.getParameter("userpass"));
			user.setSecurityQuestion(request.getParameter("securityquestion"));  
			user.setSecurityAnswer(request.getParameter("securityanswer")); 
			user.setFirstName(request.getParameter("firstname"));
			user.setMiddleName(request.getParameter("middlename"));
			user.setLastName(request.getParameter("lastname"));
			user.setContactNo(request.getParameter("mobileno"));
			user.setEmailId(request.getParameter("emailid"));
			user.setDeptName(request.getParameter("deptname"));
			user.setMmr(request.getParameter("mmr"));
			user.setTbTest(request.getParameter("tb"));
			user.setMailed(request.getParameter("mailed"));
			user.setUserType(request.getParameter("usertype"));

			try {
				Student user1 = null;
				if(!profileDao.updateProfileDetails(user)) {
					request.setAttribute("errorMsg", "Failed to update profile. Please try again!");
					user1 = profileDao.getProfileDetails(user.getOldUserName());
				}else {
				request.setAttribute("errorMsg", "User profile updated Successfully. ");
				user1 = profileDao.getProfileDetails(user.getUserName());
				}
				
				
					request.setAttribute("user",user1);

				
			}catch(Exception e) {
				request.setAttribute("errorMsg", "Failed to update profile. Please try again!");
			}
			
			finalView="manageprofile.jsp";
		}else if(viewType!=null && "forgotpassword".equalsIgnoreCase(viewType)) {
			User user = new User();
			user.setUserName(request.getParameter("username")); 
			user.setSecurityQuestion(request.getParameter("securityquestion"));  
			user.setSecurityAnswer(request.getParameter("securityanswer")); 
			
			try {
				if(!profileDao.forgotPassword(user)) {
					request.setAttribute("errorMsg", "Incorrect input!");
					finalView="forgotpassword.jsp";
				}else {
				request.setAttribute("username", user.getUserName());
				finalView="newpassword.jsp";
				}
			}catch(Exception e) {
				request.setAttribute("errorMsg", "Incorrect input!");
				finalView="forgotpassword.jsp";
			}
			
		}else if(viewType!=null && "newpassword".equalsIgnoreCase(viewType)) {
			User user = new User();
			user.setUserName(request.getParameter("username")); 
			user.setNewPass(request.getParameter("newpass"));  
			user.setConfirmPass(request.getParameter("confirmpass")); 
			
			try {
				if(!user.getNewPass().isEmpty() && !user.getConfirmPass().isEmpty() && !user.getNewPass().equals(user.getConfirmPass())) {
					request.setAttribute("errorMsg", "New Password and Confirm Password do not match!");
				}
			else if(!profileDao.updatePassword(user)) {
					request.setAttribute("errorMsg", "Failed to update password!");
				}else {
					request.setAttribute("errorMsg", "Password updated successfully. Try logging in again!");
				}
				request.setAttribute("username", user.getUserName());
			}catch(Exception e) {
				request.setAttribute("errorMsg", "Failed to update password!");
			}
			
			finalView="newpassword.jsp";
		}
		
		
		 rd=request.getRequestDispatcher(finalView);  
			rd.forward(request,response);
	}  
}  