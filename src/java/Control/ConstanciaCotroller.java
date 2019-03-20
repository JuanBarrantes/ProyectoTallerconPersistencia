/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Beans.Constancia;
import Beans.Docente;
import Beans.LineaDocente;
import Modelo.AlumnoModel;
import Modelo.ConstanciaModel;
import Modelo.DocenteModel;
import Modelo.EscuelaModel;
import Modelo.LineaDocenteModel;
import Modelo.LineaInvestigacionModel;
import Modelo.conn.ConexionModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class ConstanciaCotroller extends HttpServlet {
    private HttpSession session;
    private AlumnoModel mAlumno;
    private LineaInvestigacionModel mLinea;
    private LineaDocenteModel mLDocente;
    private DocenteModel mDocente;
    private ConexionModel mConn;
    private EscuelaModel mEscuela;
    private ConstanciaModel mConstancias;
    private Gson json;
    
    @Override
    public void init() throws ServletException {
        super.init();        
        
        try {
            mConn = ConexionModel.getConexion();
            mEscuela = new EscuelaModel(mConn.getPool());
            mAlumno=new AlumnoModel(mConn.getPool());
            mLDocente=new LineaDocenteModel(mConn.getPool());
            mLinea=new LineaInvestigacionModel(mConn.getPool());
            mDocente=new DocenteModel(mConn.getPool());
            mConstancias=new ConstanciaModel(mConn.getPool());
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
            case "ObtenerFecha":
                obtenerFecha(request, response);
                break;
            case "CargaDocentes":
                comboDocentes(request, response);
                break;
            case "CargaLineas":
                comboLineas(request, response);
                break;
            case "CotejarTitulo":
                cotejarTitulo (request, response);
                break;
            case "BuscaRelacion":
                buscaRelacion (request, response);
                break;
            case "Guardar":
                guardarConstancia (request, response);
                break;
            case "Cargar":
                ordenarTabla ("", request, response);
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

    private void obtenerFecha(HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;
        try {
        String[] meses= {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        Calendar c1 = Calendar.getInstance();
        String dia=Integer.toString(c1.get(Calendar.DATE));
        String mes=meses[c1.get(Calendar.MONTH)];
        String mes1=Integer.toString(c1.get(Calendar.MONTH)+1);
        String año =Integer.toString(c1.get(Calendar.YEAR));
        String Formato1=dia+" de "+mes+" de "+año;

        String dia2="", mes2="";
        if (dia.length()==1) {
           dia2="0"+dia;
        }else{
            dia2=dia;
        }
        if (mes1.length()==1) {
            mes2="0"+mes1;
        }else{
            mes2=mes;
        }
        String Formato2=año+"-"+mes2+"-"+dia2;


            HashMap<String, Object> JSONROOT = new HashMap<>();
            JSONROOT.put("FECHA1", Formato1);
            JSONROOT.put("FECHA2", Formato2);
            
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        } catch ( IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }

    private void comboDocentes(HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;
        try {
            
            HashMap<String, Object> JSONROOT = new HashMap<>();
            List<Docente> respuestaServer = null;
            int escul = Integer.parseInt(request.getParameter("codigo"));
                    respuestaServer = mDocente.findDocente3(mEscuela.findEscuela(escul));
                JSONROOT.put("DATA", (List<Docente>) respuestaServer);

            
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void comboLineas(HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;
        try {
            
            HashMap<String, Object> JSONROOT = new HashMap<>();
            List<LineaDocente> respuestaServer = null;
            int escul = Integer.parseInt(request.getParameter("codigo"));
                    respuestaServer = mLinea.findLineas3(mDocente.findDocente(escul));
                JSONROOT.put("DATA", (List<LineaDocente>) respuestaServer);

            
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void cotejarTitulo(HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;
        try {
            
            HashMap<String, Object> JSONROOT = new HashMap<>();
            String respuestaServer = null;
            String facul = request.getParameter("codigo");
                    respuestaServer = mConstancias.cotejar(facul);
            if (respuestaServer != null) {
                JSONROOT.put("RESPUESTA", respuestaServer);

            }
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void guardarConstancia(HttpServletRequest request, HttpServletResponse response) {
        String constancia = request.getParameter("txtNroConstancia");
        String expediente = request.getParameter("txtNroExpediente");
        String titulo = request.getParameter("txttitulo").toUpperCase();
        String codigo = request.getParameter("txtcodigoP");
        int dictamen = Integer.parseInt(request.getParameter("txtDICTAMEN"));
        int alumno = Integer.parseInt(request.getParameter("CODIGOALUMNO"));
        int relacion =Integer.parseInt(request.getParameter("txtrelacion"));
        SimpleDateFormat formateFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date dFecha=null;
        try {
            dFecha=formateFecha.parse(request.getParameter("fechaformat"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        Constancia cons = new Constancia ();
        cons.setNroConstancia(constancia);
        cons.setNroExpendiente(expediente);
        cons.setTituloTesis(titulo);
        cons.setDictamen(dictamen);
        cons.setCodigoTesis(codigo);
        cons.setAlumno(mAlumno.findAlumno(alumno));
        cons.setDocenteLinea(mLDocente.findLineaDocente(relacion));
        cons.setFecha(dFecha);        
        String cadenaJson;
        try{
            HashMap<String, Object> JSONROOT = new HashMap<>();
            String respuesta= mConstancias.create(cons);
            JSONROOT.put("RESP", respuesta);
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            response.getWriter().write(cadenaJson);
        }catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }
        

    }

    private void buscaRelacion(HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;
        try {
            
            HashMap<String, Object> JSONROOT = new HashMap<>();
            String cantidad = "";
            String relacion = "";
            int facul =Integer.parseInt(request.getParameter("codigo"));
            int facul2 =Integer.parseInt(request.getParameter("codigo1"));
            System.out.println("dsg"+facul+facul2);
                    cantidad= mLDocente.cotejarCant(mDocente.findDocente(facul), mLinea.findLineaInvestigacion(facul2));
                    relacion =mLDocente.cotejarRel(mDocente.findDocente(facul), mLinea.findLineaInvestigacion(facul2));

                JSONROOT.put("RESPUESTA", cantidad);
                JSONROOT.put("RESP2", relacion);
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
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
            List<Constancia> respuestaServer = new ArrayList<>();
            if (!request.getParameter("filtro").equals("Cargar")) {
                /*SOLO SI ESTA CORRECTO LISTARA*/
                System.out.println("mensaje trans: " + MENSAJETRANSACCION);
                if (MENSAJETRANSACCION.equals("Registrado")
                        | MENSAJETRANSACCION.equals("Modificado")
                        | MENSAJETRANSACCION.equals("Eliminado")) {
                    respuestaServer = mConstancias.findConstanciaEntities();
                }
                mensajeRoot = "Transaccion";
            } else {
                String codigo = request.getParameter("txtNombre");
                System.out.println(codigo);
                if (codigo==null) {
                    respuestaServer = mConstancias.findConstanciaEntities();
                } else if(codigo.equals("")){
                    respuestaServer = mConstancias.findConstanciaEntities();
                }else{
                    
                    respuestaServer = mConstancias.findConstancias2(codigo);
                }
                mensajeRoot = "";
            }
            JSONROOT.put("MENSAJEROOT", mensajeRoot);
            JSONROOT.put("MENSAJEACCION", MENSAJETRANSACCION);
            if (respuestaServer != null) {
                JSONROOT.put("DATA", (List<Constancia>) respuestaServer);
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
