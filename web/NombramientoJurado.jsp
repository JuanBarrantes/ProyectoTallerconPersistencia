<%-- 
    Document   : NombramientoJurado
    Created on : 02/07/2018, 11:42:23 AM
    Author     : JhanxD
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    .color0{
        background-color:red
    }
    .color1{
        background-color:green
    }
    .letras{
        color: 	#FFFFFF
    }


    table{
        height: 300px;
        overflow-y: auto;
        overflow-x: auto;
    }
</style>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nombramiento del Jurado</title>
        <link rel="stylesheet" href="/Proyecto_Taller/css/bootstrap.min.css"/>
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <%@ include file = "../BarraNav.jsp" %> 
        </nav>
        <div class="card p-4">
            <div id ="numero1">
                <div class="container border border-primary rounded" >
                    <div class="form-group row">
                        <label for="constancia" class="col-sm-5 col-form-label offset-sm-3"><h4>Revisar Constancia: </h4></label>
                        <div class="col-sm-4">
                            <button style="margin-top: 10px" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalconstancias" name="btncargar" id="btncargar">Buscar</button>
                        </div>
                    </div>
                </div>

                <div class="container border border-primary rounded" style="margin-top: 20px">
                    <div class="text-primary" style="text-align:center">
                        <h1>JURADO SELECCIONADO:</h1>
                    </div>
                    <div class="container border border-success rounded">
                        <div class="form-group row" style="margin-top: 20px" >
                            <label class="col-sm-4 col-form-label offset-sm-2"><h6>NUMERO DE LA CONSTANCIA:</h6></label>
                            <div class="col-sm-4">
                                <div id="n" class="container border border-success rounded text-muted">Esperando...</div>
                            </div>

                        </div>
                        <div class="form-group row">
                            <label class="col-sm-4 col-form-label offset-sm-2"><h6>ASESOR:</h6></label>
                            <div class="col-sm-4">
                                <div id="a" class="container border border-success rounded text-muted">Esperando...</div>
                            </div>

                        </div>
                        <div class="form-group row">
                            <label class="col-sm-4 col-form-label offset-sm-2"><h6>AUTOR:</h6></label>
                            <div class="col-sm-4">
                                <div id="a2" class="container border border-success rounded text-muted">Esperando...</div>
                            </div>

                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label text-primary"></label>
                        <div class="col-sm-4">
                            <label class="col-form-label text-primary">NOMBRE</label>
                        </div>
                        <div class="col-sm-2">
                            <label class="col-form-label text-primary">CATEGORIA</label>
                        </div>
                        <div class="col-sm-2">
                            <label class="col-form-label text-primary">GRADO ACADEMICO</label>
                        </div>
                        <div class="col-sm-2">
                            <label class="col-form-label text-primary">ANTIGUEDAD</label>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="presidente" class="col-sm-2 col-form-label text-primary">Presidente:</label>
                        <div class="col-sm-4">
                            <div id="n0" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                        <div class="col-sm-2">
                            <div id="c0" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                        <div class="col-sm-2">
                            <div id="g0" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                        <div class="col-sm-2">
                            <div id="f0" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="secretario" class="col-sm-2 col-form-label text-primary">Secretario:</label>
                        <div class="col-sm-4">
                            <div id="n1" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                        <div class="col-sm-2">
                            <div id="c1" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                        <div class="col-sm-2">
                            <div id="g1" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                        <div class="col-sm-2">
                            <div id="f1" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="vocal" class="col-sm-2 col-form-label text-primary">Vocal:</label>
                        <div class="col-sm-4">
                            <div id="n2" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                        <div class="col-sm-2">
                            <div id="c2" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                        <div class="col-sm-2">
                            <div id="g2" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                        <div class="col-sm-2">
                            <div id="f2" class="container border border-primary rounded text-muted">Esperando...</div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <button type="button" id="btnGuardarr" class="btn btn-primary offset-sm-4">Confirmar</button>
                        <div class="col-sm-2 offset" id="dr">

                        </div>
                        <div class="col-sm-2 offset">
                            <button type="button" id="btnContinuar" class="btn btn-primary offset-sm-4">Continuar</button>
                        </div>
                    </div>
                    
                    <input type="hidden" name="d0" id="d0">
                    <input type="hidden" name="d1" id="d1">
                    <input type="hidden" name="d2" id="d2">
                    <input type="hidden" name="ga0" id="ga0">
                    <input type="hidden" name="ga1" id="ga1">
                    <input type="hidden" name="ga2" id="ga2">
                    <form id="formjurado">
                        <input type="hidden" name="cargo0" id="cargo0">
                        <input type="hidden" name="cargo1" id="cargo1">
                        <input type="hidden" name="cargo2" id="cargo2">
                        <input type="hidden" name="PRESIDENTE" id="PRESIDENTE" value="PRESIDENTE">
                        <input type="hidden" name="SECRETARIO" id="SECRETARIO" value="SECRETARIO">
                        <input type="hidden" name="VOCAL" id="VOCAL" value="VOCAL">
                        <input type="hidden" name="idcodigoc" id="idcodigoc">
                        <input type="hidden" class="form-control" name="fechaformat" id="fechaformat">
                        <input type="hidden" class="form-control" name="filtro" id="orden" value="Guardar">
                    </form>
                    <input type="text" disabled="" class="form-control text-center" name="txtfecha" id="txtfecha">
                </div>

            </div>
            <div id="numero2">
                <div class="container border border-primary rounded" >
                    <div class="text-primary" style="text-align:center">
                        <h1>ENVIO DE MENSAJES</h1>
                    </div>
                    <div class="form-group row">
                        <label for="correoP" class="col-sm-4 col-form-label text-primary">Correo del Presidente: </label>
                        <div class="col-sm-4">
                            <div id="correo0" class="container border border-primary rounded ">Esperando...</div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="correoS" class="col-sm-4 col-form-label text-primary">Correo del Secretario </label>
                        <div class="col-sm-4">
                            <div id="correo1" class="container border border-primary rounded ">Esperando...</div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="correoV" class="col-sm-4 col-form-label text-primary">Correo del Vocal </label>
                        <div class="col-sm-4">
                            <div id="correo2" class="container border border-primary rounded ">Esperando...</div>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-2 offset-sm-4" id="dcorreos">

                        </div>
                        <div class="col-sm-2" id="dformatoA">

                        </div>
                    </div>
                    <form id ="correos">
                        <input type="hidden" name="const" id="const">
                        <input type="hidden" name="co0" id="co0">
                        <input type="hidden" name="co1" id="co1">
                        <input type="hidden" name="co2" id="co2">
                    </form>
                </div>
            </div>
            
            

            <div class="col-sm-8 offset-sm-2">
                <div style="margin-top: 25px" class="progress" id="progres4">
                    <div class="progress-bar progress-bar-striped bg-success" role="progressbar" style="width: 66%" aria-valuenow="66" aria-valuemin="0" aria-valuemax="100">Segunda Etapa Completada</div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="modalconstancias" tabindex="-1" role="dialog"> 
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title">Constancias</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>


                    </div>
                    <div class="modal-body">
                        <form  id="BuscaConstancia">
                            <div class="row">
                                <div class="col-8">
                                    <div class="input-group input-group-sm mb-2">
                                        <input type="text" class="form-control input-sm mr-2" aria-label="Small" aria-describedby="inputGroup-sizing-sm" name="txtNombre" id="txtNombre" placeholder="NRO DE CONSTANCIA">
                                        <input type="button" class="btn btn-primary btn-sm"  id="btnBuscar2" aria-label="Small" title="Buscar Constancia" value="Buscar">
                                        <input type="hidden" class="form-control" name="filtro" id="orden" value="Cargar">
                                    </div>
                                </div>

                            </div>
                        </form>

                        <form id="frmConstanciass">
                            <table class="table table-responsive table-bordered table-hover table-sm" id="tablaConstancias">
                                <thead class="thead-dark">
                                    <tr class="table-info">
                                        <th class="ocultar">codigo</th>
                                        <th>Escoger:</th>
                                        <th>N° CONSTANCIA</th>
                                        <th>N° EXPEDIENTE</th>
                                        <th>DICTAMEN</th>
                                        <th>CODIGO</th>

                                        <th>ALUMNO</th>
                                        <th>ASESOR</th>
                                        <th>FECHA</th>
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

    </body>
    <script src="js/apps/jquery-3.3.1.min.js"></script>
    <script src="js/apps/Jurado.js" type="text/javascript"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
    <script src="/Proyecto_Taller/js/bootstrap.min.js"></script>
</html>
