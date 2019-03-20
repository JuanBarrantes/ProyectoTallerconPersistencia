/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author JhanxD
 */
@Entity
@Table(name = "linea_docente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LineaDocente.findAll", query = "SELECT l FROM LineaDocente l")
    , @NamedQuery(name = "LineaDocente.findByIdRelacion", query = "SELECT l FROM LineaDocente l WHERE l.idRelacion = :idRelacion")})
public class LineaDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_RELACION")
    @Expose private Integer idRelacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docenteLinea")
    private List<Constancia> constanciaList;
    @JoinColumn(name = "LINEA_ID", referencedColumnName = "ID_LINEA")
    @ManyToOne(optional = false)
    @Expose private LineaInvestigacion lineaId;
    @JoinColumn(name = "DOCENTE_ID", referencedColumnName = "ID_DOCENTE")
    @ManyToOne(optional = false)
    @Expose private Docente docenteId;

    public LineaDocente() {
    }

    public LineaDocente(Integer idRelacion) {
        this.idRelacion = idRelacion;
    }

    public Integer getIdRelacion() {
        return idRelacion;
    }

    public void setIdRelacion(Integer idRelacion) {
        this.idRelacion = idRelacion;
    }

    @XmlTransient
    public List<Constancia> getConstanciaList() {
        return constanciaList;
    }

    public void setConstanciaList(List<Constancia> constanciaList) {
        this.constanciaList = constanciaList;
    }

    public LineaInvestigacion getLineaId() {
        return lineaId;
    }

    public void setLineaId(LineaInvestigacion lineaId) {
        this.lineaId = lineaId;
    }

    public Docente getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(Docente docenteId) {
        this.docenteId = docenteId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRelacion != null ? idRelacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LineaDocente)) {
            return false;
        }
        LineaDocente other = (LineaDocente) object;
        if ((this.idRelacion == null && other.idRelacion != null) || (this.idRelacion != null && !this.idRelacion.equals(other.idRelacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.LineaDocente[ idRelacion=" + idRelacion + " ]";
    }
    
}
