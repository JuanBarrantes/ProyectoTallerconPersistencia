/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author JhanxD
 */
@Entity
@Table(name = "constancia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Constancia.findAll", query = "SELECT c FROM Constancia c")
    , @NamedQuery(name = "Constancia.findByIdConstancia", query = "SELECT c FROM Constancia c WHERE c.idConstancia = :idConstancia")
    , @NamedQuery(name = "Constancia.findByNroConstancia", query = "SELECT c FROM Constancia c WHERE c.nroConstancia = :nroConstancia")
    , @NamedQuery(name = "Constancia.findByNroExpendiente", query = "SELECT c FROM Constancia c WHERE c.nroExpendiente = :nroExpendiente")
    , @NamedQuery(name = "Constancia.findByCodigoTesis", query = "SELECT c FROM Constancia c WHERE c.codigoTesis = :codigoTesis")
    , @NamedQuery(name = "Constancia.findByTituloTesis", query = "SELECT c FROM Constancia c WHERE c.tituloTesis = :tituloTesis")
    , @NamedQuery(name = "Constancia.findByDictamen", query = "SELECT c FROM Constancia c WHERE c.dictamen = :dictamen")
    , @NamedQuery(name = "Constancia.findByFecha", query = "SELECT c FROM Constancia c WHERE c.fecha = :fecha")})
public class Constancia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_CONSTANCIA")
    @Expose private Integer idConstancia;
    @Basic(optional = false)
    @Column(name = "NRO_CONSTANCIA")
    @Expose private String nroConstancia;
    @Basic(optional = false)
    @Column(name = "NRO_EXPENDIENTE")
    @Expose private String nroExpendiente;
    @Basic(optional = false)
    @Column(name = "CODIGO_TESIS")
    @Expose private String codigoTesis;
    @Basic(optional = false)
    @Column(name = "TITULO_TESIS")
    @Expose private String tituloTesis;
    @Basic(optional = false)
    @Column(name = "DICTAMEN")
    @Expose private int dictamen;
    @Basic(optional = false)
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    @Expose private Date fecha;
    @JoinColumn(name = "ALUMNO", referencedColumnName = "ID_ALUMNO")
    @ManyToOne(optional = false)
    @Expose private Alumno alumno;
    @JoinColumn(name = "DOCENTE_LINEA", referencedColumnName = "ID_RELACION")
    @ManyToOne(optional = false)
    @Expose private LineaDocente docenteLinea;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codigoC")
    private List<Tesis> tesisList;

    public Constancia() {
    }

    public Constancia(Integer idConstancia) {
        this.idConstancia = idConstancia;
    }

    public Constancia(Integer idConstancia, String nroConstancia, String nroExpendiente, String codigoTesis, String tituloTesis, int dictamen, Date fecha) {
        this.idConstancia = idConstancia;
        this.nroConstancia = nroConstancia;
        this.nroExpendiente = nroExpendiente;
        this.codigoTesis = codigoTesis;
        this.tituloTesis = tituloTesis;
        this.dictamen = dictamen;
        this.fecha = fecha;
    }

    public Integer getIdConstancia() {
        return idConstancia;
    }

    public void setIdConstancia(Integer idConstancia) {
        this.idConstancia = idConstancia;
    }

    public String getNroConstancia() {
        return nroConstancia;
    }

    public void setNroConstancia(String nroConstancia) {
        this.nroConstancia = nroConstancia;
    }

    public String getNroExpendiente() {
        return nroExpendiente;
    }

    public void setNroExpendiente(String nroExpendiente) {
        this.nroExpendiente = nroExpendiente;
    }

    public String getCodigoTesis() {
        return codigoTesis;
    }

    public void setCodigoTesis(String codigoTesis) {
        this.codigoTesis = codigoTesis;
    }

    public String getTituloTesis() {
        return tituloTesis;
    }

    public void setTituloTesis(String tituloTesis) {
        this.tituloTesis = tituloTesis;
    }

    public int getDictamen() {
        return dictamen;
    }

    public void setDictamen(int dictamen) {
        this.dictamen = dictamen;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public LineaDocente getDocenteLinea() {
        return docenteLinea;
    }

    public void setDocenteLinea(LineaDocente docenteLinea) {
        this.docenteLinea = docenteLinea;
    }

    @XmlTransient
    public List<Tesis> getTesisList() {
        return tesisList;
    }

    public void setTesisList(List<Tesis> tesisList) {
        this.tesisList = tesisList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConstancia != null ? idConstancia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Constancia)) {
            return false;
        }
        Constancia other = (Constancia) object;
        if ((this.idConstancia == null && other.idConstancia != null) || (this.idConstancia != null && !this.idConstancia.equals(other.idConstancia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Constancia[ idConstancia=" + idConstancia + " ]";
    }
    
}
