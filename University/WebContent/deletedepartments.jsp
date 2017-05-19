<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<link rel="stylesheet" href="css/reset.css" type="text/css" media="all">
<link rel="stylesheet" href="css/layout.css" type="text/css" media="all">
<link rel="stylesheet" href="css/style.css" type="text/css" media="all">
<script type="text/javascript" src="js/jquery-1.5.2.js" ></script>
<script type="text/javascript" src="js/cufon-yui.js"></script>
<script type="text/javascript" src="js/cufon-replace.js"></script>
<script type="text/javascript" src="js/Cabin_400.font.js"></script>
<script type="text/javascript" src="js/tabs.js"></script>
<script type="text/javascript" src="js/jquery.jqtransform.js" ></script>
<script type="text/javascript" src="js/jquery.nivo.slider.pack.js"></script>
<script type="text/javascript" src="js/atooltip.jquery.js"></script>
<script type="text/javascript" src="js/script.js"></script>

<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

</head>
<body id="page4">
<div class="main">
  <!--header -->
    <header>
      <div class="wrapper">
      <h1>  </h1>
       <h1>&nbsp  </h1>
	   <h1>&nbsp  </h1>
	   <br/>
	   
	   <br/>
	   <h1>&nbsp  </h1>
    </div>

 	<nav>
            <ul id="menu">
        <li  id="menu_active"><a href="#"><span><span>Course Details</span></span></a></li>
        <li><a href="#" onClick="modifyFunction('viewstudents');"><span><span>View Students</span></span></a></li>
        <li><a href="#" onClick="modifyFunction('enquiryhome');"><span><span>View Enquiries</span></span></a></li>
        <li class="end"><a href="login.jsp"><span><span>Logout</span></span></a></li>
      </ul>
    </nav>

     <script type="text/javascript">
    
    function modifyFunction(viewType) {
    	document.ContactForm.viewType.value=viewType;
    	if(viewType=='enquiryhome') {
    		document.ContactForm.action="enquiryServlet";
    	}
    	document.ContactForm.submit();
    }
    
    function deleteDepartment(cId) {
    	document.ContactForm.viewType.value="deleteDepartmentdetails";
    	document.ContactForm.selecteddepartment.value=cId;
    	var r = confirm("Are you sure to delete ? ");
    	if(r==true) {
    		document.ContactForm.submit();
    	}else {
    		return false;
    	}
    
    }
    
    </script>
    
  </header>
  <!-- / header -->
  <!--content -->
  <section id="content">
    <div class="wrapper pad1">
	
	      <article class="col1">
        <div class="box1">
          <h2 class="top">Modify Course Details</h2>
          <div class="pad">
           
            <ul class="pad_bot2 list1">
              <li> <a href="adddepartments.jsp">Add Course Details</a> </li>
               <li> <a href="#" onClick="modifyFunction('editdepartment');">Edit Course Details</a> </li>
              <li><a  href="#" onClick="modifyFunction('deletedepartment');">Delete Course Details</a> </li>
            </ul>
          </div>
        </div>
      </article>  
	
      <article class="col2">
      
 <h3 class="pad_top1" align="center">Delete Course Details</h3>
        <form name="ContactForm" id="ContactForm" action="viewDepartmentsServlet" method="post">
        <input type="hidden"  class="input"  name="viewType" required="required" value="deletedepartment"/>
        <input type="hidden"  class="input"  name="selecteddepartment" required="required" value=""/>
        <table align="center">
                    
        <tr>
        <td> Course Name : </td>
        <td> <input type="text"  class="input"  name="deptname"  value=""/></td>
            </tr>
            <tr><td>&nbsp;</td></tr>
                    
              <tr><td>&nbsp;</td></tr>
              <tr>
					<td colspan="2" align="center"><input type="submit" value="Search" /></td>
				</tr>
		
              </table>
    </form>
    
      </article>
    </div>
    
 <table width="100%" >
    <tr>
        <td colspan="7"> <h4>Department Details</h4> </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
     <tr>
     <th>Course Name</th>
      <th>Course Code</th>
     <th>Department Name</th>
     <th>Department Type</th>
     <th>Action</th>
      </tr> 
      
      <tr><td colspan="7">&nbsp;</td></tr> 
    <core:forEach var="course" items="${departmentsList}">
    <tr align="center">
    <td><core:out value="${course.courseName}"></core:out> </td>
        <td><core:out value="${course.courseCode}"></core:out> </td>
    
    <td><core:out value="${course.deptName}"></core:out> </td>
    <td><core:out value="${course.type}"></core:out> </td>
     
    <td><a href="#"  onClick="deleteDepartment('<core:out value="${course.courseId}"/>');">Delete</a> </td>
    
    </tr>
    
    </core:forEach>
    </table>
       
  </section>
  <!--content end-->
  <!--footer -->
  <footer>
    <div class="wrapper">
      
    </div>
  </footer>
  <!--footer end-->
</div>
<script type="text/javascript">Cufon.now();</script>
</body>
</html>