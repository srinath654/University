package com.ars.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ars.model.Course;
import com.ars.model.Student;
import com.ars.model.User;

public class CourseDao {
	
	Connection conn = null;
	PreparedStatement pst = null;
	Statement stmt = null;
	ResultSet rs = null;
	User user=null;
	String url = "jdbc:mysql://localhost:3306/";
	String dbName = "university";
	String driver = "com.mysql.jdbc.Driver";
	String userName = "root";
	String password = "admin";
	
	public List<Course> getDepartmentDetails(Course course) {	
		List<Course> departmentsList = new ArrayList<Course>();
		try {
			
			Class.forName(driver).newInstance();
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			String getCourseDetailsQuery = "select * from course_details"; 
			
			if(course.getType()==null || course.getType().isEmpty()) {
				course.setType("");
				getCourseDetailsQuery = getCourseDetailsQuery + " where 1=1 ";
			}else {
				getCourseDetailsQuery = getCourseDetailsQuery + " where type ='" + course.getType()+"'";
			}
			
			
			if(course.getDeptName()!=null && !course.getDeptName().isEmpty()) {
				getCourseDetailsQuery = getCourseDetailsQuery + " and dept_name like '%" + course.getDeptName()+"%'";
			}
			
			
			if(course.getCourseName()!=null && !course.getCourseName().isEmpty()) {
				getCourseDetailsQuery = getCourseDetailsQuery + " and course_name like '%" + course.getCourseName()+"%'";
			}
			
			if(course.getAvailable()!=null && !course.getAvailable().isEmpty()) {
				getCourseDetailsQuery = getCourseDetailsQuery + " and available='" + course.getAvailable()+"'";
			}
			
			if(course.getCourseCode()!=null && !course.getCourseCode().isEmpty()) {
				getCourseDetailsQuery = getCourseDetailsQuery + " and course_code like '%" + course.getCourseCode()+"%'";
			}
			pst = conn
					.prepareStatement(getCourseDetailsQuery);
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				Course course1 = new Course();
				course1.setCourseId(rs.getInt("course_id"));
				course1.setCourseName(rs.getString("course_name"));
				course1.setStatus(rs.getString("status"));
				course1.setCourseCode(rs.getString("course_code"));
				course1.setType(rs.getString("type"));
				course1.setCapacity(rs.getInt("capacity"));
				course1.setDeptName(rs.getString("dept_name"));
				course1.setAvailable(rs.getString("available"));
				course1.setProfessor(rs.getString("professor"));
				departmentsList.add(course1);
	        }
			
			String getEnrolledCountQuery = "select course_id,count(*) from enrollment_details group by course_id";	
			Map<Integer,Integer> enrolledCountMap = new HashMap<Integer,Integer>(); 
			pst = conn
					.prepareStatement(getEnrolledCountQuery);
			
			rs = pst.executeQuery();
			while(rs.next()) {
				enrolledCountMap.put(rs.getInt(1), rs.getInt(2));
			}
			
			for(Course c : departmentsList) {
				if(enrolledCountMap.containsKey(c.getCourseId())) {
					c.setRemaining(c.getCapacity()-enrolledCountMap.get(c.getCourseId()));
				}else {
					c.setRemaining(c.getCapacity());
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return departmentsList;
	}

	
	public List<Student> getStudentDetails() {	
		List<Student> studentsList = new ArrayList<Student>();
		try {
			
			Class.forName(driver).newInstance();
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			String getStudentDetailsQuery = "select * from student_details where first_name!='admin'"; 
		
			pst = conn
					.prepareStatement(getStudentDetailsQuery);
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				Student student = new Student();
				student.setStudentId(rs.getInt("student_id"));
				student.setFirstName(rs.getString("first_name"));
				student.setMiddleName(rs.getString("middle_name"));
				student.setLastName(rs.getString("last_name"));
				student.setEmailId(rs.getString("email_id"));
				student.setContactNo(rs.getString("contact_no"));
				student.setStatus(rs.getString("status"));
				student.setTbTest(rs.getString("tb_test"));
				student.setMmr(rs.getString("mmr"));
				student.setMailed(rs.getString("mailed"));
				studentsList.add(student);
	        }

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return studentsList;
	}

	
	
	public boolean insertDepartmentDetails(List<Course> courseList) {
	try {
			
			conn = createConnection();
			stmt = conn.createStatement();
			
			for(Course c : courseList) {
			String insertQuery = "insert into course_details values (null,'" +c.getCourseName()+
					"','" +c.getStatus()+
					"','" +c.getCourseCode()+
					"','" +c.getType()+
					"','" +c.getCapacity()+
					"','" +c.getDeptName()+
					"','" +c.getAvailable()+
					"','" +c.getProfessor()+
					"')";
		
			stmt.executeUpdate(insertQuery);
			
			}	
	
		} catch (Exception e) {
			System.out.println(e);
			return false;
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			closeConnection(conn,pst,rs);
		}
		return true;
	}
	
	public boolean updateDepartmentDetails(Course course) {
		try {
				
				conn = createConnection();
				stmt = conn.createStatement();
				String updateQuery = "update course_details set course_name = '" +course.getCourseName()+
						"', course_code = '" +course.getCourseCode()+
						"', type = '" +course.getType()+
						"',capacity = '" +course.getCapacity()+
						"', dept_name= '" +course.getDeptName()+
						"',available= '" +course.getAvailable()+
						"',professor= '" +course.getProfessor()+
						"' where course_id = '" + course.getCourseId() + "'";
			
				int count = stmt.executeUpdate(updateQuery);
					if(count>0) {
						return true;
					}
		
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				closeConnection(conn,pst,rs);
			}
			return false;
		}
	
	public boolean updateStudentDetails(Student student) {
		try {
				
				conn = createConnection();
				stmt = conn.createStatement();
				String query = "update student_details set first_name = '" +student.getFirstName()+
						"', middle_name = '" +student.getMiddleName()+
						"', last_name = '" +student.getLastName()+
						"',contact_no = '" +student.getContactNo()+
						"', email_id= '" +student.getEmailId()+
						"',mmr= '" +student.getMmr()+
						"',tb_test= '" +student.getTbTest()+
						"',mailed= '" +student.getMailed()+
						"',status= '" +student.getStatus()+
						"' where student_id = '" + student.getStudentId() + "'";
			

				int count = stmt.executeUpdate(query);
					if(count>0) {
						return true;
					}
		
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				closeConnection(conn,pst,rs);
			}
			return false;
		}
	
	public boolean deleteCourseDetails(Course course) {
		try {
				
				conn = createConnection();
				stmt = conn.createStatement();
				String deleteQuery = "delete from course_details where course_id = '" +course.getCourseId()+"'";
			
				int count = stmt.executeUpdate(deleteQuery);
					if(count>0) {
						return true;
					}
		
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				closeConnection(conn,pst,rs);
			}
			return false;
		}
	
	private void closeConnection(Connection con, PreparedStatement pst, ResultSet rs) {

		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pst != null) {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
	}
	
	private Connection createConnection() {
		try{
		Class.forName(driver).newInstance();
		conn = DriverManager
				.getConnection(url + dbName, userName, password);
		
		}catch(Exception e) {
			System.out.println(e);
		}
		return conn;
	}
	
}