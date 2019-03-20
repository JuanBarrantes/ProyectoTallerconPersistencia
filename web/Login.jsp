<%-- 
    Document   : login
    Created on : 14/06/2018, 02:28:58 PM
    Author     : JhanxD
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" href="/Proyecto_Taller/css/bootstrap.min.css"/>
    </head>
     <body>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-6 col-md-8">
                    <div class="card border-info p-4">
                        <div class="card-body">
                            <form method="POST" id="FrmLogin" action="Session">
                                <div style="text-align: center;">
                                    <h1 class="text-primary">SisTes-Proyecto de Taller</h1>
                                   
                                    <h4 class="text-primary">UNPRG</h4>
                                </div>
                                
                                <div class="row justify-content-center">
                                    
                                    <input type="text" class="form-control" placeholder="Usuario" name="txtUsuario" id="txtUsuario">
                                </div>
                                <div class="row justify-content-center">
                                    
                                    <input type="password" class="form-control" placeholder="Contraseña" name="txtPass" id="txtPass">
                                </div>
                                <div class="row">
                                    <div class="col-6">
                                        <button type="submit" class="btn btn-primary px-4" id="btnIngresar"> Login</button>
                                    </div>
                                    <div class="col-6 text-right">
                                        <button type="button" class="btn btn-link px-0">Olvidaste Contraseña?</button>
                                    </div>                                        
                                </div>
                                
                                <div class="row">
                                    <div class="col-12 text-center text-primary">
                                        
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <script src="js/apps/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="js/apps/Login.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
<script src="/Proyecto_Taller/js/bootstrap.min.js"></script>
</html>
