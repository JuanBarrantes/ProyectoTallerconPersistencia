$(document).ready(function () {


    var orden = $('#btnBuscar').val();
    $.ajax({
        url: "Escuelas",
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

function llenacombo(jsonResponse){
   
        var fila1;
        $.each(jsonResponse.DATA2, function (index, value){
            fila1="<option  value='"+value.idFacultad+"'>"+value.abreviatura+"</option>";
            $('#combo').append(fila1);
            $('#combo1').append(fila1);
        });
    
}
;

function llenarcajita(dato){
    //alert("helou");
    $('#rsta').val(dato);
    $('#rsta1').val(dato);
};

function llenartabla(jsonResponse) {
        var fila;

        $.each(jsonResponse.DATA, function (index, value) {
            fila = "<tr idfacultad='" + value.idEscuela + "'>" +
                    "<th class='align-middle'><input type='hidden'>" + value.abreviatura + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.nombre + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.facultad.abreviatura + "</th>" +
                    "<th class='align-middle' style='text-align:center'><input type='button' class='btn btn-primary editar' value='Editar'></th>" +
                    "<th class='align-middle' style='text-align:center'><input type='button' class='btn btn-danger eliminar' value='Eliminar'></th></tr>";

            $('#registrotabla').append(fila);
        });
    }
;


$(function () {
    $('#btnBuscar').click(function (e) {
        var nombre = $('#txtNombre').val();
        var orden = $('#btnBuscar').val();
        $.ajax({
            url: "Escuelas",
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
        if (nombre.length > 1 && nombre.length <= 80) {
            if (valido.test(nombre)) {
                if (abre.length > 1 && abre.length <= 10) {
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
        var nombre = $('#nombren').val();
        var abrev = $('#abreviaturan').val();
        var orden = $('#guardar').val();
        var facul=$('#rsta').val();

        if (validar_campos(nombre, abrev)) {
            $.ajax({
                url: "Escuelas",
                type: "post",
                data: {
                    filtro: orden,
                    filtro2: nombre,
                    filtro3: abrev,
                    filtro4: facul
                },
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
        }
    });







    $('#editar').click(function (e) {
        e.preventDefault();
        var nombre = $('#txtNombreER').val();
        var abrev = $('#txtNombreAbreviadoER').val();
        var orden = $('#editar').val();
        var codiv = $('#txtIdEscuelaER').val();
        var facul=$('#rsta1').val();
        //llenarmodal("gfjnghn","dbd");
        if (validar_campos(nombre, abrev)) {
            $.ajax({
                url: "Escuelas",
                type: "post",
                data: {
                    filtro: orden,
                    filtro1: codiv,
                    filtro2: nombre,
                    filtro3: abrev,
                    filtro4: facul
                },
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
        }
    });


    $('#eliminar').click(function (e) {
        e.preventDefault();
        //var nombre = $('#txtNombreER2').val();
        //var abrev = $('#txtNombreAbreviadoER2').val();
        var orden = $('#eliminar').val();
        var codiv = $('#txtIdFacultadER2').val();
        //llenarmodal("gfjnghn","dbd");
        
            $.ajax({
                url: "Escuelas",
                type: "post",
                data: {
                    filtro: orden,
                    filtro1: codiv
                  //  filtro2: nombre,
                  //  filtro3: abrev

                },
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
    var botones = document.getElementsByClassName("editar");
    for (var i = 0; i < botones.length; i++) {
        botones[i].addEventListener("click", function () {
            var array_tr = this.parentElement.parentElement.children;
            var data = {"nombre": array_tr[1].innerText, "abreviado": array_tr[0].innerText};
            $('#txtIdEscuelaER').val($(this.parentElement.parentElement).attr("idfacultad"));
            $('#txtNombreER').val(data.nombre);
            $('#txtNombreAbreviadoER').val(data.abreviado);
            // $('#accionModal').val("Editar");
            $("#modaleditar").modal("show");
            document.getElementsByTagName("body")[0].style.paddingRight = "0";
            // $('#txtTituloModalMan').html("<i class='icon-graduation' aria-hidden='true'></i> Editar Facultad");
        });

    }
    var botones = document.getElementsByClassName("eliminar");
    for (var i = 0; i < botones.length; i++) {
        botones[i].addEventListener("click", function () {
            var array_tr = this.parentElement.parentElement.children;
            var data = {"nombre": array_tr[1].innerText, "abreviado": array_tr[0].innerText, "facul":array_tr[2].innerText};
            $('#txtIdFacultadER2').val($(this.parentElement.parentElement).attr("idfacultad"));
            $('#txtNombreER2').val(data.nombre);
            $('#txtNombreAbreviadoER2').val(data.abreviado);
            $('txtNombreAbreviadoER22').val(data.abreviado);
            //$('#accionModal2').val("Eliminar");
            $("#modaleliminar").modal("show");
            document.getElementsByTagName("body")[0].style.paddingRight = "0";
            // $('#txtTituloModalMan').html("<i class='icon-graduation' aria-hidden='true'></i> Editar Facultad");

        });
    }
}
;


