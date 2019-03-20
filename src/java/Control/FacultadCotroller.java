/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Beans.Facultad;
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
public class FacultadCotroller extends HttpServlet {
    private HttpSession session;
    private FacultadModel mFacultad;
    private ConexionModel mConn;
    private Gson json;

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            mConn = ConexionModel.getConexion();
            mFacultad = new FacultadModel(mConn.getPool());
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
                    ingresarFacultad(request, response);
                    break;
                case "Eliminar":
                    eliminarFacultad(request, response);
                    break;
                case "Editar":
                    editarFacultad(request, response);
                    break;
                default:
                    RequestDispatcher miDis = request.getRequestDispatcher("/Facultad.jsp");
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

    private void ingresarFacultad(HttpServletRequest request, HttpServletResponse response) {
        String datoNOMBRE = request.getParameter("filtro2");
        String datoABREVIATURA = request.getParameter("filtro3");

        Facultad temfac = new Facultad();
        temfac.setNombre(datoNOMBRE);
        temfac.setAbreviatura(datoABREVIATURA);
        try {
            mFacultad.create(temfac);
            ordenarTabla("Registrado", request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void editarFacultad(HttpServletRequest request, HttpServletResponse response) {
        Integer datoCODIGO = Integer.parseInt(request.getParameter("filtro1"));
        String datoNOMBRE = request.getParameter("filtro2");
        String datoABREVIATURA = request.getParameter("filtro3");
        Facultad temfac = new Facultad();
        temfac.setIdFacultad(datoCODIGO);
        temfac.setNombre(datoNOMBRE);
        temfac.setAbreviatura(datoABREVIATURA);
        try {
            mFacultad.edit(temfac);
            ordenarTabla("Modificado", request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void eliminarFacultad(HttpServletRequest request, HttpServletResponse response) {
        int datoCODIGO = Integer.parseInt(request.getParameter("filtro1"));

        try {
            mFacultad.destroy(datoCODIGO);
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
            List<Facultad> respuestaServer = new ArrayList<>();
            if (!request.getParameter("filtro").equals("Buscar")) {
                /*SOLO SI ESTA CORRECTO LISTARA*/
                System.out.println("mensaje trans: " + MENSAJETRANSACCION);
                if (MENSAJETRANSACCION.equals("Registrado")
                        | MENSAJETRANSACCION.equals("Modificado")
                        | MENSAJETRANSACCION.equals("Eliminado")) {
                    respuestaServer = mFacultad.findFacultadEntities();
                }
                mensajeRoot = "Transaccion";
            } else {
                String codigo = request.getParameter("txtNombre");
                System.out.println(codigo);
                if (codigo==null) {
                    respuestaServer = mFacultad.findFacultadEntities();
                } else if(codigo.equals("")){
                    respuestaServer = mFacultad.findFacultadEntities();
                }else{
                    
                    respuestaServer = mFacultad.findFacultad2(codigo);
                }
                mensajeRoot = "";
            }
            JSONROOT.put("MENSAJEROOT", mensajeRoot);
            JSONROOT.put("MENSAJEACCION", MENSAJETRANSACCION);
            if (respuestaServer != null) {
                JSONROOT.put("DATA", (List<Facultad>) respuestaServer);
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
