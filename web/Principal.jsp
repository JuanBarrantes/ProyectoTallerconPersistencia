<%-- 
    Document   : Principal
    Created on : 25/06/2018, 10:26:24 PM
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
        <title>Comenzar Registro</title>
        <link rel="stylesheet" href="/Proyecto_Taller/css/bootstrap.min.css"/>
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <%@ include file = "../BarraNav.jsp" %> 
        </nav>
        <div class="card p-4">
            <div id="numero1">
                <div class="container border border-primary rounded" >
                    <div class="form-group row">
                        <label for="viejoalumno" class="col-sm-5 col-form-label offset-sm-3"><h4>¿Alumno ya registrado? Entonces: </h4></label>
                        <div class="col-sm-4">
                            <button style="margin-top: 10px" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalalumnos" name="btncargar" id="btncargar">Busca Alumno</button>
                        </div>
                    </div>
                </div>
                <div class="container border border-primary rounded" style="margin-top: 10px" >
                    <div class="text-primary" style="text-align:center">
                        <h1 >Nuevo Alumno</h1>
                    </div>
                    <form id="alumnodatos">
                        <div class="form-group row">
                            <label for="nombres" class="col-sm-2 col-form-label">Nombres: </label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="txtNombres" id="txtNombres" placeholder="Primer y/o Segundo Nombre">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="apellidos" class="col-sm-2 col-form-label">Apellidos: </label>

                            <div class="col">
                                <input type="text" class="form-control" id="txtapepaterno" name="txtapepaterno" placeholder="Apellido Paterno">
                            </div>
                            <div class="col">
                                <input type="text" class="form-control" id="txtapematerno" name="txtapematerno" placeholder="Apellido Materno">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="telefono" class="col-sm-2 col-form-label">Teléfono: </label>

                            <div class="col-sm-2">
                                <input type="text" class="form-control" name="txttelefono" id="txttelefono" placeholder="Nro Fijo o Celular">    
                            </div>
                            <div class="col-sm-1">
                                <label for="dni" class="col col-form-label">DNI: </label>
                            </div>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" name="txtdni" id="txtdni" placeholder="Nro de DNI">
                            </div>
                         <div class="col-sm-2">
                                <label for="correo" class="col col-form-label">Correo: </label>
                            </div>
                            <div class="col-sm-3">
                                <input type="text" class="form-control" name="txtemail" id="txtemail" placeholder="Ejem: hola@unprg.com">
                            </div>

                        </div>
                        <div class="form-group row">
                            <label for="universidad" class="col-sm-3 col-form-label">Universidad de Origen:</label>
                            <div class="col-sm-4">
                                <select onchange="llenar1(this.value)">
                                    <option>---Selecione Universidad---</option>
                                    <option>Universidad Nacional Pedro Ruiz Gallo</option>
                                    <option>Otra universidad</option>
                                </select>
                                <input type="hidden" class="form-control" name="txtUniver" id="txtUniver">
                            </div>
                            <div class="col-sm-2">
                                <label for="grado" class="col col-form-label">Grado académico: </label>
                            </div>
                            <div class="col-sm-3">
                                <select onchange="llenar2(this.value)">
                                    <option>---Selecione Grado---</option>
                                    <option>Bachiller</option>
                                    <option>Estudiante de ultimo Ciclo</option>
                                </select>
                                <input type="hidden" class="form-control" name="txtGrado" id="txtGrado">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="facultad" class="col-sm-2 col-form-label">Facultad:</label>
                            <div class="col-sm-4">
                                <select id="comboF" onchange="llenar4(this.value)">

                                </select>
                                <input type="hidden" class="form-control" name="txtFacultad" id="txtFacultad">
                            </div>
                            <div class="col-sm-2">
                                <label for="escuela" class="col-form-label">Escuela Profesional:</label>
                            </div>
                            <div class="col-sm-4">
                                <select id="comboE" onchange="llenar3(this.value)">

                                </select>
                                <input type="hidden" class="form-control" name="txtEscuela" id="txtEscuela">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-sm-4">
                                <button type="button" id="btnValidar" class="btn btn-primary">Validar Datos</button>
                                <input type="hidden" class="form-control" name="filtro" id="orden" value="Guardar">
                                <input type="hidden" class="form-control" name="txtEstadot" id="txtEstadot">
                            </div>
                            <div class="col-sm-4 offset">
                                <h5 id="respuestadatos"></h5>
                            </div>
                        </div>
                    </form>
                </div>

                <div class="col-sm-4 offset-sm-5">
                    <button style="margin-top: 25px" type="button" id="btnGuardar" class="btn btn-primary">Continuar</button>
                    <button style="margin-top: 25px" type="button" id="btnGuardar2" class="btn btn-primary">Continuar</button>

                </div>
            </div>

            <div id="numero2">
                <div class="container border border-primary rounded" style="margin-top: 10px" >
                    <div class="text-primary" style="text-align:center">
                        <h1 >Constancia de No Duplicidad</h1>
                    </div>
                    <form id="alumnoconstancia">
                        <div class="form-group row">
                            <label for="Constancia" class="col-sm-2 col-form-label">N° de Constancia: </label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="txtNroConstancia" id="txtNroConstancia" placeholder="Ejem: 019-2018-FICSA-UI">
                            </div>
                            <div class="col-sm-2">
                                <label for="Constancia" class="col-form-label">N° de Expediente: </label>
                            </div>
                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="txtNroExpediente" id="txtNroExpediente" placeholder="Ejem: 288-2018-FICSA-UI">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="titulo" class="col-sm-2 col-form-label">Titulo del proyecto: </label>

                            <div class="col">
                                <textarea class="form-control" id="txttitulo" name="txttitulo" placeholder="Titulo del tema propuesto" rows="3"></textarea>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="codigoP" class="col-sm-2 col-form-label">Codigo del proyecto: </label>

                            <div class="col-sm-4">
                                <input type="text" class="form-control" name="txtcodigoP" id="txtcodigoP" placeholder="Ejem: IS-2018-022">    
                            </div>
                            <div class="col-sm-2 offset-sm-2">
                                <button type="button" class="btn btn-success" id="btnCotejar">Buscar Similitudes</button>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="dictamen" class="col-sm-2 col-form-label">Dictamen:</label>
                            <div class="col">
                                <textarea class="form-control" style="font-weight: bold;" disabled="" id="txtdictamen" name="txtdictamen" placeholder="Esperando Respuesta ..." rows="2"></textarea>
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="asesor" class="col-sm-2 col-form-label">Asesor:</label>
                            <div class="col-sm-4">
                                <select id="comboD" onchange="llenar5(this.value)">

                                </select>
                                <input type="hidden" class="form-control" name="txtDocente" id="txtDocente">
                                <input type="hidden" class="form-control" name="txtrelacionresultado" id="txtrelacionresultado">
                                <input type="hidden" class="form-control" name="txtrelacion" id="txtrelacion">
                                <input type="hidden" name="CODIGOALUMNO" id="CODIGOALUMNO">
                                <input type="hidden" name="CODIGOREPORTE" id="CODIGOREPORTE">
                            </div>
                            <div class="col-sm-2">
                                <label for="linea" class="col-form-label">Linea: </label>
                            </div>
                            <div class="col-sm-4">
                                <select id="comboL" onchange="llenar6(this.value)">

                                </select>
                                <input type="hidden" class="form-control" name="txtLinea" id="txtLinea">
                            </div>
                        </div>
                        <div class="form-group row">
                            <label for="fecha" class="col-sm-3 col-form-label">Fecha del Dictamen:</label>
                            <div class="col-sm-4">         
                                <input type="text" disabled="" class="form-control" name="txtfecha" id="txtfecha">
                                <input type="hidden" class="form-control" name="fechaformat" id="fechaformat">
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-sm-4">
                                <button type="button" id="btnValidarConstancia" class="btn btn-primary">Validar Datos</button>
                                <input type="hidden" class="form-control" name="filtro" id="orden" value="Guardar">
                                <input type="hidden" class="form-control" name="txtDICTAMEN" id="txtDICTAMEN">
                            </div>
                            <div class="col-sm-5 offset">
                                <h5 id="respuestadatos2"></h5>
                            </div>
                            <div class="col-sm-3 offset" id="dd">
                                
                            </div>
                        </div>
                    </form>
                </div>

                <div class="col-sm-4 offset-sm-5">
                    <button style="margin-top: 25px" type="button" id="btnGuardar3" class="btn btn-primary"><a class="text-white" href="NombramientoJurado.jsp">Continuar</a></button>
                </div>
            </div>

            <div class="col-sm-8 offset-sm-2">
                <div style="margin-top: 25px" class="progress" id="progres1">
                    <div class="progress-bar" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
                </div>
                <div style="margin-top: 25px" class="progress" id="progres2">
                    <div class="progress-bar progress-bar-striped bg-warning" role="progressbar" style="width: 16%" aria-valuenow="16" aria-valuemin="0" aria-valuemax="100">Paso 1</div>
                </div>
                <div style="margin-top: 25px" class="progress" id="progres6">
                    <div class="progress-bar progress-bar-striped bg-success" role="progressbar" style="width: 16%" aria-valuenow="16" aria-valuemin="0" aria-valuemax="100">Paso 1 Listo</div>
                </div>
                <div style="margin-top: 25px" class="progress" id="progres3">
                    <div class="progress-bar progress-bar-striped bg-success" role="progressbar" style="width: 33%" aria-valuenow="33" aria-valuemin="0" aria-valuemax="100">Primera Etapa Completada</div>
                </div>
                <div style="margin-top: 25px" class="progress" id="progres7">
                    <div class="progress-bar progress-bar-striped bg-warning" role="progressbar" style="width: 49%" aria-valuenow="49" aria-valuemin="0" aria-valuemax="100">Paso 2 Listo</div>
                </div>
                <div style="margin-top: 25px" class="progress" id="progres4">
                    <div class="progress-bar progress-bar-striped bg-success" role="progressbar" style="width: 66%" aria-valuenow="66" aria-valuemin="0" aria-valuemax="100">Segunda Etapa Completada</div>
                </div>
                <div style="margin-top: 25px" class="progress" id="progres5">
                    <div class="progress-bar progress-bar-striped bg-success" role="progressbar" style="width: 100%" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"></div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="modalalumnos" tabindex="-1" role="dialog"> 
            <div class="modal-dialog" role="document">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 class="modal-title">ALUMNOS</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>


                    </div>
                    <div class="modal-body">
                        <form  id="BuscaAlumno">
                            <div class="row">
                                <div class="col-8">
                                    <div class="input-group input-group-sm mb-2">
                                        <input type="text" class="form-control input-sm mr-2" aria-label="Small" aria-describedby="inputGroup-sizing-sm" name="txtNombre" id="txtNombre" placeholder="Datos">
                                        <input type="button" class="btn btn-primary btn-sm"  id="btnBuscar2" aria-label="Small" title="Buscar Alumno" value="Buscar">
                                        <input type="hidden" class="form-control" name="filtro" id="orden" value="Cargar">
                                    </div>
                                </div>

                            </div>
                        </form>

                        <form id="frmAlumnoss">
                            <table class="table table-responsive table-bordered table-hover table-sm" id="tablaAlumnos">
                                <thead class="thead-dark">
                                    <tr class="table-info">
                                        <th class="ocultar">codigo</th>
                                        <th>Escoger:</th>
                                        <th>NOMBRES</th>
                                        <th>A.PATERNO</th>
                                        <th>A.MATERNO</th>
                                        <th>ESTADO</th>
                                        <th>DNI</th>
                                        <th>EMAIL</th>
                                        <th>CELULAR</th>
                                        <th>G.ACADEMICO</th>
                                        <th>PROCEDENCIA</th>
                                        <th>ESCUELA</th>
                                    </tr>
                                </thead>
                                <tbody id="tablitaAlumnos">

                                </tbody>

                            </table>
                        </form>
                        
                    </div>
                </div>
            </div>
        </div>

        

    </body>
    <script src="js/apps/jquery-3.3.1.min.js"></script>
    <script src="js/apps/Alumnno.js" type="text/javascript"></script>
    <script src="js/apps/Constancia.js" type="text/javascript"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
    <script src="/Proyecto_Taller/js/bootstrap.min.js"></script>
</html>