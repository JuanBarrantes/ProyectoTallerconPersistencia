<%-- 
    Document   : Index
    Created on : 05/06/2018, 10:06:46 PM
    Author     : JhanxD
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="area_session" scope="session" type="java.lang.String"  class="java.lang.String"></jsp:useBean>
<jsp:useBean id="namae" scope="session" type="java.lang.String"  class="java.lang.String"></jsp:useBean>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>INICIO</title>
        <link rel="stylesheet" href="/Proyecto_Taller/css/bootstrap.min.css"/>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <%@ include file = "../BarraNav2.jsp" %> 
        </nav>

        <script src="js/apps/jquery-3.3.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
        <script src="/Proyecto_Taller/js/bootstrap.min.js"></script>
    </body>
</html>

