/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Beans.Docente;
import Beans.Escuela;
import Beans.LineaDocente;
import Beans.LineaInvestigacion;
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
public class DocenteCotroller extends HttpServlet {
    private HttpSession session;
    private DocenteModel mDocente;
    private ConexionModel mConn;
    private LineaInvestigacionModel mLineas;
    private LineaDocenteModel mLDocente;
    private EscuelaModel mEscuela;
    private Gson json;
    
    @Override
    public void init() throws ServletException {
        super.init();        
        
        try {
            mConn = ConexionModel.getConexion();
            mEscuela = new EscuelaModel(mConn.getPool());
            mLineas = new LineaInvestigacionModel(mConn.getPool());
            mLDocente= new LineaDocenteModel(mConn.getPool());
            mDocente = new DocenteModel(mConn.getPool());
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
        try {
            String dato = request.getParameter("filtro") == null ? "CargarPagina" : request.getParameter("filtro");
            System.out.println("filtro: " + dato);
            // Redirigir el flujo de ejecucion al metodo adecuado
            switch (dato) {
                case "Buscar":
                    ordenarTabla("", request, response);
                    break;
                case "Agregar":
                    ingresarDocente(request, response);
                    break;
                case "Eliminar":
                    eliminarDocente(request, response);
                    break;
                case "Editar":
                    editarDocente(request, response);
                    break;
                case "OrdenarLineas":
                    OrdenarLineas("",request,response);
                    break;
                case "AgregarRelacion":
                    ingresarRelacion(request, response);
                    break;
                case "AnularRelacion":
                    AnularRelacion(request, response);
                    break;
                default:
                    RequestDispatcher miDis = request.getRequestDispatcher("/Docente.jsp");
                    miDis.forward(request, response);
                    break;
            }
        } catch (Exception e) {
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

    private void ingresarDocente(HttpServletRequest request, HttpServletResponse response) {
        String dNonmbre = request.getParameter("nombren");
        String dPaterno = request.getParameter("APaternon");
        String dMaterno = request.getParameter("AMaternon");
        String dGrado = request.getParameter("GAcademicon");
        String dCatgoria = request.getParameter("categorian");
        String dMail = request.getParameter("mailn");
        int dEscuela = Integer.parseInt(request.getParameter("rsta"));
        SimpleDateFormat formateFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date dFecha = null;
        try {
            dFecha = formateFecha.parse(request.getParameter("FIngreson"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        
        Docente temdoc = new Docente();
        temdoc.setNombre(dNonmbre);
        temdoc.setAPaterno(dPaterno);
        temdoc.setAMaterno(dMaterno);
        temdoc.setGradoAcademico(dGrado);
        temdoc.setCategoria(dCatgoria);
        temdoc.setEmail(dMail);
        temdoc.setEscuela(mEscuela.findEscuela(dEscuela));
        temdoc.setFechaIngreso(dFecha);
        if (dGrado.equals("Doctor")) {
            temdoc.setGa(1);
        }
        if (dGrado.equals("Magister")) {
            temdoc.setGa(2);
        }
        if (dGrado.equals("Ingeniero")) {
            temdoc.setGa(3);
        }
        if (dCatgoria.equals("PRINCIPAL")) {
            temdoc.setC(1);
        }
        if (dCatgoria.equals("ASOCIADO")) {
            temdoc.setC(2);
        }
        if (dCatgoria.equals("AUXILIAR")) {
            temdoc.setC(3);
        }
        
        try {
            mDocente.create(temdoc);
            ordenarTabla("Registrado", request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    private void editarDocente(HttpServletRequest request, HttpServletResponse response) {
        int dDocente = Integer.parseInt(request.getParameter("codigoe"));
        String dNonmbre = request.getParameter("nombree");
        String dPaterno = request.getParameter("APaternoe");
        String dMaterno = request.getParameter("AMaternoe");
        String dGrado = request.getParameter("GAcademicoe");
        String dCatgoria = request.getParameter("categoriae");
        String dMail = request.getParameter("maile");
        int dEscuela = Integer.parseInt(request.getParameter("rstae"));
        SimpleDateFormat formateFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date dFecha=null;
        try {
            dFecha=formateFecha.parse(request.getParameter("FIngresoe"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        
        Docente temdoc = new Docente();
        temdoc.setIdDocente(dDocente);
        temdoc.setNombre(dNonmbre);
        temdoc.setAPaterno(dPaterno);
        temdoc.setAMaterno(dMaterno);
        temdoc.setGradoAcademico(dGrado);
        temdoc.setCategoria(dCatgoria);
        temdoc.setEmail(dMail);
        temdoc.setEscuela(mEscuela.findEscuela(dEscuela));
        temdoc.setFechaIngreso(dFecha);
        if (dGrado.equals("Doctor")) {
            temdoc.setGa(1);
        }
        if (dGrado.equals("Magister")) {
            temdoc.setGa(2);
        }
        if (dGrado.equals("Ingeniero")) {
            temdoc.setGa(3);
        }
        if (dCatgoria.equals("PRINCIPAL")) {
            temdoc.setC(1);
        }
        if (dCatgoria.equals("ASOCIADO")) {
            temdoc.setC(2);
        }
        if (dCatgoria.equals("AUXILIAR")) {
            temdoc.setC(3);
        }
        try {
            mDocente.edit(temdoc);
            ordenarTabla("Modificado", request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void eliminarDocente(HttpServletRequest request, HttpServletResponse response) {
        int dDocente = Integer.parseInt(request.getParameter("codigod"));

        try {
            mDocente.destroy(dDocente);
            ordenarTabla("Eliminado", request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void ordenarTabla(String MENSAJETRANSACCION, HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;
        
        try {
            String mensajeRoot;
            HashMap<String, Object> JSONROOT = new HashMap<>();
            List<Docente> respuestaServer = new ArrayList<>();
            List<Escuela> respuestaServer1 = new ArrayList<>();
            respuestaServer1 = mEscuela.findEscuelaEntities();
            if (!request.getParameter("filtro").equals("Buscar")) {
                /*SOLO SI ESTA CORRECTO LISTARA*/
                System.out.println("mensaje trans: " + MENSAJETRANSACCION);
                if (MENSAJETRANSACCION.equals("Registrado")
                        | MENSAJETRANSACCION.equals("Modificado")
                        | MENSAJETRANSACCION.equals("Eliminado")) {
                    respuestaServer = mDocente.findDocenteEntities();
                }
                mensajeRoot = "Transaccion";
            } else {
                String codigo = request.getParameter("txtNombre");
                System.out.println(codigo);
                if (codigo == null) {
                    respuestaServer = mDocente.findDocenteEntities();
                } else if (codigo.equals("")) {
                    respuestaServer = mDocente.findDocenteEntities();
                } else {
                    
                    respuestaServer = mDocente.findDocente2(codigo);
                }
                mensajeRoot = "";
            }
            JSONROOT.put("MENSAJEROOT", mensajeRoot);
            JSONROOT.put("MENSAJEACCION", MENSAJETRANSACCION);
            if (respuestaServer != null) {
                JSONROOT.put("DATA", (List<Docente>) respuestaServer);
                JSONROOT.put("DATA2", (List<Escuela>) respuestaServer1);
            }
            
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void OrdenarLineas(String MENSAJETRANSACCION, HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;
        
        try {
            String mensajeRoot;
            HashMap<String, Object> JSONROOT = new HashMap<>();
            Object[] respuesta = null;
            List<LineaDocente> respuestaServer = new ArrayList<>();
            List<LineaInvestigacion> respuestaServer1 = new ArrayList<>();
                if (!request.getParameter("filtro").equals("OrdenarLineas")) {
                /*SOLO SI ESTA CORRECTO LISTARA*/
                System.out.println("mensaje trans: " + MENSAJETRANSACCION);
                if (MENSAJETRANSACCION.equals("Registrado")
                        | MENSAJETRANSACCION.equals("Modificado")
                        | MENSAJETRANSACCION.equals("Eliminado")) {
                    respuesta = listar2(request, response);
                }
                mensajeRoot = "Transaccion";
            } else {
                respuesta = listar2(request, response);
                mensajeRoot = "";
            }
                respuestaServer=(List<LineaDocente>)respuesta[0];
                respuestaServer1=(List<LineaInvestigacion>)respuesta[1];
            JSONROOT.put("MENSAJEROOT", mensajeRoot);
            JSONROOT.put("MENSAJEACCION", MENSAJETRANSACCION);
            if (respuestaServer != null) {
                JSONROOT.put("DATA", (List<LineaDocente>) respuestaServer);
                JSONROOT.put("DATA2", (List<LineaInvestigacion>) respuestaServer1);
            }
            
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public Object[] listar2(HttpServletRequest request, HttpServletResponse response) {
        Object[] res = new Object[2];
        String codigo=request.getParameter("codigol1");
        System.out.println("docente "+codigo);
        String escue=request.getParameter("escuelall");
        System.out.println("escula "+escue);
        if (codigo!=null) {
            int escuela = Integer.parseInt(escue);
            int codiv = Integer.parseInt(codigo);
            res[0]=mLDocente.findLineaDocente2(mDocente.findDocente(codiv));
            res[1]=mLineas.findLineaInvestigacionEntities3(mEscuela.findEscuela(escuela));
            return res;
        }else{
            int escuela=Integer.parseInt(request.getParameter("rstaE"));
            int codiv=Integer.parseInt(request.getParameter("rstaD"));
            res[0]=mLDocente.findLineaDocente2(mDocente.findDocente(codiv));
            res[1]=mLineas.findLineaInvestigacionEntities3(mEscuela.findEscuela(escuela));
            return res;
        }
    }

    private void ingresarRelacion(HttpServletRequest request, HttpServletResponse response) {
        int dDocente = Integer.parseInt(request.getParameter("rstaD"));
        int dLinea = Integer.parseInt(request.getParameter("rstaL"));
        
        LineaDocente tem = new LineaDocente();
        tem.setDocenteId(mDocente.findDocente(dDocente));
        tem.setLineaId(mLineas.findLineaInvestigacion(dLinea));
        try {
            mLDocente.create(tem);
            OrdenarLineas("Registrado", request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void AnularRelacion(HttpServletRequest request, HttpServletResponse response) {
        int dato = Integer.parseInt(request.getParameter("rstaR"));
        try {
            mLDocente.destroy(dato);
            OrdenarLineas("Eliminado", request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
