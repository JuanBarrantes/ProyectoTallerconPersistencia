$(document).ready(function () {
    $('#btnGuardarr').hide();
    $('#numero2').hide();
    $('#btnContinuar').hide();
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

function mostrarTabladeConstancias(jsonResponse) {
        var fila, res;
        $.each(jsonResponse.DATA, function (index, value) {
            if (value.dictamen == "1") {
            res="APROBADO";
        }else{
            res="RECHAZADO";
        }
            fila = "<tr idconstanciass='" + value.idConstancia + "'>" +
                    "<th class='align-middle' style='text-align:center'><a target='_blank' href='Tesis?filtro=Reporte&codigo=" + value.idConstancia + "' title='Generar Reporte' class='btn btn-dark'>R</a><input type='button' class='btn btn-info opciones' title='Cargar Constancia' value='C'></th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.nroConstancia + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.nroExpendiente + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + res + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.codigoTesis + "</th>" +
                    "<th class='align-middle ocultar'><input type='hidden'>" + value.docenteLinea.idRelacion + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.alumno.nombre+" "+value.alumno.aPaterno +" "+value.alumno.aMaterno + "</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.docenteLinea.docenteId.nombre + " "+value.docenteLinea.docenteId.aPaterno +" "+value.docenteLinea.docenteId.aMaterno +"</th>" +
                    "<th class='align-middle'><input type='hidden'>" + value.fecha + "</th></tr>";

            $('#tablitaConstancia').append(fila);
        });
    
}
;

function cargaJurado(asesor, dato) {
    var asesor = asesor;
    var dato = dato;
    $.ajax({
        url: "Jurados",
        type: "post",
        data: {
            filtro: "AsignarJurado",
            docente: asesor,
            constancia: dato
        },
        success: function (jsonResponse) {
            

            $('#idcodigoc').val(jsonResponse.CONSTANCIA);
            var col1, col2, col3, col4, col5;
            $.each(jsonResponse.DATA, function (index, value) {
                col1 = "<h6>" + value.docenteId.nombre+" "+value.docenteId.aPaterno +" "+value.docenteId.aMaterno +"</h6>";
                col2 = "<h6>" + value.docenteId.categoria + "</h6>";
                col3 = "<h6>" + value.docenteId.gradoAcademico + "</h6>";
                col4 = "<h6>" + value.docenteId.fechaIngreso + "</h6>";
                col5 = "<h6>" + value.docenteId.email + "</h6>";
                $('#cargo' + index + '').val(value.docenteId.idDocente);
                $('#n' + index + '').html(col1);
                $('#d'+index+'').val(value.docenteId.nombre+" "+value.docenteId.aPaterno +" "+value.docenteId.aMaterno);
                $('#c' + index + '').html(col2);
                $('#g' + index + '').html(col3);
                $('#ga'+index+'').val(value.docenteId.gradoAcademico);
                $('#f' + index + '').html(col4);
                $('#correo'+index+'').html(col5);
                $('#co'+index+'').val(value.docenteId.email);
            });
        }
    });




}

$(function () {
    $('#btnContinuar').click(function (e) {
       $('#numero1').hide();
       $('#numero2').show();
    });
    
    $('#btnGuardarr').click(function (e) {
        var dato = $('#formjurado').serialize();
        $.ajax({
            url: "Jurados",
            type: "post",
            data: dato,
            success: function () {
                var codi=$('#idcodigoc').val();
                $('#dr').html("<a target='_blank' href='Jurados?filtro=Reporte&codigo="+codi+"&jur1="+$('#ga0').val()+" "+$('#d0').val()+"&jur2="+$('#ga1').val()+" "+$('#d1').val()+"&jur3="+$('#ga2').val()+" "+$('#d2').val()+"&fech="+$('#txtfecha').val()+"' class='btn btn-dark'>Descargar Resolución</a>");
                $('#dcorreos').html("<a target='_blank' href='Jurados?filtro=EnviarMensaje&correo1="+$('#co0').val()+"&correo2="+$('#co1').val()+"&correo3="+$('#co2').val()+"' class='btn btn-success'>Enviar Correos</a>");
                $('#dformatoA').html("<a target='_blank' href='Jurados?filtro=ReporteFormatoA&codigo="+$('#const').val()+"&jur1="+$('#ga0').val()+" "+$('#d0').val()+"&jur2="+$('#ga1').val()+" "+$('#d1').val()+"&jur3="+$('#ga2').val()+" "+$('#d2').val()+"&fech="+$('#txtfecha').val()+"' class='btn btn-dark'>Formato A</a>");
                $('#btnContinuar').show();
                $('#btnGuardarr').hide();
            }

        });
    });
    $('#btncargar').click(function (e) {
        var dato = $('#BuscaConstancia').serialize();
        $.ajax({
            url: "Constancias",
            type: "post",
            data: dato,
            success: function (jsonResponse) {
                $('#tablitaConstancia').empty();
                mostrarTabladeConstancias(jsonResponse);
                agregarEventosdeConstancias();
            }

        });
    });

    $('#btnBuscar2').click(function (e) {
        var dato = $('#BuscaConstancia').serialize();
        $.ajax({
            url: "Constancias",
            type: "post",
            data: dato,
            success: function (jsonResponse) {
                $('#tablitaConstancia').empty();
                mostrarTabladeConstancias(jsonResponse);
                agregarEventosdeConstancias();
            }

        });
    });

});

function agregarEventosdeConstancias() {
    var botones = document.getElementsByClassName("opciones");
    for (var i = 0; i < botones.length; i++) {
        botones[i].addEventListener("click", function () {
            var array_tr = this.parentElement.parentElement.children;
            var asesor = array_tr[5].innerText;
            var dato = $(this.parentElement.parentElement).attr("idconstanciass");
            var fecha = $('#fechaformat').val();
            $('#const').val($(this.parentElement.parentElement).attr("idconstanciass"));
            $('#n').html("<h6>" + array_tr[1].innerText + "</h6>");
            $('#a2').html("<h6>" + array_tr[6].innerText + "</h6>");
            $('#a').html("<h6>" + array_tr[7].innerText + "</h6>");
            var orden = "Guardar";
            $.ajax({
                url: "Tesis",
                type: "post",
                data: {
                    codigo: dato,
                    filtro: orden,
                    fechaa:fecha
                },
                success: function (jsonResponse) {
                    var respuesta = jsonResponse.RESP.toLowerCase();
                    alert(respuesta);
                    if (respuesta === "registrado") {
                        cargaJurado(asesor, dato);
                        $('#modalconstancias').modal("hide");
                        $('#btnGuardarr').show();
                    }
                    
                }

            });

        });
    }
}
;

