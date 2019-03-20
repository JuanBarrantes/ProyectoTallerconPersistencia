<%-- 
    Document   : Observaciones
    Created on : 08/07/2018, 01:09:52 AM
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
        <title>Observaciones</title>
        <link rel="stylesheet" href="/Proyecto_Taller/css/bootstrap.min.css"/>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <%@ include file = "../BarraNav.jsp" %> 
        </nav>
        <div class="card p-4">
            <div id="numero1">
                <div class="container border border-primary rounded" >
                    <div class="text-primary" style="text-align:center">
                        <h1>OBSERVACIONES</h1>
                    </div>
                    <div class="form-group row">
                        <label for="TESIS" class="col-sm-2 col-form-label">AnteProyecto: </label>
                        <div class="col-sm-4">
                            <button style="margin-top: 10px" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalconstancias" name="btncargar" id="btncargar">Buscar</button>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="col offset">
                            <h5 id="respuestafechas"></h5>
                        </div>
                    </div>
                    <div id="listadejurado">
                        <div class="form-group row">
                            <label class="col-form-label col-sm-2 text-primary">PRESIDENTE: </label>
                            <div class="col-sm-3">
                                <label class="col-form-label text-dark" id="miembro0"></label>
                            </div>
                            <div class="col-sm-2" id="btn0">
                                <button id="g0" type="button" class="btn btn-success" >Sin Observacion</button>
                                <input type="hidden" name="codites" id="codites">
                            </div>
                            <div class="col-sm-1" id="btn0">
                                <button id="b0" type="button" class="btn btn-danger" data-toggle="modal" data-target="#ModalObsevaciones">Observar</button>
                            </div>
                            <div class="col-sm-3" id="btn0">
                                <button id="E0" type="button" class="btn btn-success" data-toggle="modal" data-target="#Estasseguro">Levantar Observacion</button>
                            </div>
                            <div class="col-sm-1" id="btn0">
                                <label class="col-form-label text-dark" id="estado0"></label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-form-label col-sm-2 text-primary">SECRETARIO: </label>
                            <div class="col-sm-3">
                                <label class="col-form-label text-dark" id="miembro1"></label>
                            </div>
                            <div class="col-sm-2" id="btn0">
                                <button id="g1" type="button" class="btn btn-success" >Sin Observacion</button>
                            </div>
                            <div class="col-sm-1" id="btn1">
                                <button id="b1" type="button" class="btn btn-danger" data-toggle="modal" data-target="#ModalObsevaciones">Observar</button>
                            </div>
                            <div class="col-sm-3" id="btn1">
                                <button id="E1" type="button" class="btn btn-success" data-toggle="modal" data-target="#Estasseguro">Levantar Observacion</button>
                            </div>
                            <div class="col-sm-1" id="btn0">
                                <label class="col-form-label text-dark" id="estado1"></label>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label class="col-form-label  col-sm-2 text-primary">VOCAL: </label>
                            <div class="col-sm-3">
                                <label class="col-form-label text-dark" id="miembro2"></label>
                            </div>
                            <div class="col-sm-2" id="btn0">
                                <button id="g2" type="button" class="btn btn-success" >Sin Observacion</button>
                            </div>
                            <div class="col-sm-1" id="btn2">
                                <button id="b2" type="button" class="btn btn-danger" data-toggle="modal" data-target="#ModalObsevaciones">Observar</button>
                            </div>
                            <div class="col-sm-3" id="btn2">
                                <button id="E2" type="button" class="btn btn-success" data-toggle="modal" data-target="#Estasseguro">Levantar Observacion</button>
                            </div>
                            <div class="col-sm-1" id="btn0">
                                <label class="col-form-label text-dark" id="estado2"></label>
                            </div>
                        </div>
                        <div class="form-group row offset-sm-4">
                            <input type="button" class="btn btn-success btn-sm"  id="Aprobar" aria-label="Small" title="Aprobar" value="Aprobar Anteproyecto">
                            <div class="col-sm-2" id="dformatoB">

                            </div>
                            <div class="col-sm-3" id="dreportefinal">

                            </div>
                        </div>
                        <form id="datosjurado">
                            <input id="mjurado0" name="mjurado0" type="hidden">
                            <input id="mjurado1" name="mjurado1" type="hidden">
                            <input id="mjurado2" name="mjurado2" type="hidden">
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="ModalObsevaciones" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">Observacion:</h5>
                        <h5 class="modal-title" id="fecha"></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="formObservaciones">
                            <div class="col">
                                <textarea name="observ" id="observ"  class="form-control" rows="8" placeholder="Ponga aqui las observaciones ...."></textarea>
                            </div>
                            <input name="fecha2" id="fecha2" type="hidden">
                            <input name="relacionjurado" id="relacionjurado" type="hidden">
                            <input name="idtesis" id="idtesis" type="hidden">
                            <input type="hidden" name="filtro" id="orden" value="AgregarObservacion">
                        </form>
                    </div>
                    <div class="modal-footer">
                        <input type="button" class="btn btn-primary btn-sm"  id="guardarObservacion" aria-label="Small" title="Guardar" value="Registrar Obsevacion">

                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" name="consta" id="consta">
        <div class="modal fade" id="Estasseguro" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title text-danger" id="exampleModalLabel">¡AVISO!</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        ¿ESTA REALMENTE SEGURO QUE DESEA LEVANTAR LA OBSERVACION?
                        <form id="formlevantar">
                            <input name="relacionjuradol" id="relacionjuradol" type="hidden">
                            <input name="idtesisl" id="idtesisl" type="hidden">
                            <input type="hidden" name="filtro" id="orden" value="LevantarObservacion">
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">NO</button>
                       <input type="button" class="btn btn-primary btn-sm"  id="levantarObservacion" aria-label="Small" title="Levantar Observación" value="Si, lo estoy">
                    </div>
                </div>
            </div>
        </div>


        <div class="modal fade" id="modalconstancias" tabindex="-1" role="dialog"> 
            <div class="modal-dialog modal-sm-12" role="document">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title">Tesis</h5>
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
                                        <input type="button" class="btn btn-primary btn-sm"  id="btnBuscar2" aria-label="Small" title="Buscar Tesis" value="Buscar">
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
                                        <th>TESIS</th>
                                        <th>AUTOR</th>
                                        <th>ASESOR</th>
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
    <script src="js/apps/Observacion.js" type="text/javascript"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
    <script src="/Proyecto_Taller/js/bootstrap.min.js"></script>
</html>
