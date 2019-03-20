/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Beans.Facultad;
import Beans.Jurado;
import Beans.Observacion;
import Beans.Tesis;
import Extras.ReportPDF;
import Modelo.ConstanciaModel;
import Modelo.DocenteModel;
import Modelo.EscuelaModel;
import Modelo.JuradoModel;
import Modelo.LineaDocenteModel;
import Modelo.LineaInvestigacionModel;
import Modelo.ObservacionModel;
import Modelo.TesisModel;
import Modelo.conn.ConexionModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class TesisCotroller extends HttpServlet {
    private HttpSession session;
    @Resource(name= "jdbc/Conector")
    private DataSource Pool;
    private JuradoModel mJurado;
    private LineaInvestigacionModel mLinea;
    private ConexionModel mConn;
    private ObservacionModel mObservacion;
    private TesisModel mTesis;
    private ConstanciaModel mConstancias;
    private Gson json;
    
    @Override
    public void init() throws ServletException {
        super.init();        
        
        try {
            mConn = ConexionModel.getConexion();
            mJurado=new JuradoModel(mConn.getPool());
            mLinea=new LineaInvestigacionModel(mConn.getPool());
            mObservacion=new ObservacionModel(mConn.getPool());
            mTesis=new TesisModel(mConn.getPool());
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
            case "Guardar":
                guardarTesis (request, response);
                break;
            case "Reporte":
                generarReporte (request, response);
                break;
            case "ReporteFormatoB":
                generarReporte2 (request, response);
                break;
            case "ReporteFinal":
                generarReporte3 (request, response);
                break;
            case "BuscarEstadodeObservaciones":
                estadoObservaciones (request, response);
                break;
            case "Cargar":
                ordenarTabla ("", request, response);
                break;
            case "BuscaJurado":
                ordenarJurado (request, response);
                break;
            case "AgregarObservacion":
                agreagarObservacion(request, response);
                break;
            case "LevantarObservacion":
                levantarObservacion(request,response);
                break;
            case "Aprobar":
                aprobaAnteproyecto(request,response);
                break;
            default:
                    RequestDispatcher miDis=request.getRequestDispatcher("/NombramientoJurado.jsp");
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

    private void guardarTesis(HttpServletRequest request, HttpServletResponse response) {
        int codigo = Integer.parseInt(request.getParameter("codigo"));
        SimpleDateFormat formateFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha1=null;
        try {
            fecha1=formateFecha.parse(request.getParameter("fechaa"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        Tesis cons = new Tesis ();
        cons.setCodigoC(mConstancias.findConstancia(codigo));
        cons.setFormatoA(fecha1);
        
        String cadenaJson;
        try{
            HashMap<String, Object> JSONROOT = new HashMap<>();
            String respuesta= mTesis.create(cons);
            JSONROOT.put("RESP", respuesta);
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            response.getWriter().write(cadenaJson);
        }catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void generarReporte(HttpServletRequest request, HttpServletResponse response) throws SQLException {
         try {
            int codigo_constancia = Integer.parseInt(request.getParameter("codigo"));
            JasperReport jr = JasperCompileManager.compileReport(getServletContext().getRealPath("/Reportes/Constancias.jrxml"));
            HashMap parametros = new HashMap();
            parametros.put("CODIGO",codigo_constancia);
            JasperPrint jp = JasperFillManager.fillReport(jr, parametros, Pool.getConnection());
            byte[] byts=ReportPDF.exportarPDF(jp);
            response.setContentLength(byts.length);
            response.setHeader("Content-disposition","incline; filename=ConstanciadeNoDuplicidad"+codigo_constancia+".pdf");
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
            JasperReport jr = JasperCompileManager.compileReport(getServletContext().getRealPath("/Reportes/formatoB.jrxml"));
            HashMap parametros = new HashMap();
            parametros.put("codaconst",codigo_constancia);
            parametros.put("presidente",jur1);
            parametros.put("miembro1",jur2);
            parametros.put("miembro2",jur3);

            JasperPrint jp = JasperFillManager.fillReport(jr, parametros, Pool.getConnection());
            byte[] byts=ReportPDF.exportarPDF(jp);
            response.setContentLength(byts.length);
            response.setHeader("Content-disposition","incline; filename=FormatoB"+codigo_constancia+".pdf");
            response.setContentType("application/pdf");
            try (ServletOutputStream ouput = response.getOutputStream()){
                ouput.write(byts, 0, byts.length);
                ouput.flush();
            }
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(TesisCotroller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void generarReporte3(HttpServletRequest request, HttpServletResponse response) throws SQLException {
            try {
            int codigo_constancia = Integer.parseInt(request.getParameter("codigo"));

            JasperReport jr = JasperCompileManager.compileReport(getServletContext().getRealPath("/Reportes/DecretodeAprobacion.jrxml"));
            HashMap parametros = new HashMap();
            parametros.put("codiTESIS",codigo_constancia);


            JasperPrint jp = JasperFillManager.fillReport(jr, parametros,Pool.getConnection());
            byte[] byts=ReportPDF.exportarPDF(jp);
            response.setContentLength(byts.length);
            response.setHeader("Content-disposition","incline; filename=DechetodeAPROBACION"+codigo_constancia+".pdf");
            response.setContentType("application/pdf");
            try (ServletOutputStream ouput = response.getOutputStream()){
                ouput.write(byts, 0, byts.length);
                ouput.flush();
            }
            
        } catch (JRException | IOException ex) {
            Logger.getLogger(TesisCotroller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void  estadoObservaciones(HttpServletRequest request, HttpServletResponse response) {
        int juardo=Integer.parseInt(request.getParameter("jurado"));
        int tesis=Integer.parseInt(request.getParameter("tesis"));
        
        Observacion obs = new Observacion();
        obs.setIdJurado(mJurado.findJurado(juardo));
        obs.setIdTesis(mTesis.findTesis(tesis));
        String cadenaJson;
        try {
            HashMap<String, Object> JSONROOT = new HashMap<>();
            String respuestaServer = "";
            respuestaServer = mJurado.cotejar(obs);
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

    private void ordenarTabla(String MENSAJETRANSACCION, HttpServletRequest request, HttpServletResponse response) {
        String cadenaJson;

        try {
            String mensajeRoot;
            HashMap<String, Object> JSONROOT = new HashMap<>();
            List<Tesis> respuestaServer = new ArrayList<>();
            if (!request.getParameter("filtro").equals("Cargar")) {
                /*SOLO SI ESTA CORRECTO LISTARA*/
                System.out.println("mensaje trans: " + MENSAJETRANSACCION);
                if (MENSAJETRANSACCION.equals("Registrado")
                        | MENSAJETRANSACCION.equals("Modificado")
                        | MENSAJETRANSACCION.equals("Eliminado")) {
                    respuestaServer = mTesis.findTesisEntities();
                }
                mensajeRoot = "Transaccion";
            } else {
                String codigo = request.getParameter("txtNombre");
                System.out.println(codigo);
                if (codigo==null) {
                    respuestaServer =mTesis.findTesisEntities();
                } else if(codigo.equals("")){
                    respuestaServer = mTesis.findTesisEntities();
                }else{
                    
                    respuestaServer = mTesis.findTesis2(codigo);
                }
                mensajeRoot = "";
            }
            JSONROOT.put("MENSAJEROOT", mensajeRoot);
            JSONROOT.put("MENSAJEACCION", MENSAJETRANSACCION);
            if (respuestaServer != null) {
                JSONROOT.put("DATA", (List<Tesis>) respuestaServer);
            }

            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void ordenarJurado(HttpServletRequest request, HttpServletResponse response) {
            int asesor = Integer.parseInt(request.getParameter("jurado"));
        System.out.println(asesor);
        String cadenaJson;
        try {
            HashMap<String, Object> JSONROOT = new HashMap<>();
            Object[] respuestaServer = null;
            respuestaServer = mJurado.ordenar(mConstancias.findConstancia(asesor));
            if (respuestaServer != null) {
                JSONROOT.put("RESPFECHAS", (String)respuestaServer[0]);
                JSONROOT.put("DATA", (List<Jurado>) respuestaServer[1]);
            }
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);

        } catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void agreagarObservacion(HttpServletRequest request, HttpServletResponse response) {
        int juardo=Integer.parseInt(request.getParameter("relacionjurado"));
        int tesis=Integer.parseInt(request.getParameter("idtesis"));
        String observacion = request.getParameter("observ");
        SimpleDateFormat formateFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha1=null, fecha2=null;
        try {
            fecha1=formateFecha.parse(request.getParameter("fecha2"));
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha1);
        cal.add(Calendar.DAY_OF_YEAR, 15);
        fecha2=cal.getTime();
        
        System.out.println(fecha1+"  +  "+fecha2);
        
        Observacion obs = new Observacion();
        obs.setIdJurado(mJurado.findJurado(juardo));
        obs.setIdTesis(mTesis.findTesis(tesis));
        obs.setLevantada("NO");
        obs.setObservacion(observacion);
        obs.setFechaInicio(fecha1);
        obs.setFechaFin(fecha2);
        try{
            mObservacion.create(obs);
        }catch (Exception e){
            e.printStackTrace();
        }
        

    }

    private void levantarObservacion(HttpServletRequest request, HttpServletResponse response) {
        int juardo=Integer.parseInt(request.getParameter("relacionjuradol"));
        int tesis=Integer.parseInt(request.getParameter("idtesisl"));
        System.out.println("jurado = "+juardo+" tesis = "+tesis);
        
        String cadenaJson;
        try {
            HashMap<String, Object> JSONROOT = new HashMap<>();
            String respuesta= mJurado.levantarObservacion(juardo ,mTesis.findTesis(tesis));
            JSONROOT.put("RESPFECHAS", (String)respuesta);
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        }catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void aprobaAnteproyecto(HttpServletRequest request, HttpServletResponse response) {
        int tesis=Integer.parseInt(request.getParameter("codigo"));
        System.out.println("tesis n° "+tesis);
        Observacion obs = new Observacion();
        obs.setIdTesis(mTesis.findTesis(tesis));
        String cadenaJson;
        try {
            HashMap<String, Object> JSONROOT = new HashMap<>();
            String respuesta= mTesis.aprobarAnteproyecto(obs);
            JSONROOT.put("RESPFECHAS", (String)respuesta);
            cadenaJson = json.toJson(JSONROOT);
            response.setContentType("application/json");
            System.out.println("cadena json: " + cadenaJson);
            response.getWriter().write(cadenaJson);
        }catch (IOException e) {
            Logger.getLogger(FacultadCotroller.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
