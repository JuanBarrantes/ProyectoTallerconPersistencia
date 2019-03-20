<a class="navbar-brand" href="#">SISTES</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item active">
            <a class="nav-link" href="Index.jsp">Inicio <span class="sr-only">(current)</span></a>
        </li>
      <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Nueva Tesis
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                <a class="dropdown-item" href="Principal.jsp">Empezar Registro</a>
                <a class="dropdown-item" href="NombramientoJurado.jsp">Nombramiento del Jurado</a>
                <a class="dropdown-item" href="Observaciones.jsp">Observaciones</a>
            </div>
        </li>
      <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Mantenimiento
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                <a class="dropdown-item" href="Facultad.jsp">Facultades</a>
                <a class="dropdown-item" href="Escuela.jsp">Escuelas</a>
                <a class="dropdown-item" href="LineasInvestigacion.jsp">Lineas de Investigacion</a>
                <a class="dropdown-item" href="Docente.jsp">Docentes</a>
            </div>
        </li>
      
    </ul>
      <ul class="navbar-nav mr-auto  mr-sm-2">
    <li class="nav-item dropdown">
            <a class="nav-link nav-link" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                <%
                            out.print(namae);
                %>
            </a>
            <div class="dropdown-menu dropdown-menu-right">
                <div class="dropdown-header text-center">
                <%
                            out.print(area_session);
                %>
                </div>
                <a class="dropdown-item" href="#"><i class="fa fa-user"></i> Perfil</a>
                <a class="dropdown-item" href="#"><i class="fa fa-unlock-alt"></i> Cambiar Clave</a>
                <a class="dropdown-item" href="CerrarSesion"><i class="fa fa-sign-out"></i> Cerrar Sesión</a>
            </div>
        </li>
      </ul>
  </div>