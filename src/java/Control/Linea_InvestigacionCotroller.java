/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Beans.Escuela;
import Beans.LineaInvestigacion;
import Modelo.EscuelaModel;
import Modelo.LineaInvestigacionModel;
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
 * 
 * @author JhanxD
 */
public class Linea_InvestigacionCotroller extends HttpServlet {
    private HttpSession session;
    private ConexionModel mConn;
    private LineaInvestigacionModel mLineas;
    private EscuelaModel mEscuela;
    private Gson json;
    
    @Override
    public void init() throws ServletException {
        super.init(); 
        
        try{
            mConn = ConexionModel.getConexion();
            mEscuela=new EscuelaModel(mConn.getPool());
            mLineas = new LineaInvestigacionModel(mConn.getPool());
            json = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        }catch(Exception e){
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
            case "Buscar":
                ordenarTabla("", request, response);
                break;
            case "Agregar":
                ingresarLinea(request, response);
                break;
            case "Eliminar":
                eliminarLinea(request, response);
                break;
            case "Editar":
                editarLinea(request, response);
                break;
            default:
                    RequestDispatcher miDis=request.getRequestDispatcher("/LineasInvestigacion.jsp");
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

    
    private void ingresarLinea(HttpServletRequest request, HttpServletResponse response) {
       String datoNOMBRE= request.getParameter("filtro2");
        String datoABREVIATURA=request.getParameter("filtro3");
        int datoFACUL=Integer.parseInt(request.getParameter("filtro4"));
        LineaInvestigacion temfac = new LineaInvestigacion ();
        temfac.setNombre(datoNOMBRE);
        temfac.setArea(datoABREVIATURA);
        temfac.setEscuela(mEscuela.findEscuela(datoFACUL));
        try {
            mLineas.create(temfac);
            ordenarTabla("Registrado", request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void editarLinea(HttpServletRequest request, HttpServletResponse response) {
        int datoCODIV=Integer.parseInt(request.getParameter("filtro1"));
        String datoNOMBRE= request.getParameter("filtro2");
        String datoABREVIATURA=request.getParameter("filtro3");
        int datoFACUL=Integer.parseInt(request.getParameter("filtro4"));
        LineaInvestigacion temfac = new LineaInvestigacion ();
        temfac.setIdLinea(datoCODIV);
        temfac.setNombre(datoNOMBRE);
        temfac.setArea(datoABREVIATURA);
        temfac.setEscuela(mEscuela.findEscuela(datoFACUL));
        try {
            mLineas.edit(temfac);
            ordenarTabla("Modificado", request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void eliminarLinea(HttpServletRequest request, HttpServletResponse response) {
        int datoCODIV=Integer.parseInt(request.getParameter("filtro1"));
        try {
            mLineas.destroy(datoCODIV);
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
            List<LineaInvestigacion> respuestaServer = new ArrayList<>();
            List<Escuela> respuestaServer1 = new ArrayList<>();
            respuestaServer1=mEscuela.findEscuelaEntities();
            if (!request.getParameter("filtro").equals("Buscar")) {
                /*SOLO SI ESTA CORRECTO LISTARA*/
                System.out.println("mensaje trans: " + MENSAJETRANSACCION);
                if (MENSAJETRANSACCION.equals("Registrado")
                        | MENSAJETRANSACCION.equals("Modificado")
                        | MENSAJETRANSACCION.equals("Eliminado")) {
                    respuestaServer = mLineas.findLineaInvestigacionEntities();
                }
                mensajeRoot = "Transaccion";
            } else {
                String codigo = request.getParameter("txtNombre");
                System.out.println(codigo);
                if (codigo==null) {
                    respuestaServer = mLineas.findLineaInvestigacionEntities();
                } else if(codigo.equals("")){
                    respuestaServer = mLineas.findLineaInvestigacionEntities();
                }else{
                    
                    respuestaServer = mLineas.findLineas2(codigo);
                }
                mensajeRoot = "";
            }
            JSONROOT.put("MENSAJEROOT", mensajeRoot);
            JSONROOT.put("MENSAJEACCION", MENSAJETRANSACCION);
            if (respuestaServer != null) {
                JSONROOT.put("DATA", (List<LineaInvestigacion>) respuestaServer);
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
}
