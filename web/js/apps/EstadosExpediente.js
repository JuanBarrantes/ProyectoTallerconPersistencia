$(document).ready(function () {


});

function mostrarTabladeConstancias3(jsonResponse) {

    var fila;

    $.each(jsonResponse.DATA, function (index, value) {
        var dic = value.codigoC.dictamen;
        var d;
        if (dic == 1) {
            d = "APROBADO";
        } else {
            d = "RECHAZADO";
        }

        fila = "<tr idconstanciassss='" + value.codigoC.idConstancia + "'>" +
                "<th class='align-middle' style='text-align:center'><input type='button' class='btn btn-info tesis' title='Cargar' value='Cargar'></th>" +
                "<th class='align-middle'><input type='hidden'>" + value.codigoC.codigoTesis + "</th>" +
                "<th class='align-middle'><input type='hidden'>" + value.codigoC.alumno.nombre + " " + value.codigoC.alumno.aPaterno + " " + value.codigoC.alumno.aMaterno + "</th>" +
                "<th class='align-middle'><input type='hidden'>" + value.codigoC.docenteLinea.docenteId.nombre + " " + value.codigoC.docenteLinea.docenteId.aPaterno + " " + value.codigoC.docenteLinea.docenteId.aMaterno + "</th>" +
                "<th class='align-middle'><input type='hidden'>" + d+ "</th></tr>";

        $('#tablitaConstancia').append(fila);
    });

}
;



$(function () {
    $('#btncargar').click(function (e) {
        var dato = $('#BuscaTesis').serialize();
        $.ajax({
            url: "Vista",
            type: "post",
            data: dato,
            success: function (jsonResponse) {
                $('#tablitaConstancia').empty();
                mostrarTabladeConstancias3(jsonResponse);
                agregarEventosdeConstancias3();
            }

        });
    });

    $('#btnBuscar2').click(function (e) {
        var dato = $('#BuscaTesis').serialize();
        $.ajax({
            url: "Vista",
            type: "post",
            data: dato,
            success: function (jsonResponse) {
                $('#tablitaConstancia').empty();
                mostrarTabladeConstancias3(jsonResponse);
                agregarEventosdeConstancias3();
            }

        });
    });

});

function agregarEventosdeConstancias3() {
    var botones = document.getElementsByClassName("tesis");
    for (var i = 0; i < botones.length; i++) {
        botones[i].addEventListener("click", function () {
            var array_tr = this.parentElement.parentElement.children;
            var dato = $(this.parentElement.parentElement).attr("idconstanciassss");
            var orden = "DatosTesis";
            $.ajax({
                url: "Vista",
                type: "post",
                data: {
                    jurado: dato,
                    filtro: orden
                },
                success: function (jsonResponse) {

                    $.each(jsonResponse.DATA, function (index, value) {
                        $('#txtNroConstancia').val(value.nroConstancia);
                        $('#txtNroExpediente').val(value.nroExpendiente);
                        $('#txttitulo').val(value.tituloTesis);
                        $('#txtcodigoP').val(value.codigoTesis);
                        $('#txtautor').val(value.alumno.nombre+" "+value.alumno.aPaterno+" "+value.alumno.aMaterno);
                        $('#txtasesorP').val(value.docenteLinea.docenteId.nombre + " " + value.docenteLinea.docenteId.aPaterno + " " + value.docenteLinea.docenteId.aMaterno);
                        $('#txtLinea').val(value.docenteLinea.lineaId.nombre);
                        $('#txtfecha').val(value.fecha);
                    });
                    $('#txtfecha2').val(jsonResponse.DATA2);

                    $.each(jsonResponse.DATA1, function (index, value) {
                        $('#txtpresidenteGra' + index + '').val(value.cargo);
                        $('#txtpresidente' + index + '').val(value.idDocente.nombre+" "+value.idDocente.aPaterno+" "+value.idDocente.aMaterno);
                    });
                    alert("CARGADO CORRECTAMENTE");
                    $('#modalconstancias').modal('hide');
                }

            });

        });
    }
}
;
