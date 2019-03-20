$(document).ready(function () {


    var orden = $('#btnBuscar').val();
    $.ajax({
        url: "Docentes",
        type: "post",
        data: {
            filtro: orden
        },
        success: function (jsonResponse) {
            $("#registrotabla").empty();
            llenartabla(jsonResponse);
            llenacombo(jsonResponse);
            agregarEventos();
            $('#txtNombre').focus();
        }
    });



});

function llenacombo(jsonResponse) {

    var fila1;
    $.each(jsonResponse.DATA2, function (index, value) {
        fila1 = "<option  value='" + value.idEscuela + "'>" + value.abreviatura + "</option>";
        $('#combo').append(fila1);
        $('#combo1').append(fila1);
    });

}
;

function llenacombo2(jsonResponse) {

    var fila1, fila;
    fila = "<option>"+"---Selecione una Linea---"+"</option>";
        $('#combo5').append(fila);
    $.each(jsonResponse.DATA2, function (index, value) {
        fila1 = "<option  value='" + value.idLinea + "'>" + value.nombre + "</option>";
        $('#combo5').append(fila1);
        $('#combo6').append(fila1);
    });

}
;

function llenarcajita1(dato) {
    $('#rsta').val(dato);
}
;

function llenarcajita4(dato) {
    $('#rstae').val(dato);
}
;

function llenarcajita5(dato) {
    $('#rstaL').val(dato);
}
;

function llenarcajita2(dato) {
    $('#GAcademicon').val(dato);
    $('#GAcademicoe').val(dato);
}
;

function llenarcajita3(dato) {
    $('#categorian').val(dato);
    $('#categoriae').val(dato);
}
;

function llenartabla(jsonResponse) {

        var fila;

        $.each(jsonResponse.DATA, function (index, value) {
            fila = "<tr idfacultad='" + value.idDocente + "'>" +
                    "<th class='align-middle'><input type='hidden'>" + value.nombre + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.aPaterno + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.aMaterno + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.gradoAcademico + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.categoria + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.email + "</th>" +
                    "<th class='align-middle ocultar'><input type='hidden'>" + value.escuela.idEscuela + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.escuela.abreviatura + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.fechaIngreso + "</th>" +
                    "<th class='align-middle' style='text-align:center'><input type='button' class='btn btn-warning lineas' value='Lineas'></th>" +
                    "<th class='align-middle' style='text-align:center'><input type='button' class='btn btn-primary editar' value='Editar'></th>" +
                    "<th class='align-middle' style='text-align:center'><input type='button' class='btn btn-danger eliminar' value='Eliminar'></th></tr>";

            $('#registrotabla').append(fila);
        });
    
}

function ajaxlineas() {
    var orden = "OrdenarLineas";
    var codigo = $('#codigol').val();
    var escula = $('#escuelal').val();
    $.ajax({
        url: "Docentes",
        type: "post",
        data: {
            escuelall: escula,
            codigol1: codigo,
            filtro: orden
        },
        success: function (jsonResponse) {
            $("#tablitaLineas").empty();
            llenartabladelineas(jsonResponse);
            $("#combo5").empty();
            llenacombo2(jsonResponse);
            agregarEventosdeLineas();
        }
    });
}

function llenartabladelineas(jsonResponse) {

        var fila1;
        $.each(jsonResponse.DATA, function (index, value) {
            fila1 = "<tr idLineasss='" + value.idRelacion + "'>" +
                    "<th class='align-middle'><input type='hidden'>" + value.lineaId.nombre + "</th>" +
                    "<th class='align-middle' style='text-align:center'><input type='button' class='btn btn-danger eliminarL' value='Eliminar'></th></tr>";
            $('#tablitaLineas').append(fila1);
        });
    
}

$(function () {
    
    $('#btnBuscar').click(function (e) {
        var nombre = $('#txtNombre').val();
        var orden = $('#btnBuscar').val();
        $.ajax({
            url: "Docentes",
            type: "post",
            data: {
                txtNombre: nombre,
                filtro: orden
            },
            success: function (jsonResponse) {
                $("#registrotabla").empty();
                llenartabla(jsonResponse);
                llenacombo(jsonResponse);
                agregarEventos();
                $('#txtNombre').focus();

            }
        });
    });

    function validar_campos(nombre, abre) {
        var valido = /^[a-z-A-Z- ]{1,80}$/;
        if (nombre.length > 1 && nombre.length <= 100) {
            if (valido.test(nombre)) {
                if (abre.length > 1 && abre.length <= 100) {
                    if (valido.test(abre)) {
                        $('#modalnuevo').modal("hide");
                        $('#modaleditar').modal("hide");
                        return true;
                    } else {
                        alert("Solo se permiten letras en la abreviatura");
                        //return false;
                    }

                } else {
                    alert("Escribe un abreviatura valido");
                    //return false;
                }

            } else {
                alert("Solo se permiten letras en el nombre");
                //return false;
            }

        } else {
            alert("Escribe un nombre valido");
            //return false;
        }//return false;
    }
    ;



    $('#guardar').click(function (e) {
        e.preventDefault();
        var datos = $('#frmnuevo').serialize();
        $('#modalnuevo').modal("hide");

        $.ajax({
            url: "Docentes",
            type: "post",
            data: datos,
            success: function (jsonResponse) {
                var mensajeRoot = jsonResponse.MENSAJEROOT.toLowerCase();
                if (mensajeRoot === "transaccion") {
                    var mensajeAccion = jsonResponse.MENSAJEACCION.toLowerCase();
                    if (mensajeAccion === "registrado") {
                        $("#registrotabla").empty();
                        llenartabla(jsonResponse);
                    } else {
                        alert(mensajeAccion.toUpperCase());
                    }
                } else {
                    $("#registrotabla").empty();
                    llenartabla(jsonResponse);
                }

                agregarEventos();
                $('#txtNombre').focus();
            }
        });

    });

$('#guardarRelacion').click(function (e) {
        e.preventDefault();
        var datos = $('#relaciones').serialize();
        $('#modalnuevaLinea').modal("hide");

        $.ajax({
            url: "Docentes",
            type: "post",
            data: datos,
            success: function (jsonResponse) {
                var mensajeRoot = jsonResponse.MENSAJEROOT.toLowerCase();
                if (mensajeRoot === "transaccion") {
                    var mensajeAccion = jsonResponse.MENSAJEACCION.toLowerCase();
                    if (mensajeAccion === "registrado") {
                        $("#tablitaLineas").empty();
            llenartabladelineas(jsonResponse);
                    } else {
                        alert(mensajeAccion.toUpperCase());
                    }
                } else {
                    $("#tablitaLineas").empty();
            llenartabladelineas(jsonResponse);
            
                }

                agregarEventosdeLineas();
                $('#txtNombre').focus();
            }
        });

    });








    $('#editar').click(function (e) {
        e.preventDefault();
        var datos = $('#frmcambio').serialize();
        $('#modaleditar').modal("hide");

        $.ajax({
            url: "Docentes",
            type: "post",
            data: datos,
            success: function (jsonResponse) {
                var mensajeRoot = jsonResponse.MENSAJEROOT.toLowerCase();
                if (mensajeRoot === "transaccion") {
                    var mensajeAccion = jsonResponse.MENSAJEACCION.toLowerCase();
                    if (mensajeAccion === "modificado") {
                        $("#registrotabla").empty();
                        llenartabla(jsonResponse);
                    } else {
                        alert(mensajeAccion.toUpperCase());
                    }
                } else {
                    $("#registrotabla").empty();
                    llenartabla(jsonResponse);
                }

                agregarEventos();
                $('#txtNombre').focus();
            }
        });

    });


    $('#eliminar').click(function (e) {
        e.preventDefault();
        var datos = $('#frmeliminar').serialize();
        $('#modaleliminar').modal("hide");

        $.ajax({
            url: "Docentes",
            type: "post",
            data: datos,
            success: function (jsonResponse) {
                var mensajeRoot = jsonResponse.MENSAJEROOT.toLowerCase();
                if (mensajeRoot === "transaccion") {
                    var mensajeAccion = jsonResponse.MENSAJEACCION.toLowerCase();
                    if (mensajeAccion === "eliminado") {
                        $("#registrotabla").empty();
                        llenartabla(jsonResponse);
                    } else {
                        alert(mensajeAccion.toUpperCase());
                    }
                } else {
                    $("#registrotabla").empty();
                    llenartabla(jsonResponse);
                }

                $("#modaleliminar").modal("hide");

                agregarEventos();
                $('#txtNombre').focus();
            }
        });

    });

});

function agregarEventos() {
    var botones = document.getElementsByClassName("lineas");
    for (var i = 0; i < botones.length; i++) {
        botones[i].addEventListener("click", function () {
            var array_tr = this.parentElement.parentElement.children;
            var data = {"escuela": array_tr[6].innerText,"email": array_tr[5].innerText, "materno": array_tr[2].innerText, "paterno": array_tr[1].innerText, "nombre": array_tr[0].innerText};
            $('#codigol').val($(this.parentElement.parentElement).attr("idfacultad"));
            $('#rstaD').val($(this.parentElement.parentElement).attr("idfacultad"));
            $('#escuelal').val(data.escuela);
            $('#rstaE').val(data.escuela);
            $('#docentel').val(data.nombre);
            $('#NombreDocente').html("<i class='icon-graduation' aria-hidden='true'></i> Docente: " + data.nombre+" "+ data.paterno+" "+data.materno);
            ajaxlineas();
            $("#modallineas").modal("show");
            document.getElementsByTagName("body")[0].style.paddingRight = "0";
        });

    }
    var botones = document.getElementsByClassName("editar");
    for (var i = 0; i < botones.length; i++) {
        botones[i].addEventListener("click", function () {
            var array_tr = this.parentElement.parentElement.children;
            var data = {"email": array_tr[5].innerText, "materno": array_tr[2].innerText, "paterno": array_tr[1].innerText, "nombre": array_tr[0].innerText};
            $('#codigoe').val($(this.parentElement.parentElement).attr("idfacultad"));
            $('#nombree').val(data.nombre);
            $('#APaternoe').val(data.paterno);
            $('#AMaternoe').val(data.materno);
            $('#maile').val(data.email);
            // $('#accionModal').val("Editar");
            $("#modaleditar").modal("show");
            document.getElementsByTagName("body")[0].style.paddingRight = "0";
        });

    }
    var botones = document.getElementsByClassName("eliminar");
    for (var i = 0; i < botones.length; i++) {
        botones[i].addEventListener("click", function () {
            var array_tr = this.parentElement.parentElement.children;
            var data = {"materno": array_tr[2].innerText, "paterno": array_tr[1].innerText, "nombre": array_tr[0].innerText};
            $('#codigod').val($(this.parentElement.parentElement).attr("idfacultad"));
            $('#nombred').val(data.nombre);
            $('#APaternod').val(data.paterno);
            $('#AMaternod').val(data.materno);
            //$('#accionModal2').val("Eliminar");
            $("#modaleliminar").modal("show");
            document.getElementsByTagName("body")[0].style.paddingRight = "0";
        });
    }
}
;

function agregarEventosdeLineas() {
    
    
    var botones = document.getElementsByClassName("eliminarL");
    for (var i = 0; i < botones.length; i++) {
        botones[i].addEventListener("click", function () {
            var dato = $(this.parentElement.parentElement).attr("idLineasss");
            $.ajax({
                url: "Docentes",
                type: "post",
                data: {filtro: "AnularRelacion" , rstaR: dato, codigol1: $('#escuelal').val(), escuelall:$('#codigol').val() },
                success: function (jsonResponse) {
            $("#tablitaLineas").empty();
            llenartabladelineas(jsonResponse);
            $("#combo5").empty();
            llenacombo2(jsonResponse);
                    agregarEventosdeLineas();
            }
            });
            document.getElementsByTagName("body")[0].style.paddingRight = "0";


        });
    }
}
;
