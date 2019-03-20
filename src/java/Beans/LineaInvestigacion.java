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
@Table(name = "linea_investigacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LineaInvestigacion.findAll", query = "SELECT l FROM LineaInvestigacion l")
    , @NamedQuery(name = "LineaInvestigacion.findByIdLinea", query = "SELECT l FROM LineaInvestigacion l WHERE l.idLinea = :idLinea")
    , @NamedQuery(name = "LineaInvestigacion.findByNombre", query = "SELECT l FROM LineaInvestigacion l WHERE l.nombre = :nombre")
    , @NamedQuery(name = "LineaInvestigacion.findByArea", query = "SELECT l FROM LineaInvestigacion l WHERE l.area = :area")})
public class LineaInvestigacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_LINEA")
    @Expose private Integer idLinea;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    @Expose private String nombre;
    @Basic(optional = false)
    @Column(name = "AREA")
    @Expose private String area;
    @JoinColumn(name = "ESCUELA", referencedColumnName = "ID_ESCUELA")
    @ManyToOne(optional = false)
    @Expose private Escuela escuela;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lineaId")
    private List<LineaDocente> lineaDocenteList;

    public LineaInvestigacion() {
    }

    public LineaInvestigacion(Integer idLinea) {
        this.idLinea = idLinea;
    }

    public LineaInvestigacion(Integer idLinea, String nombre, String area) {
        this.idLinea = idLinea;
        this.nombre = nombre;
        this.area = area;
    }

    public Integer getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Integer idLinea) {
        this.idLinea = idLinea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Escuela getEscuela() {
        return escuela;
    }

    public void setEscuela(Escuela escuela) {
        this.escuela = escuela;
    }

    @XmlTransient
    public List<LineaDocente> getLineaDocenteList() {
        return lineaDocenteList;
    }

    public void setLineaDocenteList(List<LineaDocente> lineaDocenteList) {
        this.lineaDocenteList = lineaDocenteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLinea != null ? idLinea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LineaInvestigacion)) {
            return false;
        }
        LineaInvestigacion other = (LineaInvestigacion) object;
        if ((this.idLinea == null && other.idLinea != null) || (this.idLinea != null && !this.idLinea.equals(other.idLinea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.LineaInvestigacion[ idLinea=" + idLinea + " ]";
    }
    
}
