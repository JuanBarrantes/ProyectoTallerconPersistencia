$(document).ready(function () {


    var orden = "Iniciar";
    $.ajax({
        url: "Alumnos",
        type: "post",
        data: {
            filtro: orden
        },
        success: function (jsonResponse) {
            llenarcomboFacultad(jsonResponse);
        }
    });

    $('#btnGuardar').hide();
    $('#btnGuardar2').hide();
    $('#progres2').hide();
    $('#progres6').hide();
    $('#progres7').hide();
    $('#progres3').hide();
    $('#progres4').hide();
    $('#progres5').hide();
    $('#numero2').hide();


});

function llenarcomboFacultad(jsonResponse) {

    var fila1, fila;
    fila = "<option>" + "---Selecione una Facultad---" + "</option>";
    $('#comboF').append(fila);
    $.each(jsonResponse.DATA, function (index, value) {
        fila1 = "<option  value='" + value.idFacultad + "'>" + value.abreviatura + "</option>";
        $('#comboF').append(fila1);
    });

}
;

function llenarcomboEscuela(jsonResponse) {

    var fila1, fila;
    fila = "<option>" + "---Selecione una Escuela Profesional---" + "</option>";
    $('#comboE').append(fila);
    $.each(jsonResponse.DATA, function (index, value) {
        fila1 = "<option  value='" + value.idEscuela + "'>" + value.abreviatura + "</option>";
        $('#comboE').append(fila1);
    });

}
;

function mostrarTabla(jsonResponse) {

    var fila;
    var uni, est;
    $.each(jsonResponse.DATA, function (index, value) {
        if ((value.uniOrigen)!== "Universidad Nacional Pedro Ruiz Gallo") {
            uni = "OTRA";
        } else {
            uni = "UNPRG";
        }
        if (value.estadot == "1") {
            est = "FINALIZADO";
        } else {
            est = "PENDIENTE";
        }

        fila = "<tr idalumno='" + value.idAlumno + "'>" +
                "<th class='align-middle' style='text-align:center'><input type='button' class='btn btn-info opciones" + value.estadot + "' value='Cargar'></th>" +
                "<th class='align-middle'><input type='hidden'>" + value.nombre + "</th>" +
                "<th class='align-middle'><input type='hidden'>" + value.aPaterno + "</th>" +
                "<th class='align-middle'><input type='hidden'>" + value.aMaterno + "</th>" +
                "<th class='align-middle letras color" + value.estadot + "'><input type='hidden'>" + est + "</th>" +
                "<th class='align-middle'><input type='hidden'>" + value.dni + "</th>" +
                "<th class='align-middle'><input type='hidden'>" + value.email + "</th>" +
                "<th class='align-middle'><input type='hidden'>" + value.celular + "</th>" +
                "<th class='align-middle'><input type='hidden'>" + value.gradoAcademico + "</th>" +
                "<th class='align-middle'><input type='hidden'>" + value.uni + "</th>" +
                "<th class='align-middle ocultar'><input type='hidden'>" + value.escuela.idEscuela + "</th>" +
                "<th class='align-middle'><input type='hidden'>" + value.escuela.abreviatura + "</th></tr>";

        $('#tablitaAlumnos').append(fila);
    });

}
;

function validardatos() {
    var nombre, paterno, materno, telefono, dni, correo, expresionemail, expresiontexto, universidad, escuela, grado, facultad;
    nombre = document.getElementById("txtNombres").value;
    paterno = document.getElementById("txtapepaterno").value;
    materno = document.getElementById("txtapematerno").value;
    telefono = document.getElementById("txttelefono").value;
    dni = document.getElementById("txtdni").value;
    correo = document.getElementById("txtemail").value;
    universidad = document.getElementById("txtUniver").value;
    escuela = document.getElementById("txtEscuela").value;
    grado = document.getElementById("txtGrado").value;
    facultad = document.getElementById("txtFacultad").value;
    expresiontexto = /[A-Za-z ]/;
    expresionemail = /\w+@\w+\.+[a-z]/;

    if (nombre === "" || paterno === "" || materno === "" || telefono === "" || dni === "" || correo === "" || universidad === "" || escuela === "" || grado === "") {
        alert("Todos los campos son obligatorios");
        return false;
    } else if (nombre.length > 20) {
        alert("El campo Nombres solo permite 20 carácteres");
        return false;
    } else if (!expresiontexto.test(nombre)) {
        alert("Solo se permiten letras en el campo Nombres");
        return false;
    } else if (paterno.length > 20) {
        alert("El campo Apellido paterno solo permite 20 carácteres");
        return false;
    } else if (!expresiontexto.test(paterno)) {
        alert("Solo se permiten letras en el campo de Apellido Paterno");
        return false;
    } else if (materno.length > 20) {
        alert("El campo Apellido Materno solo permite 20 carácteres");
        return false;
    } else if (!expresiontexto.test(materno)) {
        alert("Solo se permiten letras en el campo de Apellido Materno");
        return false;
    } else if (telefono.length > 9) {
        alert("El campo Telefono solo permite 9 carácteres");
        return false;
    } else if (isNaN(telefono)) {
        alert("Solo se permiten números en el campo Teléfono");
        return false;
    } else if (dni.length > 8) {
        alert("El campo DNI solo permite 8 carácteres");
        return false;
    } else if (isNaN(dni)) {
        alert("Solo se permiten números en el campo DNI");
        return false;
    } else if (correo.length > 20) {
        alert("El campo Correo solo permite 20 carácteres");
        return false;
    } else if (!expresionemail.test(correo)) {
        alert("EL Correo Ingresado no es válido");
        return false;
    }

    return true;

}
;

function llenar1(dato) {
    $('#txtUniver').val(dato);
}
function llenar2(dato) {
    $('#txtGrado').val(dato);
}
function llenar3(dato) {
    $('#txtEscuela').val(dato);
}
function llenar4(dato) {
    $('#txtFacultad').val(dato);
    $.ajax({
        url: "Alumnos",
        type: "post",
        data: {
            filtro: "CargaEscuelas",
            codigo: $('#txtFacultad').val()
        },
        success: function (jsonResponse) {
            $('#comboE').empty();
            llenarcomboEscuela(jsonResponse);
        }
    });


}


$(function () {

    $('#btnValidar').click(function (e) {


        if (validardatos()) {
            $('#txtEstadot').val(0);
            var datos = $('#alumnodatos').serialize();
            $.ajax({
                url: "Alumnos",
                type: "post",
                data: datos,
                success: function (jsonResponse) {
                    var codigoalumno = jsonResponse.INFO;
                    var respta = jsonResponse.RESP;
                    var respta1 = jsonResponse.RESP.toLowerCase();
                    $('#CODIGOALUMNO').val(codigoalumno);
                    if (respta1 === "registrado") {
                        alert(respta);
                        $('#respuestadatos').html("<p class='text-success'>✔ Campos Validados</p>");
                        $('#btnGuardar').show();
                        $('#progres1').hide();
                        $('#progres2').show();
                    } else {
                        alert(respta);
                        $('#respuestadatos').html("<p class='text-danger'>✘ Campos No Validados</p>");
                    }
                }
            });

        } else {
            $('#respuestadatos').html("<p class='text-danger'>✘ Campos No Validados</p>");
        }
    });

    $('#btncargar').click(function (e) {
        var dato = $('#BuscaAlumno').serialize();
        $.ajax({
            url: "Alumnos",
            type: "post",
            data: dato,
            success: function (jsonResponse) {
                $('#tablitaAlumnos').empty();
                mostrarTabla(jsonResponse);
                agregarEventos();
            }

        });
    });

    $('#btnBuscar2').click(function (e) {
        var dato = $('#BuscaAlumno').serialize();
        $.ajax({
            url: "Alumnos",
            type: "post",
            data: dato,
            success: function (jsonResponse) {
                $('#tablitaAlumnos').empty();
                mostrarTabla(jsonResponse);
                agregarEventos();
            }

        });
    });

    $('#btnGuardar').click(function (e) {
        $('#numero1').hide();
        $('#numero2').show();

    });


});

function agregarEventos() {
    var botones = document.getElementsByClassName("opciones0");
    for (var i = 0; i < botones.length; i++) {
        botones[i].addEventListener("click", function () {
            var array_tr = this.parentElement.parentElement.children;
            var data = {"escuelacodig": array_tr[10].innerText, "nombre": array_tr[0].innerText};
            $('#CODIGOALUMNO').val($(this.parentElement.parentElement).attr("idalumno"));
            $('#txtEscuela').val(data.escuelacodig);
            $('#btnGuardar2').show();
            $('#modalalumnos').modal("hide");
            alert("CARGADO CORRECTAMENTE");

        });
    }
    var botones = document.getElementsByClassName("opciones1");
    for (var i = 0; i < botones.length; i++) {
        botones[i].addEventListener("click", function () {
            alert("Este Alumno ya completo su proceso de tesis");
        });
    }
}
;