/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Extras;

import java.io.ByteArrayOutputStream;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

/**
 *
 * @author JhanxD
 */
public class ReportPDF {
    
    public static byte[] exportarPDF(JasperPrint jasperPrint) throws JRException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        @SuppressWarnings("rawtypes")
        Exporter exporter;
        exporter = new JRPdfExporter();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        SimplePdfReportConfiguration reportConfiguration = new SimplePdfReportConfiguration();
        SimplePdfExporterConfiguration exportConfiguration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(exportConfiguration);
        exporter.setConfiguration(reportConfiguration);
        exporter.exportReport();
        return out.toByteArray();
    }
    
}
