/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control.session;

import Beans.session.Usuario;
import Extras.StringEncrypt;
import Modelo.conn.ConexionModel;
import Modelo.session.UsuarioModel;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author JhanxD
 */
public class SessionCotroller extends HttpServlet {

    private final String key = "92AE31A79FEEB2A3"; //llave-parametro 1 
    private final String iv = "0123456789ABCDEF"; // vector de inicialización parametro 2
    private ConexionModel mConn;
    private UsuarioModel usuariomodel;
    
    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        mConn=new ConexionModel();
        usuariomodel = new UsuarioModel(mConn.getPool());
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
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            // si no hay sesiones iniciadas
            try {
                String login = request.getParameter("txtUsuario") == null ? "" : request.getParameter("txtUsuario");
                String pass = request.getParameter("txtPass") == null ? "" : request.getParameter("txtPass");
                Gson json = new Gson();
                String respuesta;
                String permisos = "";
                Usuario usuario = usuariomodel.getUserValidation(login);
                if (usuario != null) {
                    if (usuario.getEstado() == 0) {
                        respuesta = "El Usuario ingresado no está habilitado";
                    } else if (usuario.getPass().equals(pass)) {
                        //MANDAMOS AL INDEX
                        respuesta = "CORRECTO";
                        String encryp = ""; //VOLVEMOS A ENCRIPTAR
                        try {
                            encryp = StringEncrypt.encrypt(key, iv, usuario.getPass());
                        } catch (Exception ex) {
                            Logger.getLogger(SessionCotroller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        usuario.setPass(encryp);
                        session.setAttribute("user", usuario);
                        String area_session;
                        if (usuario.getIdusuario() == 1) {
                            area_session = "Administrador";
                        } else {
                            area_session = "Usuario";
                        }

                        System.out.println("->" + area_session);
                        session.setAttribute("area_session", area_session);
                        session.setAttribute("namae", usuario.getUsuario());
                        permisos = String.valueOf(usuario.getIdusuario());
                        //System.out.println("permisos: " + permisos);
                        //session.setAttribute("permisos", permisos);
                    } else {
                        respuesta = "Contraseña incorrecta";
                    }
                } else {
                    respuesta = "El Usuario ingresado no existe";
                }
                HashMap<String, Object> JSONROOT = new HashMap<>();
                JSONROOT.put("AUTENTICACION", respuesta);
                JSONROOT.put("PERMISOS", permisos);
                String cadenajson = json.toJson(JSONROOT);
                response.setContentType("application/json");
                response.getWriter().write(cadenajson);
            } catch (IOException ex) {
                Logger.getLogger(SessionCotroller.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //System.out.println("entro a enviar al index porque habia sesion :");
            response.sendRedirect("Index.jsp");
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
        processRequest(request, response);
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
        processRequest(request, response);
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
