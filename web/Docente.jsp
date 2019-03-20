<%-- 
    Document   : Docente
    Created on : 20/06/2018, 06:58:19 PM
    Author     : JhanxD
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.* , Control.Linea_InvestigacionCotroller, Control.DocenteCotroller"%>
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
    .table1{
        height: 300px;
        overflow-y: auto;
        overflow-x: auto;
    }
    .table2{
        height: 100px;
        overflow-y: auto;
        overflow-x: auto;
    }
</style>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ESCALAFON</title>
        <link rel="stylesheet" href="/Proyecto_Taller/css/bootstrap.min.css"/>
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-light bg-light">
            <%@ include file = "../BarraNav.jsp" %> 
        </nav>

        <h1 style="text-align: center">DOCENTES REGISTRADOS</h1>
        <div class="container">
            <form  id="BuscaDocente">
                <div class="row">
                    <div class="col-4">
                        <div class="input-group input-group-sm mb-3">
                            <input type="text" class="form-control input-sm mr-2" aria-label="Small" aria-describedby="inputGroup-sizing-sm" name="txtNombre" id="txtNombre" placeholder="Nombre">
                            <input type="button" class="btn btn-primary btn-sm"  id="btnBuscar" aria-label="Small" title="Buscar Linea de Investigacion" value="Buscar">
                        </div>
                    </div>

                </div>
            </form>
            <form  id="tablita">
                <div class="col-12">
                    <table class="table table-responsive table-bordered table-hover table-sm" id="tabla">
                        <thead class="thead-dark">
                            <tr class="table-info">
                                <th class="ocultar">codigo</th>
                                <th>NOMBRE</th>
                                <th>A.PATERNO</th>
                                <th>A.MATERNO</th>
                                <th>G.ACADEMICO</th>
                                <th>CATEGORIA</th>
                                <th>EMAIL</th>
                                <th>ESCUELA</th>
                                <th>F.INGRESO</th>
                                <th style="width: 6%; text-align: center;">LINEAS</th>
                                <th style="width: 4%; text-align: center;">EDIT</th>
                                <th style="width: 4%; text-align: center;">ELI</th>
                            </tr>
                        </thead>
                        <tbody id="registrotabla">

                        </tbody>
                    </table>
                </div>
            </form>
            <div class="container">
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalnuevo">
                    Nuevo Docente
                </button>
            </div>
        </div>

        <div class="modal fade" id="modalnuevo" tabindex="-1" role="dialog"> 
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Nuevo Docente</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="frmnuevo">

                            <table class="table">
                                <tbody>
                                    <tr>
                                        <td>Escuela</td>
                                        <td><select id="combo" onchange="llenarcajita1(this.value)">
                                                <option>---Selecione una Escuela---</option></select></td>
                                        <td><input class="ocultar" id="rsta" name="rsta" type="text"></td>
                                    </tr>
                                    <tr>
                                        <td>Nombres</td>
                                        <td><label for="nombre"></label><input type="text" name="nombren" id="nombren"></td>
                                    </tr>
                                    <tr>
                                        <td>Apellido Paterno</td>
                                        <td><label for="APaterno"></label><input type="text" name="APaternon" id="APaternon"></td>
                                    </tr>
                                    <tr>
                                        <td>Apellido Materno</td>
                                        <td><label for="AMaterno"></label><input type="text" name="AMaternon" id="AMaternon"></td>
                                    </tr>
                                    <tr>
                                        <td>Grado Academico</td>
                                        <td><select id="combogrado" onchange="llenarcajita2(this.value)">
                                                <option>---Selecione un Grado---</option>
                                                <option  value="Doctor">Doctor</option>
                                                <option  value="Magister">Magister</option>
                                                <option  value="Ingeniero">Ingeniero</option></select></td>
                                        <td><label for="GAcademico"></label><input class="ocultar" type="text" name="GAcademicon" id="GAcademicon"></td>
                                    </tr>
                                    <tr>
                                        <td>Categoria</td>
                                        <td><select id="combocategoria" onchange="llenarcajita3(this.value)">
                                                <option>---Selecione una Categoria---</option>
                                                <option  value="ASOCIADO">ASOCIADO</option>
                                                <option  value="AUXILIAR">AUXILIAR</option>
                                                <option  value="PRINCIPAL">PRINCIPAL</option></select></td>
                                        <td><label for="Categoria"></label><input class="ocultar" type="text" name="categorian" id="categorian"></td>
                                    </tr>
                                    <tr>
                                        <td>E_Mail</td>
                                        <td><label for="mail"></label><input type="text" name="mailn" id="mailn"></td>
                                    </tr>
                                    <tr>
                                        <td>Fecha de Ingreso</td>
                                        <td><label for="FIngreso"></label><input type="date" name="FIngreson" id="FIngreson"></td>
                                    </tr>
                                </tbody>
                                <tr class="modal-footer">
                                    <td><input type="button" class="btn btn-primary btn-sm"  id="guardar" aria-label="Small" title="Guardar Docente" value="Agregar"></td>
                                <input type="hidden" name="filtro" id="orden" value="Agregar">
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
                        <h5 class="modal-title">Modificar Docente</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="frmcambio">

                            <table class="table">
                                <tbody>
                                    <tr><input type="hidden" name="codigoe" id="codigoe">
                                <td>Escuela</td>
                                <td><select id="combo1" onchange="llenarcajita4(this.value)">
                                        <option>---Selecione una Escuela---</option></select></td>
                                <td><input class="ocultar" id="rstae" name="rstae" type="text"></td>
                                </tr>
                                <tr>
                                    <td>Nombres</td>
                                    <td><label for="nombre"></label><input type="text" name="nombree" id="nombree"></td>
                                </tr>
                                <tr>
                                    <td>Apellido Paterno</td>
                                    <td><label for="APaterno"></label><input type="text" name="APaternoe" id="APaternoe"></td>
                                </tr>
                                <tr>
                                    <td>Apellido Materno</td>
                                    <td><label for="AMaterno"></label><input type="text" name="AMaternoe" id="AMaternoe"></td>
                                </tr>
                                <tr>
                                    <td>Grado Academico</td>
                                    <td><select id="combogrado" onchange="llenarcajita2(this.value)">
                                            <option>---Selecione un Grado---</option>
                                            <option  value="Doctor">Doctor</option>
                                            <option  value="Magister">Magister</option>
                                            <option  value="Ingeniero">Ingeniero</option></select></td>
                                    <td><label for="GAcademico"></label><input class="ocultar" type="text" name="GAcademicoe" id="GAcademicoe"></td>
                                </tr>
                                <tr>
                                    <td>Categoria</td>
                                    <td><select id="combocategoria" onchange="llenarcajita3(this.value)">
                                            <option>---Selecione una Categoria---</option>
                                                <<option>---Selecione una Categoria---</option>
                                                <option  value="ASOCIADO">ASOCIADO</option>
                                                <option  value="AUXILIAR">AUXILIAR</option>
                                                <option  value="PRINCIPAL">PRINCIPAL</option></select></td>
                                    <td><label for="Categoria"></label><input class="ocultar" type="text" name="categoriae" id="categoriae"></td>
                                </tr>
                                <tr>
                                    <td>E_Mail</td>
                                    <td><label for="mail"></label><input type="text" name="maile" id="maile"></td>
                                </tr>
                                <tr>
                                    <td>Fecha de Ingreso</td>
                                    <td><label for="FIngreso"></label><input type="date" name="FIngresoe" id="FIngresoe"></td>
                                </tr>
                                </tbody>
                                <tr class="modal-footer">
                                    <td><input type="button" class="btn btn-primary btn-sm"  id="editar" aria-label="Small" title="Modificar Docente" value="Editar"></td>
                                <input type="hidden" name="filtro" id="orden" value="Editar">
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
                        <h5 class="modal-title">¿Realmente desea eliminar este Docente?</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="frmeliminar">

                            <table class="table">
                                <tbody>
                                    <tr><input type="hidden" name="codigod" id="codigod">
                                <td>Nombres</td>
                                <td><label for="nombre"></label><input disabled="" type="text" name="nombred" id="nombred"></td>
                                </tr>
                                <tr>
                                    <td>Apellido Paterno</td>
                                    <td><label for="APaterno"></label><input disabled="" type="text" name="APaternod" id="APaternod"></td>
                                </tr>
                                <tr>
                                    <td>Apellido Materno</td>
                                    <td><label for="AMaterno"></label><input disabled="" type="text" name="AMaternod" id="AMaternod"></td>
                                </tr>

                                </tbody>
                                <tr class="modal-footer">
                                    <td><input type="button" class="btn btn-primary btn-sm"  id="eliminar" aria-label="Small" title="Eliminar Docente" value="Eliminar"></td>
                                <input type="hidden" name="filtro" id="orden" value="Eliminar">
                                </tr>
                            </table>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="modallineas" tabindex="-1" role="dialog"> 
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">LINEAS DE INVESTIGACION</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form id="frmelineas">
                            <h5 id="NombreDocente"></h5>
                            <input  class="ocultar" name="codigol" id="codigol">
                            <input  class="ocultar" name="escuelal" id="escuelal">
                            <input type="hidden" name="docentel" id="docentel">
                            <table class="table table-responsive table-bordered table-hover table-sm table1" id="tablaLineas">
                                <thead class="thead-dark">
                                    <tr class="table-info">
                                        <th class="ocultar">codigo</th>
                                        <th>LINEAS</th>
                                        <th style="width: 8%; text-align: center;">ELI</th>
                                    </tr>
                                </thead>
                                <tbody id="tablitaLineas">

                                </tbody>
                            </table>
                        </form>
                        <div class="container">
                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalnuevaLinea" id="alineass">
                                Asignar Linea
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="modalnuevaLinea" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <form id="relaciones">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Asignar Linea</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        
                        <table class="table2">
                                <tbody>
                                    <tr>
                                        <td>Linea de Investigación: </td>
                                    </tr>
                                    <tr>
                                        <td><select id="combo5" onchange="llenarcajita5(this.value)">
                                                </select></td>
                                        <td><input  class="ocultar" id="rstaL" name="rstaL" type="text"></td>
                                        <td><input  class="ocultar" id="rstaD" name="rstaD" type="text"></td>
                                        <td><input  class="ocultar" id="rstaE" name="rstaE" type="text"></td>
                                        
                                    </tr>
                                </tbody>
                        </table>
                        
                    </div>
                    <div class="modal-footer">
                        <input type="button" class="btn btn-primary btn-sm"  id="guardarRelacion" aria-label="Small" title="Asigar Linea" value="Asignar">
                                <input type="hidden" name="filtro" id="orden" value="AgregarRelacion">
                    </div>
                </div>
                </form>
            </div>
        </div>


    </body>
    <script src="js/apps/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="js/apps/Docente.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/tether/1.4.0/js/tether.min.js" integrity="sha384-DztdAPBWPRXSA/3eYEEUWrWCy7G5KFbe8fFjk5JAIxUYHKkDx6Qin1DkWx51bBrb" crossorigin="anonymous"></script>
    <script src="/Proyecto_Taller/js/bootstrap.min.js"></script>
</html>