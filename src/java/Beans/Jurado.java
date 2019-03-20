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
@Table(name = "jurado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jurado.findAll", query = "SELECT j FROM Jurado j")
    , @NamedQuery(name = "Jurado.findByRelacion", query = "SELECT j FROM Jurado j WHERE j.relacion = :relacion")
    , @NamedQuery(name = "Jurado.findByCargo", query = "SELECT j FROM Jurado j WHERE j.cargo = :cargo")
    , @NamedQuery(name = "Jurado.findByFechaEmitido", query = "SELECT j FROM Jurado j WHERE j.fechaEmitido = :fechaEmitido")})
public class Jurado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "RELACION")
    @Expose private Integer relacion;
    @Basic(optional = false)
    @Column(name = "CARGO")
    @Expose private String cargo;
    @Basic(optional = false)
    @Column(name = "FECHA_EMITIDO")
    @Temporal(TemporalType.DATE)
    @Expose private Date fechaEmitido;
    @JoinColumn(name = "ID_TESIS", referencedColumnName = "ID_TESIS")
    @ManyToOne(optional = false)
    @Expose private Tesis idTesis;
    @JoinColumn(name = "ID_DOCENTE", referencedColumnName = "ID_DOCENTE")
    @ManyToOne(optional = false)
    @Expose private Docente idDocente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idJurado")
    private List<Observacion> observacionList;

    public Jurado() {
    }

    public Jurado(Integer relacion) {
        this.relacion = relacion;
    }

    public Jurado(Integer relacion, String cargo, Date fechaEmitido) {
        this.relacion = relacion;
        this.cargo = cargo;
        this.fechaEmitido = fechaEmitido;
    }

    public Integer getRelacion() {
        return relacion;
    }

    public void setRelacion(Integer relacion) {
        this.relacion = relacion;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Date getFechaEmitido() {
        return fechaEmitido;
    }

    public void setFechaEmitido(Date fechaEmitido) {
        this.fechaEmitido = fechaEmitido;
    }

    public Tesis getIdTesis() {
        return idTesis;
    }

    public void setIdTesis(Tesis idTesis) {
        this.idTesis = idTesis;
    }

    public Docente getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Docente idDocente) {
        this.idDocente = idDocente;
    }

    @XmlTransient
    public List<Observacion> getObservacionList() {
        return observacionList;
    }

    public void setObservacionList(List<Observacion> observacionList) {
        this.observacionList = observacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (relacion != null ? relacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jurado)) {
            return false;
        }
        Jurado other = (Jurado) object;
        if ((this.relacion == null && other.relacion != null) || (this.relacion != null && !this.relacion.equals(other.relacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Jurado[ relacion=" + relacion + " ]";
    }
    
}
