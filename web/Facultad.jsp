<%-- 
    Document   : Facultad
    Created on : 05/06/2018, 01:36:06 PM
    Author     : JhanxD
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.* , Control.FacultadCotroller, Beans.Facultad"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="area_session" scope="session" type="java.lang.String"  class="java.lang.String"></jsp:useBean>
<jsp:useBean id="namae" scope="session" type="java.lang.String"  class="java.lang.String"></jsp:useBean>
<!DOCTYPE html>
<%
    response.setHeader("Pragma", "no-cache");
    response.addHeader("Cache-Control", "must-revalidate");
    response.addHeader("Cache-Control", "no-cache");
    response.addHeader("Cache-Control", "no-store");
    response.setDateHeader("Expires", 0);
%>

<style>
    .ocultar{
        display:none
        }
        
    table{
        height: 410px;
        overflow-y: auto;
        overflow-x: auto;
    }
</style>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Facultad</title>
        <link rel="stylesheet" href="/Proyecto_Taller/css/bootstrap.min.css"/>
    </head>
    <body>
        
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
                    <%@ include file = "../BarraNav.jsp" %> 
                </nav>
 <h1 style="text-align: center">LISTA DE FACULTADES REGISTRADAS</h1>
        <div class="container">
            <form  id="BuscaFacultad">
                                            <div class="row">
                                                <div class="col-4">
                                                    <div class="input-group input-group-sm mb-3">
                                                        <input type="text" class="form-control input-sm mr-2" aria-label="Small" aria-describedby="inputGroup-sizing-sm" name="txtNombre" id="txtNombre" placeholder="Nombre">
                                                        <input type="button" class="btn btn-primary btn-sm"  id="btnBuscar" aria-label="Small" title="Buscar Facultad" value="Buscar">
                                                        <input type="hidden" name="filtro" id="orden" value="Buscar">
                                                    </div>
                                                </div>
                                                
                                            </div>
                                        </form>
            <form  id="tablita">
            <div class="col-12">
                <table class="table table-responsive table-bordered table-hover table-sm" id="tablafacultad">
                    <thead class="thead-dark">
                        <tr class="table-info">
                            <th class="ocultar">codigo</th>
                            <th>ABREVIATURA</th>
                            <th>NOMBRE</th>
                            <th style="width: 4%; text-align: center;">EDIT</th>
                            <th style="width: 4%; text-align: center;">ELI</th>
                        </tr>
                    </thead>
                    <tbody id="registrotabla">
                        
                    </tbody>
                </table>
                
            </div>
            </form>
        </div>

    
</div>
<div class="container">
    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalnuevo">
        Nueva Facultad
    </button>
</div>


<div class="modal fade" id="modalnuevo" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Nueva Facultad</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form name="form1" method="POST" enctype="multipart/form-data" action="FacultadCotroller" id="frmnuevo">
                    
                    <table class="table">
                        <tbody>

                            <tr>
                                <td>Nombre</td>
                                <td><label for="nombre"></label><input type="text" name="nombre" id="nombren"></td>
                            </tr>
                            <tr>
                                <td>Abreviatura</td>
                                <td><label for="abreviatura"></label><input type="text" name="abreviatura" id="abreviaturan"></td>
                            </tr>
                        </tbody>
                        <tr class="modal-footer">
                            
                            <td><input type="button" class="btn btn-primary btn-sm"  id="guardar" aria-label="Small" title="Guardar Facultad" value="Agregar"></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="modaleditar" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Modificar Facultad</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form name="form2" method="POST" enctype="multipart/form-data" action="FacultadCotroller" id="frmeditable">
                    
                    <table class="table">
                        
              
                        <tbody id="cuerpo">
                            <div class="modal-body">
                                            <div class="form-group col-12">
                                                <label for="txtNombreER">Nombre</label>
                                                <input class="form-control form-control-sm" id="txtNombreER" name="txtNombreER" type="text" placeholder="Nombre" maxlength="100">
                                                
                                            </div>
                                            <div class="form-group col-12">
                                                <label for="txtNombreAbreviadoER">Abreviado</label>
                                                <input class="form-control form-control-sm" id="txtNombreAbreviadoER" name="txtNombreAbreviadoER" type="text" placeholder="Abreviado" maxlength="30">
                                                
                                            </div>
                                            <div class="form-group col-12 ocultar">
                                                <label for="txtNombreAbreviadoER">Codigo</label>
                                                <input class="form-control form-control-sm" id="txtIdFacultadER" name="txtIdFacultadER" type="text" placeholder="Codigo" maxlength="30">
                                                
                                            </div>
                                            <input type="hidden" id="accionModal" name="filtro" value="">
                                            <div class="clearfix"></div>
                                        </div>
                        </tbody>
                        <tr class="modal-footer">
                            <td><input type="button" class="btn btn-primary btn-sm"  id="editar" aria-label="Small" title="Editar Facultad" value="Editar"></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="modaleliminar" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">¿REALMENTE DESEA ELIMINAR FACULTAD?</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form name="form2" method="POST" enctype="multipart/form-data" action="FacultadCotroller" id="frmBORRABLE">
                    
                    <table class="table">
                        
              
                        <tbody id="Ncuerpo">
                            <div class="modal-body">
                                            <div class="form-group col-12">
                                                <label for="txtNombreER">Nombre</label>
                                                <input class="form-control form-control-sm" id="txtNombreER2" name="txtNombreER2" type="text" disabled="" placeholder="Nombre" maxlength="100">
                                                
                                            </div>
                                            <div class="form-group col-12">
                                                <label for="txtNombreAbreviadoER">Abreviado</label>
                                                <input class="form-control form-control-sm" id="txtNombreAbreviadoER2" name="txtNombreAbreviadoER2" type="text" disabled="" placeholder="Abreviado" maxlength="30">
                                                
                                            </div>
                                            <div class="form-group col-12 ocultar">
                                                <label for="txtNombreAbreviadoER">Codigo</label>
                                                <input class="form-control form-control-sm" id="txtIdFacultadER2" name="txtIdFacultadER2" type="text" placeholder="Codigo" maxlength="30">
                                                
                                            </div>
                                            <input type="hidden" id="accionModal2" name="filtro" value="">
                                            <div class="clearfix"></div>
                                        </div>
                        </tbody>
                        <tr class="modal-footer">
                            <td><input type="button" class="btn btn-danger btn-sm"  id="eliminar" aria-label="Small" title="Borrar Facultad" value="Eliminar"></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script src="js/apps/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="js/apps/Facultad.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
<script src="/Proyecto_Taller/js/bootstrap.min.js"></script>
</html>
