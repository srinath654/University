package com.ars.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ars.dao.CourseDao;
import com.ars.model.Course;
import com.ars.model.Enquiry;
import com.ars.model.Student;

public class ViewDepartmentsServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)  
			throws ServletException, IOException {  

		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  
		String viewType=request.getParameter("viewType");
		HttpSession session = request.getSession(false);
		String finalView="";
		RequestDispatcher rd=null;
		CourseDao courseDao = new CourseDao();
	
		if(viewType!=null && ("editdepartment".equalsIgnoreCase(viewType) || "deletedepartment".equalsIgnoreCase(viewType))) {
			
			String searchString =request.getParameter("deptname");
			Course course = new Course();
			if(searchString!=null && !searchString.isEmpty()) {
				course.setCourseName(searchString);
			}
			List<Course> departmentsList = courseDao.getDepartmentDetails(course);
			
			if(departmentsList.size()==0) {
				request.setAttribute("errorMsg", "No Courses to display!");
			}
			request.setAttribute("departmentsList",departmentsList);
			session.setAttribute("departmentsList",departmentsList);
			if("editdepartment".equalsIgnoreCase(viewType)) {
			finalView="modifydepartments.jsp";
			}else {
				finalView="deletedepartments.jsp";
			}
			
		}else if(viewType!=null && ("viewstudents".equalsIgnoreCase(viewType))) {
			
			List<Student> studentsList = courseDao.getStudentDetails();
			
			if(studentsList.size()==0) {
				request.setAttribute("errorMsg", "No Students to display!");
			}
			request.setAttribute("studentsList",studentsList);
			session.setAttribute("studentsList",studentsList);
			
				finalView="viewstudents.jsp";
			
		}else if(viewType!=null && ("viewstudentdetails".equalsIgnoreCase(viewType))) {
			List<Student> list = (List<Student>) session.getAttribute("studentsList");
			List<Student> newList = new ArrayList<Student>();
			String type = request.getParameter("type");
			for(Student item : list) {
				if(item.getStatus().equalsIgnoreCase(type)) {
					newList.add(item);
				}
			}
			request.setAttribute("studentsList",newList);
			finalView="viewstudents.jsp";
		}else if(viewType!=null && ("editdepartmentdetails".equalsIgnoreCase(viewType))) {
			List<Course> list = (List<Course>) session.getAttribute("departmentsList");
			int courseId =Integer.valueOf(request.getParameter("selecteddepartment"));
			for(Course item : list) {
				if(item.getCourseId()==courseId) {
					request.setAttribute("course", item);
					if("editdepartmentdetails".equalsIgnoreCase(viewType)) {
					request.setAttribute("mode", "edit");
					}else {
						request.setAttribute("mode", "delete");
					}
					break;
				}
			}			
			finalView="adddepartments.jsp";
		}else if(viewType!=null && ("editstudent".equalsIgnoreCase(viewType))) {
			List<Student> list = (List<Student>) session.getAttribute("studentsList");
			int studentId =Integer.valueOf(request.getParameter("selectedstudent"));
			for(Student item : list) {
				if(item.getStudentId()==studentId) {
					request.setAttribute("student", item);
					if("editstudent".equalsIgnoreCase(viewType)) {
					request.setAttribute("mode", "edit");
					}else {
						request.setAttribute("mode", "delete");
					}
					break;
				}
			}			
		finalView="editstudentdetails.jsp";
		}else if(viewType!=null && "updatedepartment".equalsIgnoreCase(viewType)) {
			Course c = new Course();
			c.setCourseId(Integer.valueOf(request.getParameter("cid")));
			c.setCourseName(request.getParameter("cname1"));
			c.setCourseCode(request.getParameter("ccode1"));
			c.setAvailable(request.getParameter("avail1"));
			c.setCapacity(Integer.valueOf(request.getParameter("capacity1")));
			c.setProfessor(request.getParameter("prof1"));
			c.setStatus("1");
			c.setType(request.getParameter("dtype"));
			c.setDeptName(request.getParameter("deptname"));
			
			try {
				if(!courseDao.updateDepartmentDetails(c)) {
					request.setAttribute("errorMsg", "Failed to update course details. Please try again!");
				}else {
					request.setAttribute("errorMsg", "course Details Updated Successfully!");
				}
			}catch(Exception e) {
				request.setAttribute("errorMsg", "Failed to update course details. Please try again!");
			}
			finalView="adddepartments.jsp";
		}else if(viewType!=null && "updatestudent".equalsIgnoreCase(viewType)) {
			
			Student user = new Student();
			int studentId =Integer.valueOf(request.getParameter("selectedstudent"));
			user.setStudentId(studentId);
			user.setFirstName(request.getParameter("firstname"));
			user.setMiddleName(request.getParameter("middlename"));
			user.setLastName(request.getParameter("lastname"));
			user.setContactNo(request.getParameter("mobileno"));
			user.setEmailId(request.getParameter("emailid"));
			user.setStatus(request.getParameter("status"));
			user.setMmr(request.getParameter("mmr"));
			user.setTbTest(request.getParameter("tb"));
			user.setMailed(request.getParameter("mailed"));

			try {
				if(!courseDao.updateStudentDetails(user)) {
					request.setAttribute("errorMsg", "Failed to update student details. Please try again!");
				}else {
					request.setAttribute("errorMsg", "Student Details Updated Successfully!");
				}
			}catch(Exception e) {
				request.setAttribute("errorMsg", "Failed to update student details. Please try again!");
			}
			List<Student> studentsList = courseDao.getStudentDetails();
			
			if(studentsList.size()==0) {
				request.setAttribute("errorMsg", "No Students to display!");
			}
			request.setAttribute("studentsList",studentsList);
			session.setAttribute("studentsList",studentsList);
			
				finalView="viewstudents.jsp";
		}else if(viewType!=null && "deleteDepartmentdetails".equalsIgnoreCase(viewType)) {
			Course c = new Course();
			c.setCourseId(Integer.valueOf(request.getParameter("selecteddepartment")));
					
			try {
				if(!courseDao.deleteCourseDetails(c)) {
					request.setAttribute("errorMsg", "Failed to delete Course details. Please try again!");
				}else {
					request.setAttribute("errorMsg", "Course Details Deleted Successfully!");
				}
			}catch(Exception e) {
				request.setAttribute("errorMsg", "Failed to  delete course  details. Please try again!");
			}
			
			finalView="adddepartments.jsp";
		}else if(viewType!=null && "adddepartment".equalsIgnoreCase(viewType)) {
			
			List<Course> courseList = new ArrayList<Course>();
			String deptName = request.getParameter("deptname");
			String deptType = request.getParameter("dtype");
			for(int i=1;i<=5;i++) {
				Course c = new Course();
				if(request.getParameter("cname"+i)!=null && request.getParameter("cname"+i).trim().length()>0) {
					c.setCourseName(request.getParameter("cname"+i));
					c.setCourseCode(request.getParameter("ccode"+i));
					c.setAvailable(request.getParameter("avail"+i));
					c.setCapacity(Integer.valueOf(request.getParameter("capacity"+i)));
					c.setProfessor(request.getParameter("prof"+i));
					c.setStatus("1");
					c.setType(deptType);
					c.setDeptName(deptName);
					
					courseList.add(c);
				}
			}
				try {
				if(!courseDao.insertDepartmentDetails(courseList)) {
					request.setAttribute("errorMsg", "Failed to insert course details. Please try again!");
				}else {
					request.setAttribute("errorMsg", "New Course Details Added Successfully!");
				}
			}catch(Exception e) {
				request.setAttribute("errorMsg", "Failed to insert course details. Please try again!");
			}
			
			finalView="adddepartments.jsp";
		}
		
		
		 rd=request.getRequestDispatcher(finalView);  
			rd.forward(request,response);
	}  
}  