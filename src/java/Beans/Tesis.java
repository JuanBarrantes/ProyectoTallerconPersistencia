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
@Table(name = "tesis")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tesis.findAll", query = "SELECT t FROM Tesis t")
    , @NamedQuery(name = "Tesis.findByIdTesis", query = "SELECT t FROM Tesis t WHERE t.idTesis = :idTesis")
    , @NamedQuery(name = "Tesis.findByFormatoA", query = "SELECT t FROM Tesis t WHERE t.formatoA = :formatoA")})
public class Tesis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TESIS")
    @Expose private Integer idTesis;
    @Column(name = "FORMATO_A")
    @Temporal(TemporalType.DATE)
    @Expose private Date formatoA;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTesis")
    private List<Jurado> juradoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTesis")
    private List<Observacion> observacionList;
    @JoinColumn(name = "CODIGO_C", referencedColumnName = "ID_CONSTANCIA")
    @ManyToOne(optional = false)
    @Expose private Constancia codigoC;

    public Tesis() {
    }

    public Tesis(Integer idTesis) {
        this.idTesis = idTesis;
    }

    public Integer getIdTesis() {
        return idTesis;
    }

    public void setIdTesis(Integer idTesis) {
        this.idTesis = idTesis;
    }

    public Date getFormatoA() {
        return formatoA;
    }

    public void setFormatoA(Date formatoA) {
        this.formatoA = formatoA;
    }

    @XmlTransient
    public List<Jurado> getJuradoList() {
        return juradoList;
    }

    public void setJuradoList(List<Jurado> juradoList) {
        this.juradoList = juradoList;
    }

    @XmlTransient
    public List<Observacion> getObservacionList() {
        return observacionList;
    }

    public void setObservacionList(List<Observacion> observacionList) {
        this.observacionList = observacionList;
    }

    public Constancia getCodigoC() {
        return codigoC;
    }

    public void setCodigoC(Constancia codigoC) {
        this.codigoC = codigoC;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTesis != null ? idTesis.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tesis)) {
            return false;
        }
        Tesis other = (Tesis) object;
        if ((this.idTesis == null && other.idTesis != null) || (this.idTesis != null && !this.idTesis.equals(other.idTesis))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Tesis[ idTesis=" + idTesis + " ]";
    }
    
}
