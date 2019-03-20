/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Beans.Alumno;
import Beans.Escuela;
import Beans.Facultad;
import Modelo.AlumnoModel;
import Modelo.EscuelaModel;
import Modelo.FacultadModel;
import Modelo.conn.ConexionModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author JhanxD
 */
public class AlumnoCotroller extends HttpServlet {
    private HttpSession session;
    private FacultadModel mFacultad;
    private AlumnoModel mAlumno;
    private ConexionModel mConn;
    private EscuelaModel mEscuela;
    private Gson json;
    
    @Override
    public void init() throws ServletException {
        super.init();        
        
        try {
            mConn = ConexionModel.getConexion();
            mEscuela = new EscuelaModel(mConn.getPool());
            mFacultad = new FacultadModel(mConn.getPool());
            mAlumno=new AlumnoModel(mConn.getPool());
            json = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
        try{
          String dato = request.getParameter("filtro") == null ? "CargarPagina" : request.getParameter("filtro");
            System.out.println("filtro: " + dato);
        // Redirigir el flujo de ejecucion al metodo adecuado
        switch (dato) {
            case "Iniciar":
                comboFacultades(request, response);
                break;
            case "CargaEscuelas":
                comboEscuela (request, response);
                break;
            case "Guardar":
                guardarAlumno (request, response);
                break;
            case "Cargar":
                ordenarTabla("", request, response);
                break;
            default:
                    RequestDispatcher miDis=request.getRequestDispatcher("/Principal.jsp");
                    miDis.forward(request, response);
                    break;
    }
    }catch(Exception e){
        e.printStackTrace();
    }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login");
        } else {
            processRequest(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login");
        } else {
            processRequest(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void comboFacultades(HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;
        try {            
            HashMap<String, Object> JSONROOT = new HashMap<>();
                JSONROOT.put("DATA", (List<Facultad>) mFacultad.findFacultadEntities());  
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void comboEscuela(HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;
        try {
            HashMap<String, Object> JSONROOT = new HashMap<>();
            int facul = Integer.parseInt(request.getParameter("codigo"));
                JSONROOT.put("DATA", (List<Escuela>) mEscuela.findEscuelaEntities2(mFacultad.findFacultad(facul)));
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }
    }

        private void guardarAlumno(HttpServletRequest request, HttpServletResponse response) {
        String nombre = request.getParameter("txtNombres");
        String paterno = request.getParameter("txtapepaterno");
        String materno = request.getParameter("txtapematerno");
        int telefono = Integer.parseInt(request.getParameter("txttelefono"));
        int dni = Integer.parseInt(request.getParameter("txtdni"));
        String correo = request.getParameter("txtemail");
        String univer = request.getParameter("txtUniver");
        String grado = request.getParameter("txtGrado");
        int escuel = Integer.parseInt(request.getParameter("txtEscuela"));
        int estad = Integer.parseInt(request.getParameter("txtEstadot"));
        
       // Alumno alum = new Alumno (nombre, paterno, materno, dni, correo, telefono, grado, univer, escuel, estad);
        Alumno dev = new Alumno ();
        dev.setAMaterno(materno);
        dev.setAPaterno(paterno);
        dev.setNombre(nombre);
        dev.setCelular(telefono);
        dev.setDni(dni);
        dev.setEmail(correo);
        dev.setUniOrigen(univer);
        dev.setGradoAcademico(grado);
        dev.setEscuela(mEscuela.findEscuela(escuel));
        dev.setEstadot(estad);
        String cadenaJson;
        try{
            HashMap<String, Object> JSONROOT = new HashMap<>();
            String resultado=mAlumno.create(dev);
            List<Alumno> alumno = mAlumno.findAlumnoEntities2(nombre, paterno, materno);
            int codigo = alumno.get(0).getIdAlumno();
            System.out.println("codigo del alumno "+codigo);
                JSONROOT.put("DATA", alumno);
                JSONROOT.put("INFO", codigo);
                JSONROOT.put("RESP", resultado);

            cadenaJson = json.toJson(JSONROOT);
            System.out.println("cadena json: " + cadenaJson);
            response.setContentType("application/json");
            response.getWriter().write(cadenaJson);
        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }

    private void ordenarTabla(String MENSAJETRANSACCION, HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;

        try {
            String mensajeRoot;
            HashMap<String, Object> JSONROOT = new HashMap<>();
            List<Alumno> respuestaServer = new ArrayList<>();
            if (!request.getParameter("filtro").equals("Cargar")) {
                /*SOLO SI ESTA CORRECTO LISTARA*/
                System.out.println("mensaje trans: " + MENSAJETRANSACCION);
                if (MENSAJETRANSACCION.equals("Registrado")
                        | MENSAJETRANSACCION.equals("Modificado")
                        | MENSAJETRANSACCION.equals("Eliminado")) {
                    respuestaServer = mAlumno.findAlumnoEntities();
                }
                mensajeRoot = "Transaccion";
            } else {
                String codigo = request.getParameter("txtNombre");
                System.out.println(codigo);
                if (codigo==null) {
                    respuestaServer = mAlumno.findAlumnoEntities();
                } else if(codigo.equals("")){
                    respuestaServer = mAlumno.findAlumnoEntities();
                }else{
                    
                    respuestaServer = mAlumno.findAlumno2(codigo);
                }
                mensajeRoot = "";
            }
            JSONROOT.put("MENSAJEROOT", mensajeRoot);
            JSONROOT.put("MENSAJEACCION", MENSAJETRANSACCION);
            if (respuestaServer != null) {
                JSONROOT.put("DATA", (List<Alumno>) respuestaServer);
            }

            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }

    }
    

}
