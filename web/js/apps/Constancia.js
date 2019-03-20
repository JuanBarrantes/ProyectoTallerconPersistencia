$(document).ready(function () {

    $('#btnGuardar3').hide();

    $.ajax({
        url: "Constancias",
        type: "post",
        data: {
            filtro: "ObtenerFecha"
        },
        success: function (jsonResponse) {
            var f1 = jsonResponse.FECHA1;
            var f2 = jsonResponse.FECHA2;
            $('#txtfecha').val(f1);
            $('#fechaformat').val(f2);
        }
    });




});

function BuscaRelacion() {
    $.ajax({
        url: "Constancias",
        type: "post",
        data: {

            filtro: "BuscaRelacion",
            codigo: $('#txtDocente').val(),
            codigo1: $('#txtLinea').val()
        },
        success: function (jsonResponse) {
            var respuesta = jsonResponse.RESPUESTA;
            var respuesta2 = jsonResponse.RESP2;
            $('#txtrelacionresultado').val(respuesta);
            $('#txtrelacion').val(respuesta2);

        }
    });



}
;

function validardatos2() {
    var nroConstancia, nroExpediente, titulo, codigo, dictamen, docente, linea, DIC, exprecons, exprescod, relacion;
    nroConstancia = document.getElementById("txtNroConstancia").value;
    nroExpediente = document.getElementById("txtNroExpediente").value;
    titulo = document.getElementById("txttitulo").value;
    codigo = document.getElementById("txtcodigoP").value;
    dictamen = document.getElementById("txtdictamen").value;
    docente = document.getElementById("txtDocente").value;
    linea = document.getElementById("txtLinea").value;
    DIC = document.getElementById("txtDICTAMEN").value;
    relacion = document.getElementById("txtrelacionresultado").value;
    exprecons = /\d+-\d+-+[A-Z]+-+[A-Z]/;
    exprescod = /[A-Z]+-+\d+-\d/;

    if (nroConstancia === "" || nroExpediente === "" || titulo === "" || codigo === "" || dictamen === "" || docente === "" || linea === "") {
        alert("Todos los campos son obligatorios");
        return false;
    } else if (DIC === "") {
        alert("Pulse el Boton Buscar Similitudes");
        return false;
    } else if (nroConstancia.length > 20) {
        alert("El campo Nro de Constancia solo permite 20 carácteres");
        return false;
    } else if (!exprecons.test(nroConstancia)) {
        alert("Codigo incorrecto en el campo Nro de Contancia, por favor siga el ejemplo");
        return false;
    } else if (nroExpediente.length > 20) {
        alert("El campo Nro de Expediente solo permite 20 carácteres");
        return false;
    } else if (!exprecons.test(nroExpediente)) {
        alert("Codigo incorrecto en el campo Nro de Expediente, por favor siga el ejemplo");
        return false;
    } else if (codigo.length > 20) {
        alert("El campo Codigo del Proyecto solo permite 20 carácteres");
        return false;
    } else if (!exprescod.test(codigo)) {
        alert("Codigo incorrecto en el campo Codigo del Proyecto, por favor siga el ejemplo");
        return false;
    } else if (titulo.length > 400) {
        alert("El campo Titulo del Proyecto solo permite 400 carácteres");
        return false;
    } else if (relacion != 1) {
        alert("Este Asesor no tiene esa Linea de Investigación asignada");
        return false;
    }
    return true;

}
;

function traercodigo (){
    var fila;
        $.ajax({
            url: "Constancias",
            type: "post",
            data: {

                filtro: "Cargar",
                txtNombre: $('#txtNroConstancia').val()
            },
            success: function (jsonResponse) {
                $.each(jsonResponse.DATA, function (index, value) {
                fila = value.idConstancia;
                $('#dd').html("<a target='_blank' href='Tesis?filtro=Reporte&codigo="+fila+"' class='btn btn-dark'>Descargar Constancia</a>");
                $('#CODIGOREPORTE').val(fila);
            
        });
            }
        });
};



$(function () {
    $('#btnValidarConstancia').click(function (e) {
        
        if (validardatos2()) {

            var dato = $('#alumnoconstancia').serialize();
            $.ajax({
                url: "Constancias",
                type: "post",
                data: dato,
                success: function (jsonResponse) {
                    var respta = jsonResponse.RESP;
                    var respta1 = jsonResponse.RESP.toLowerCase();
                    if (respta1 === "registrado") {
                        alert(respta);
                        $('#respuestadatos2').html("<p class='text-success'>✔ Campos Validados</p>");
                        $('#progres3').hide();
                        $('#progres7').show();
                        $('#btnGuardar3').show();
                        traercodigo();
                        
                    } else {
                        alert(respta);
                        $('#respuestadatos2').html("<p class='text-danger'>✘ Campos No Validados</p>");
                    }
                }
            });
        } else {
            $('#respuestadatos2').html("<p class='text-danger'>✘ Campos No Validados</p>");
        }
    });

    $('#btnGuardar2').click(function (e) {
        $.ajax({
            url: "Constancias",
            type: "post",
            data: {

                filtro: "CargaDocentes",
                codigo: $('#txtEscuela').val()
            },
            success: function (jsonResponse) {
                $('#numero1').hide();
                $('#numero2').show();
                $('#comboD').empty();
                llenarcomboDocentes(jsonResponse);
                $('#progres1').hide();
                $('#progres3').show();
            }
        });
    });

    $('#btnGuardar').click(function (e) {
        $.ajax({
            url: "Constancias",
            type: "post",
            data: {

                filtro: "CargaDocentes",
                codigo: $('#txtEscuela').val()
            },
            success: function (jsonResponse) {
                $('#comboD').empty();
                llenarcomboDocentes(jsonResponse);
                $('#progres1').hide();
                $('#progres6').hide();
                $('#progres2').hide();
                $('#progres3').show();

            }
        });
    });
    
    $('#btnCotejar').click(function (e) {
        $.ajax({
            url: "Constancias",
            type: "post",
            data: {

                filtro: "CotejarTitulo",
                codigo: $('#txttitulo').val()
            },
            success: function (jsonResponse) {
                var respuesta = jsonResponse.RESPUESTA;
                if (respuesta == 0) {
                    $('#txtdictamen').empty();
                    $('#txtdictamen').val("NO EXISTE DUPLICIDAD DEL PROYECTO DE INVESTIGACION EN LA BASE DE DATOS DE LA UNIDAD DE INVESTIGACION  DE LA FICSA");
                    $('#txtDICTAMEN').val("1");
                } else {
                    $('#txtdictamen').empty();
                    $('#txtdictamen').val("EXISTE DUPLICIDAD DEL PROYECTO DE INVESTIGACION EN LA BASE DE DATOS DE LA UNIDAD DE INVESTIGACION  DE LA FICSA");
                    $('#txtDICTAMEN').val("0");
                }

            }
        });
    });


});

function llenarcomboDocentes(jsonResponse) {
    var fila1, fila;
    fila = "<option>" + "---Selecione el Asesor---" + "</option>";
    $('#comboD').append(fila);
    $.each(jsonResponse.DATA, function (index, value) {
        fila1 = "<option  value='" + value.idDocente + "'>" + value.aPaterno+" "+value.aMaterno+" "+value.nombre+ "</option>";
        $('#comboD').append(fila1);
    });
}
;

function llenarcomboLinea(jsonResponse) {
    var fila1, fila;
    fila = "<option>" + "---Selecione la Linea de Investigación---" + "</option>";
    $('#comboL').append(fila);
    $.each(jsonResponse.DATA, function (index, value) {
        fila1 = "<option  value='" + value.lineaId.idLinea + "'>" + value.lineaId.nombre + "</option>";
        $('#comboL').append(fila1);
    });
}
;

function llenar5(dato) {
    $('#txtDocente').val(dato);
    $.ajax({
        url: "Constancias",
        type: "post",
        data: {
            filtro: "CargaLineas",
            codigo: dato
        },
        success: function (jsonResponse) {
            $('#comboL').empty();
            llenarcomboLinea(jsonResponse);
        }
    });


}
;

function llenar6(dato) {
    $('#txtLinea').val(dato);
    BuscaRelacion();
}


