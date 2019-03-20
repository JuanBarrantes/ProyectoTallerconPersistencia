$(function () {


    $("#FrmLogin").submit(function () {
        return false;
    });


    
    $('#btnIngresar').click(function (){
        validar();
        validarErrores();
      
    });
    
    $('#txtUsuario').focus();
});

function validar() {
    if ($('#txtUsuario').val() !== "") {
       if ($('#txtPass').val() !== "") {
           return true;
       }else {
           alert("Por favor Ingrese una Contraseña");
       }
    } else {
        alert("Por favor Ingrese un Usuario");
    }
}

function validarErrores() {
    //console.log("ENTRO AJAX");
    $.ajax({
        url: 'Session',
        dataType: 'json',
        type: 'POST',
        data: $('#FrmLogin').serialize(),
        success: function (jsonResponse) {
        
            //console.log(jsonResponse.AUTENTICACION);
            if (jsonResponse.AUTENTICACION === "CORRECTO") {
                if (jsonResponse.PERMISOS === "1") {
                    $(location).attr('href', 'Index.jsp');
                }else{
                    $(location).attr('href', 'Index2.jsp');
                }
                
            } else {
                alert(jsonResponse.AUTENTICACION);

            }
        }
    });

    return false;
}