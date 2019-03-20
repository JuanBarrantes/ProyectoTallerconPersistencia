/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Beans.Constancia;
import Beans.Jurado;
import Beans.Tesis;
import Extras.VistaModel;
import Modelo.conn.ConexionModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
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
public class VistaCotroller extends HttpServlet {
    private HttpSession session;
    private ConexionModel mConn;
    private VistaModel mVista;
    private Gson json;
    
    @Override
    public void init() throws ServletException {
        super.init(); 

        try{
            mConn = new ConexionModel();
            mVista=new VistaModel(mConn.getPool());
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
            case "Cargar":
                ordenarTabla ("", request, response);
                break;
            case "DatosTesis":
                datosTesis (request, response);
                break;
            default:
                    RequestDispatcher miDis=request.getRequestDispatcher("/EstadosAnteproyectos.jsp");
                    miDis.forward(request, response);
                    break;
    }
    }catch(Exception e){
        e.printStackTrace();
    }
    }
    
    
    private void ordenarTabla(String MENSAJETRANSACCION, HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;
        try {
            String mensajeRoot;
            HashMap<String, Object> JSONROOT = new HashMap<>();
            List<Tesis> respuestaServer = null;
            if (!request.getParameter("filtro").equals("Cargar")) {
                /*SOLO SI ESTA CORRECTO LISTARA*/
                System.out.println("mensaje trans: " + MENSAJETRANSACCION);
                if (MENSAJETRANSACCION.equals("Registrado")
                        | MENSAJETRANSACCION.equals("Modificado")
                        | MENSAJETRANSACCION.equals("Eliminado")) {
                    respuestaServer = listar(request, response);
                }
                mensajeRoot = "Transaccion";
            } else {
                respuestaServer = listar(request, response);
                mensajeRoot = "";
            }
            JSONROOT.put("MENSAJEROOT", mensajeRoot);
            JSONROOT.put("MENSAJEACCION", MENSAJETRANSACCION);
            if (respuestaServer != null) {
               
                JSONROOT.put("DATA", respuestaServer);
        
            }
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            System.out.println("mensaje rout "+ mensajeRoot);
            System.out.println("mensaje transaccion "+ MENSAJETRANSACCION);
            response.getWriter().write(cadenaJson);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    private List listar(HttpServletRequest request, HttpServletResponse response) throws SQLException, ClassNotFoundException {
        return mVista.ordenar(request.getParameter("txtNombre") == null ? "" : request.getParameter("txtNombre"));
    }
    
    private void datosTesis(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        int tesis = Integer.parseInt(request.getParameter("jurado"));
        System.out.println(tesis);
        String cadenaJson;
        try {
            HashMap<String, Object> JSONROOT = new HashMap<>();
            List<Constancia> respuestaServer1 = null;
            respuestaServer1 = mVista.datos0(tesis);
            List<Jurado> respuestaServer2 = null;
            respuestaServer2 = mVista.datos1(tesis);
            Date respuestaServer3 = mVista.datos2(tesis);
                JSONROOT.put("DATA1", respuestaServer2) ;
                JSONROOT.put("DATA", respuestaServer1);
                JSONROOT.put("DATA2", respuestaServer3);
            
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);

        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
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

}
