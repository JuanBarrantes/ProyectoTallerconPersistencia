/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author JhanxD
 */
@Entity
@Table(name = "observacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Observacion.findAll", query = "SELECT o FROM Observacion o")
    , @NamedQuery(name = "Observacion.findByIdObservacion", query = "SELECT o FROM Observacion o WHERE o.idObservacion = :idObservacion")
    , @NamedQuery(name = "Observacion.findByObservacion", query = "SELECT o FROM Observacion o WHERE o.observacion = :observacion")
    , @NamedQuery(name = "Observacion.findByLevantada", query = "SELECT o FROM Observacion o WHERE o.levantada = :levantada")
    , @NamedQuery(name = "Observacion.findByFechaInicio", query = "SELECT o FROM Observacion o WHERE o.fechaInicio = :fechaInicio")
    , @NamedQuery(name = "Observacion.findByFechaFin", query = "SELECT o FROM Observacion o WHERE o.fechaFin = :fechaFin")})
public class Observacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_OBSERVACION")
    private Integer idObservacion;
    @Basic(optional = false)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Column(name = "LEVANTADA")
    private String levantada;
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @JoinColumn(name = "ID_TESIS", referencedColumnName = "ID_TESIS")
    @ManyToOne(optional = false)
    private Tesis idTesis;
    @JoinColumn(name = "ID_JURADO", referencedColumnName = "RELACION")
    @ManyToOne(optional = false)
    private Jurado idJurado;

    public Observacion() {
    }

    public Observacion(Integer idObservacion) {
        this.idObservacion = idObservacion;
    }

    public Observacion(Integer idObservacion, String observacion) {
        this.idObservacion = idObservacion;
        this.observacion = observacion;
    }

    public Integer getIdObservacion() {
        return idObservacion;
    }

    public void setIdObservacion(Integer idObservacion) {
        this.idObservacion = idObservacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getLevantada() {
        return levantada;
    }

    public void setLevantada(String levantada) {
        this.levantada = levantada;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Tesis getIdTesis() {
        return idTesis;
    }

    public void setIdTesis(Tesis idTesis) {
        this.idTesis = idTesis;
    }

    public Jurado getIdJurado() {
        return idJurado;
    }

    public void setIdJurado(Jurado idJurado) {
        this.idJurado = idJurado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idObservacion != null ? idObservacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Observacion)) {
            return false;
        }
        Observacion other = (Observacion) object;
        if ((this.idObservacion == null && other.idObservacion != null) || (this.idObservacion != null && !this.idObservacion.equals(other.idObservacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Observacion[ idObservacion=" + idObservacion + " ]";
    }
    
}
