/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Beans.Docente;
import Beans.Jurado;
import Beans.Observacion;
import Extras.Email;
import Extras.ReportPDF;
import Modelo.ConstanciaModel;
import Modelo.DocenteModel;
import Modelo.JuradoModel;
import Modelo.LineaInvestigacionModel;
import Modelo.ObservacionModel;
import Modelo.TesisModel;
import Modelo.conn.ConexionModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import static java.lang.System.out;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author JhanxD
 */
public class JuradoCotroller extends HttpServlet {
    @Resource(name= "jdbc/Conector")
    private HttpSession session;
    private DataSource Pool;
    private JuradoModel mJurado;
    private LineaInvestigacionModel mLinea;
    private DocenteModel mDocente;
    private ConexionModel mConn;
    private TesisModel mTesis;
    private ObservacionModel mObservacion;
    private ConstanciaModel mConstancias;
    private Gson json;
    
    @Override
    public void init() throws ServletException {
        super.init();        
        
        try {
            mConn = ConexionModel.getConexion();
            mJurado=new JuradoModel(mConn.getPool());
            mLinea=new LineaInvestigacionModel(mConn.getPool());
            mDocente=new DocenteModel(mConn.getPool());
            mTesis=new TesisModel(mConn.getPool());
            mObservacion=new ObservacionModel(mConn.getPool());
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
        try {
            String dato = request.getParameter("filtro") == null ? "CargarPagina" : request.getParameter("filtro");
            System.out.println("filtro: " + dato);
            // Redirigir el flujo de ejecucion al metodo adecuado
            switch (dato) {
                case "AsignarJurado":
                    asignarJurado(request, response);
                    break;
                case "Guardar":
                    guardarJurado(request, response);
                    break;
                case "Reporte":
                    generarReporte (request, response);
                    break;
                case "ReporteFormatoA":
                    generarReporte2 (request, response);
                    break;
                case "SinObservaciones":
                    sinObservaciones (request, response);
                    break;
                case "EnviarMensaje":
                    enviarCorreos ( request, response);
                    break;
                default:
                    RequestDispatcher miDis = request.getRequestDispatcher("/NombramientoJurado.jsp");
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

    private void asignarJurado(HttpServletRequest request, HttpServletResponse response) {
        int asesor = Integer.parseInt(request.getParameter("docente"));
        int constancia = Integer.parseInt(request.getParameter("constancia"));
        String cadenaJson;
        try {
            HashMap<String, Object> JSONROOT = new HashMap<>();
            Object[] respuestaServer = null;
            respuestaServer = mJurado.asignar(asesor);
            int docente, linea;
            linea=Integer.parseInt((String)respuestaServer[0]);
            docente=Integer.parseInt((String)respuestaServer[1]);
            Object[] respuestaServerr = null;
            respuestaServerr=mJurado.buscar(mLinea.findLineaInvestigacion(linea),mDocente.findDocente(docente), mConstancias.findConstancia(constancia));
            
            if (respuestaServerr != null) {
                JSONROOT.put("DATA", (List<Docente>) respuestaServerr[0]);
                JSONROOT.put("CONSTANCIA", respuestaServerr[1]);
            }
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void guardarJurado(HttpServletRequest request, HttpServletResponse response) {
        int id_tesis = Integer.parseInt(request.getParameter("idcodigoc"));
        int id_presidente = Integer.parseInt(request.getParameter("cargo0"));
        int id_secretario = Integer.parseInt(request.getParameter("cargo1"));
        int id_vocal = Integer.parseInt(request.getParameter("cargo2"));
        String presidente = request.getParameter("PRESIDENTE");
        String secretario = request.getParameter("SECRETARIO");
        String vocal = request.getParameter("VOCAL");
        SimpleDateFormat formateFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date dFecha = null;
        try {
            dFecha = formateFecha.parse(request.getParameter("fechaformat"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        Jurado jur1 = new Jurado ();
        jur1.setIdTesis(mTesis.findTesis(id_tesis));
        jur1.setIdDocente(mDocente.findDocente(id_presidente));
        jur1.setCargo(presidente);
        jur1.setFechaEmitido(dFecha);
        Jurado jur2 = new Jurado ();
        jur2.setIdTesis(mTesis.findTesis(id_tesis));
        jur2.setIdDocente(mDocente.findDocente(id_secretario));
        jur2.setCargo(secretario);
        jur2.setFechaEmitido(dFecha);
        Jurado jur3 = new Jurado ();
        jur3.setIdTesis(mTesis.findTesis(id_tesis));
        jur3.setIdDocente(mDocente.findDocente(id_vocal));
        jur3.setCargo(vocal);
        jur3.setFechaEmitido(dFecha);
        
        try{
            mJurado.create(jur1);
            mJurado.create(jur2);
            mJurado.create(jur3);
            
        }catch (Exception e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void generarReporte(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        try {
            int codigo_constancia = Integer.parseInt(request.getParameter("codigo"));
            String jur1 = request.getParameter("jur1");
            String jur2 = request.getParameter("jur2");
            String jur3 = request.getParameter("jur3");
            String fech = request.getParameter("fech");
            JasperReport jr = JasperCompileManager.compileReport(getServletContext().getRealPath("/Reportes/ResolucionJurado.jrxml"));
            HashMap parametros = new HashMap();
            parametros.put("codacta",codigo_constancia);
            parametros.put("jurado1",jur1);
            parametros.put("jurado2",jur2);
            parametros.put("jurado3",jur3);
            parametros.put("fechaemitido",fech);
            JasperPrint jp = JasperFillManager.fillReport(jr, parametros,Pool.getConnection());
            byte[] byts=ReportPDF.exportarPDF(jp);
            response.setContentLength(byts.length);
            response.setHeader("Content-disposition","incline; filename=ResolucionJurado"+codigo_constancia+".pdf");
            response.setContentType("application/pdf");
            try (ServletOutputStream ouput = response.getOutputStream()){
                ouput.write(byts, 0, byts.length);
                ouput.flush();
            }
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(TesisCotroller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void generarReporte2(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        try {
            int codigo_constancia = Integer.parseInt(request.getParameter("codigo"));
            String jur1 = request.getParameter("jur1");
            String jur2 = request.getParameter("jur2");
            String jur3 = request.getParameter("jur3");
            String fech = request.getParameter("fech");
            JasperReport jr = JasperCompileManager.compileReport(getServletContext().getRealPath("/Reportes/formatoA.jrxml"));
            HashMap parametros = new HashMap();
            parametros.put("codaconst",codigo_constancia);
            parametros.put("presidente",jur1);
            parametros.put("1miembro",jur2);
            parametros.put("2miembro",jur3);
            parametros.put("fecha",fech);
            parametros.put("fecha2"," ");
            parametros.put("autor2"," ");
            JasperPrint jp = JasperFillManager.fillReport(jr, parametros,Pool.getConnection());
            byte[] byts=ReportPDF.exportarPDF(jp);
            response.setContentLength(byts.length);
            response.setHeader("Content-disposition","incline; filename=FormatoA"+codigo_constancia+".pdf");
            response.setContentType("application/pdf");
            try (ServletOutputStream ouput = response.getOutputStream()){
                ouput.write(byts, 0, byts.length);
                ouput.flush();
            }
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(TesisCotroller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviarCorreos(HttpServletRequest request, HttpServletResponse response) {
        Email email = new Email();
            
            String de = "sistemaexpediente@gmail.com";
            String clave = "A1B2C3D4E5holamundo";
            String para = request.getParameter("para");
            String para1="sunder-28@hotmail.com";
            String mensaje = "Buenos dias, usted ha sido seleccionado como parte del jurado de ......";
            String asunto = "RESULTADO JURADO";
            
            /* 
                
                String[] direcciones = {"correo numero 1","correo numero 2","correo numero 3","correo ..."}
                boolean resultado = email.enviarCorreo(de, clave, direcciones, mensaje, asunto);
            
            */
            
            boolean resultado = email.enviarCorreo(de, clave, para1, mensaje, asunto);
            
            if(resultado){
                out.print("CORREO ELECTRONICO CORRECTAMENTE ENVIADO....."+"\n\n");
            }else{
                out.print("CORREO ELECTRONICO NO ENVIADO....."+"\n\n"); 
            }
            

    }

    private void sinObservaciones(HttpServletRequest request, HttpServletResponse response) {
        int jurado = Integer.valueOf(request.getParameter("idjurado"));
        int tesis =Integer.valueOf(request.getParameter("idtesis"));
        
        String observacion = "SIN OBSERVACIONES";
        SimpleDateFormat formateFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha1=null;
        try {
            fecha1=formateFecha.parse(request.getParameter("fecha2"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        System.out.println(fecha1);
        
        Observacion obs = new Observacion();
        obs.setIdJurado(mJurado.findJurado(jurado));
        obs.setIdTesis(mTesis.findTesis(tesis));
        obs.setLevantada("SI");
        obs.setObservacion(observacion);
        obs.setFechaInicio(fecha1);
        obs.setFechaFin(fecha1);
        try{
            mObservacion.create(obs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
