$(document).ready(function () {
    $('#listadejurado').hide();
});

function mostrarTabladeConstancias2(jsonResponse) {
        var fila;

        $.each(jsonResponse.DATA, function (index, value) {
            fila = "<tr idconstanciasss='" + value.codigoC.idConstancia + "'>" +
                    "<th class='align-middle' style='text-align:center'><input type='button' class='btn btn-info tesis' title='Cargar' value='Cargar'></th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.codigoC.codigoTesis + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.codigoC.alumno.nombre+" "+ value.codigoC.alumno.aPaterno+" "+ value.codigoC.alumno.aMaterno+ "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.codigoC.docenteLinea.docenteId.nombre+" "+value.codigoC.docenteLinea.docenteId.aPaterno +" "+ value.codigoC.docenteLinea.docenteId.aMaterno+"</th></tr>";

            $('#tablitaConstancia').append(fila);
        });
    
}
;

function obtenerfecha() {
    $.ajax({
        url: "Constancias",
        type: "post",
        data: {
            filtro: "ObtenerFecha"
        },
        success: function (jsonResponse) {
            var f1 = "  " + jsonResponse.FECHA1 + "";
            var f2 = jsonResponse.FECHA2;
            $('#fecha').html(f1);
            $('#fecha2').val(f2);
        }
    });
}
function  verificar (){
    var dato = $('#consta').val();
            var orden = "BuscaJurado";
            $.ajax({
                url: "Tesis",
                type: "post",
                data: {
                    jurado: dato,
                    filtro: orden
                },
                success: function (jsonResponse) {
                    
                    var col1, btn = jsonResponse.RESPFECHAS;
                    obtenerfecha();
                    if (btn === "ok") {
                        $('#respuestafechas').empty();
                        $('#listadejurado').show();
                        $.each(jsonResponse.DATA, function (index, value) {
                            col1 = "<h6>" + value.idDocente.nombre+" "+value.idDocente.aPaterno +" "+ value.idDocente.aMaterno+"</h6>";
                            $('#g' + index + '').val(value.relacion);
                            $('#b' + index + '').val(value.relacion);
                            $('#E' + index + '').val(value.relacion);
                            $('#miembro' + index + '').html(col1);
                            $('#idtesis').val(value.idTesis.idTesis);
                            $('#idtesisl').val(value.idTesis.idTesis);
                            $('#mjurado' + index + '').val(value.idDocente.nombre+" "+value.idDocente.aPaterno +" "+ value.idDocente.aMaterno);
                            estadodeObservaciones(value.relacion, value.idTesis.idTesis, index);

                            $('#modalconstancias').modal('hide');
                        });
                    } else {
                        $('#respuestafechas').html("<p class='text-danger'>" + btn + "</p>");
                    }



                }

            });
}

function sinobservaciones(a){
    var codigo=$('#codites').val();
    $.ajax({
        url:"Jurados",
        type:"post",
        data:{
            filtro: "SinObservaciones",
            idjurado: a,
            idtesis : codigo,
            fecha2 : $('#fecha2').val()
        },
        success: function (){
            verificar();
        }
    });
}

function estadodeObservaciones(jurado, tesis, index) {
    var jur = jurado;
    var tes = tesis;
    var ind=index;
    $.ajax({
        url: "Tesis",
        type: "post",
        data: {
            filtro: "BuscarEstadodeObservaciones",
            jurado: jur,
            tesis: tes
        },
        success: function (jsonResponse) {
            
            //alert(ind+" ok");
            var rspta = jsonResponse.RESPUESTA;
            if (rspta === "notiene") {
                $('#estado'+ind+'').html("");
                $('#g'+ind).show();
                $('#b'+ind).show();
                $('#E'+ind).hide();
            } else if (rspta === "tiene") {
                $('#estado'+ind+'').html("BAD");
                $('#g'+ind).hide();
                $('#b'+ind).hide();
                $('#E'+ind).show();
            } else if(rspta === "tuvo"){
                $('#estado'+ind+'').html("OK");
                $('#g'+ind).hide();
                $('#b'+ind).hide();
                $('#E'+ind).hide();
            }
            
            
        }
    });
}

$(function () {
    $('#btncargar').click(function (e) {
        var dato = $('#BuscaTesis').serialize();
        $.ajax({
            url: "Tesis",
            type: "post",
            data: dato,
            success: function (jsonResponse) {
                $('#tablitaConstancia').empty();
                mostrarTabladeConstancias2(jsonResponse);
                agregarEventosdeConstancias2();
            }

        });
    });
 
    $('#g0').click(function (e) {
        var a = $('#g0').val();
        sinobservaciones(a);
    });
    $('#g1').click(function (e) {
        var a = $('#g1').val();
       sinobservaciones(a);
    });
    $('#g2').click(function (e) {
        var a = $('#g2').val();
        sinobservaciones(a);
    });
    $('#b0').click(function (e) {
        var a = $('#b0').val();
        $('#relacionjurado').val(a);
        $('#observ').empty();
    });
    $('#b1').click(function (e) {
        var a = $('#b1').val();
        $('#relacionjurado').val(a);
        $('#observ').empty();
    });
    $('#b2').click(function (e) {
        var a = $('#b2').val();
        $('#relacionjurado').val(a);
        $('#observ').empty();
    });
    $('#E0').click(function (e) {
        var a = $('#E0').val();
        $('#relacionjuradol').val(a);
    });
    $('#E1').click(function (e) {
        var a = $('#E1').val();
        $('#relacionjuradol').val(a);
    });
    $('#E2').click(function (e) {
        var a = $('#E2').val();
        $('#relacionjuradol').val(a);
    });

    $('#btnBuscar2').click(function (e) {
        var dato = $('#BuscaTesis').serialize();
        $.ajax({
            url: "Tesis",
            type: "post",
            data: dato,
            success: function (jsonResponse) {
                $('#tablitaConstancia').empty();
                mostrarTabladeConstancias2(jsonResponse);
                agregarEventosdeConstancias2();
            }

        });
    });

    $('#guardarObservacion').click(function (e) {
        alert("llego aca");
        var dato = $('#formObservaciones').serialize();
        $.ajax({
            url: "Tesis",
            type: "post",
            data: dato,
            success: function (jsonResponse) {
                $('#ModalObsevaciones').modal('hide');
                verificar();
            }

        });
    });

    $('#Aprobar').click(function (e) {
        var dato = $('#idtesis').val();
        $.ajax({
            url: "Tesis",
            type: "post",
            data: {
                codigo: dato,
                filtro: "Aprobar"
            },
            success: function (jsonResponse) {

                var resp = jsonResponse.RESPFECHAS;
                if (resp === "Aprovado") {
                    $('#dformatoB').html("<a target='_blank' href='Tesis?filtro=ReporteFormatoB&codigo=" + $('#idtesis').val() + "&jur1=" + $('#mjurado0').val() + "&jur2=" + $('#mjurado1').val() + "&jur3=" + $('#mjurado2').val() + "' class='btn btn-dark'>Formato B</a>");
                    $('#dreportefinal').html("<a target='_blank' href='Tesis?filtro=ReporteFinal&codigo=" + $('#idtesis').val() + "' class='btn btn-dark'>Decreto de Aprobación</a>");
                } else {
                    alert(resp);
                }
            }

        });
    });
    $('#levantarObservacion').click(function (e) {
        var dato = $('#formlevantar').serialize();
        $.ajax({
            url: "Tesis",
            type: "post",
            data: dato,
            success: function (jsonResponse) {

                var resp = jsonResponse.RESPFECHAS;
                if (resp === "Levantado") {
                    $('#Estasseguro').modal('hide');
                    verificar();
                } else {
                    alert(resp);
                }
            }

        });
    });

});

function agregarEventosdeConstancias2() {
    var botones = document.getElementsByClassName("tesis");
    for (var i = 0; i < botones.length; i++) {
        botones[i].addEventListener("click", function () {
            var array_tr = this.parentElement.parentElement.children;
            var dato = $(this.parentElement.parentElement).attr("idconstanciasss");
            var orden = "BuscaJurado";
            $.ajax({
                url: "Tesis",
                type: "post",
                data: {
                    jurado: dato,
                    filtro: orden
                },
                success: function (jsonResponse) {
                    $('#consta').val(dato);
                    var col1, btn = jsonResponse.RESPFECHAS;
                    obtenerfecha();
                    if (btn === "ok") {
                        $('#respuestafechas').empty();
                        $('#listadejurado').show();
                        $.each(jsonResponse.DATA, function (index, value) {
                            col1 = "<h6>" + value.idDocente.nombre+" "+value.idDocente.aPaterno +" "+ value.idDocente.aMaterno+"</h6>";
                            $('#g' + index + '').val(value.relacion);
                            $('#b' + index + '').val(value.relacion);
                            $('#E' + index + '').val(value.relacion);
                            $('#miembro' + index + '').html(col1);
                            $('#idtesis').val(value.idTesis.idTesis);
                            $('#codites').val(value.idTesis.idTesis);
                            $('#idtesisl').val(value.idTesis.idTesis);
                            $('#mjurado' + index + '').val(value.idDocente.nombre+" "+value.idDocente.aPaterno +" "+ value.idDocente.aMaterno);
                            estadodeObservaciones(value.relacion, value.idTesis.idTesis, index);

                            $('#modalconstancias').modal('hide');
                        });
                    } else {
                        $('#respuestafechas').html("<p class='text-danger'>" + btn + "</p>");
                    }



                }

            });

        });
    }
}
;