<%-- 
    Document   : EstadosAnteproyectos
    Created on : 28/07/2018, 10:11:00 PM
    Author     : JhanxD
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
</style>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Estado Del Anteproyecto</title>
        <link rel="stylesheet" href="/Proyecto_Taller/css/bootstrap.min.css"/>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <%@ include file = "../BarraNav2.jsp" %> 
        </nav>

        <div class="card p-4">
            <div id="numero1">
                <div class="container border border-primary rounded" >
                    <div class="text-primary" style="text-align:center">
                        <h1>ESTADO DEL ANTEPROYECTO</h1>
                    </div>
                    <div class="form-group row">
                        <label for="TESIS" class="col-sm-2 col-form-label offset-4"><h6 class="text-center">AnteProyecto: </h6></label>
                        <div class="col-sm-4">
                            <button style="margin-top: 10px" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalconstancias" name="btncargar" id="btncargar">Escoger</button>
                        </div>
                    </div>
                    <div class="form-group row">
                            <label for="Constancia" class="col-sm-2 col-form-label">N° de Constancia: </label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="txtNroConstancia" id="txtNroConstancia" placeholder="ESPERANDO...">
                            </div>
                            <div class="col-sm-2">
                                <label for="Constancia" class="col-form-label">N° de Expediente: </label>
                            </div>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="txtNroExpediente" id="txtNroExpediente" placeholder="ESPERANDO...">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="titulo" class="col-sm-2 col-form-label">Titulo del Anteproyecto: </label>

                            <div class="col">
                                <textarea class="form-control" id="txttitulo" name="txttitulo" placeholder="ESPERANDO..." rows="3"></textarea>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="codigoP" class="col-sm-2 col-form-label">Codigo Anteproyecto: </label>

                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="txtcodigoP" id="txtcodigoP" placeholder="ESPERANDO...">    
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="alumno" class="col-sm-2 col-form-label">Autor: </label>

                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="txtautor" id="txtautor" placeholder="ESPERANDO...">    
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="asesor" class="col-sm-2 col-form-label">Asesor:</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="txtasesorP" id="txtasesorP" placeholder="ESPERANDO...">  
                            </div>
                            <div class="col-sm-2">
                                <label for="linea" class="col-form-label">Linea: </label>
                            </div>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="txtLinea" id="txtLinea" placeholder="ESPERANDO...">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="fecha" class="col-sm-2 col-form-label">Fecha de Registro:</label>
                            <div class="col-sm-4">         
                                <input type="text" class="form-control" name="txtfecha" id="txtfecha" placeholder="ESPERANDO...">
                            </div>
                            <div class="col-sm-2">
                                <label for="linea" class="col-form-label">Fecha de Aprobación: </label>
                            </div>
                            <div class="col-sm-4">         
                                <input type="text" class="form-control" name="txtfecha2" id="txtfecha2" placeholder="ESPERANDO...">
                            </div>
                        </div>
                        <div class="form-group row offset-4">
                            <label for="jurado1" class="col-sm-4 col-form-label text-center"><h6>MIEMBROS DEL JURADO</h6></label>
                        </div>
                        <div class="form-group row offset-2">
                            <label for="jurado1" class="col-sm-2 col-form-label">Presidente:</label>
                            <div class="col-sm-2">         
                                <input type="text" class="form-control" name="txtpresidenteGra" id="txtpresidenteGra0" placeholder="ESPERANDO...">
                            </div>
                            <div class="col-sm-4">         
                                <input type="text" class="form-control" name="txtpresidente" id="txtpresidente0" placeholder="ESPERANDO...">
                            </div>
                        </div>
                        <div class="form-group row offset-2">
                            <label for="jurado2" class="col-sm-2 col-form-label">Secretario:</label>
                            <div class="col-sm-2">         
                                <input type="text" class="form-control" name="txtsecretarioGra" id="txtpresidenteGra1" placeholder="ESPERANDO...">
                            </div>
                            <div class="col-sm-4">         
                                <input type="text" class="form-control" name="txtsecretraio" id="txtpresidente1" placeholder="ESPERANDO...">
                            </div>
                        </div>
                        <div class="form-group row offset-2">
                            <label for="jurado3" class="col-sm-2 col-form-label">Vocal:</label>
                            <div class="col-sm-2">         
                                <input type="text" class="form-control" name="txtvocalGra" id="txtpresidenteGra2" placeholder="ESPERANDO...">
                            </div>
                            <div class="col-sm-4">         
                                <input type="text" class="form-control" name="txtvocal" id="txtpresidente2" placeholder="ESPERANDO...">
                            </div>
                        </div>
                </div>
            </div>
        </div>
        
        <div class="modal fade" id="modalconstancias" tabindex="-1" role="dialog"> 
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title">AnteProyectos</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <div class="modal-body">
                        <form  id="BuscaTesis">
                            <div class="row">
                                <div class="col-8">
                                    <div class="input-group input-group-sm mb-2">
                                        <input type="text" class="form-control input-sm mr-2" aria-label="Small" aria-describedby="inputGroup-sizing-sm" name="txtNombre" id="txtNombre" placeholder="CODIGO DE TESIS">
                                        <input type="button" class="btn btn-primary btn-sm"  id="btnBuscar2" aria-label="Small" title="Buscar" value="Buscar">
                                        <input type="hidden" class="form-control" name="filtro" id="orden" value="Cargar">
                                    </div>
                                </div>

                            </div>
                        </form>

                        <form id="frmConstanciass">
                            <table class="table table-responsive table-bordered table-hover table-lg" id="tablaConstancias">
                                <thead class="thead-dark">
                                    <tr class="table-info">
                                        <th class="ocultar">codigo</th>
                                        <th>Escoger:</th>
                                        <th>CODIGO</th>
                                        <th>AUTOR</th>
                                        <th>ASESOR</th>
                                        <th>ESTADO</th>
                                    </tr>
                                </thead>
                                <tbody id="tablitaConstancia">

                                </tbody>

                            </table>
                        </form>

                    </div>
                </div>
            </div>
        </div>
        






        <script src="js/apps/jquery-3.3.1.min.js"></script>
        <script src="js/apps/EstadosExpediente.js" type="text/javascript"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
        <script src="/Proyecto_Taller/js/bootstrap.min.js"></script>
    </body>
</html>
