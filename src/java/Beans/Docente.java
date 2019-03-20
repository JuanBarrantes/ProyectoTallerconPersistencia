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
@Table(name = "docente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Docente.findAll", query = "SELECT d FROM Docente d")
    , @NamedQuery(name = "Docente.findByIdDocente", query = "SELECT d FROM Docente d WHERE d.idDocente = :idDocente")
    , @NamedQuery(name = "Docente.findByNombre", query = "SELECT d FROM Docente d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "Docente.findByAPaterno", query = "SELECT d FROM Docente d WHERE d.aPaterno = :aPaterno")
    , @NamedQuery(name = "Docente.findByAMaterno", query = "SELECT d FROM Docente d WHERE d.aMaterno = :aMaterno")
    , @NamedQuery(name = "Docente.findByGradoAcademico", query = "SELECT d FROM Docente d WHERE d.gradoAcademico = :gradoAcademico")
    , @NamedQuery(name = "Docente.findByGa", query = "SELECT d FROM Docente d WHERE d.ga = :ga")
    , @NamedQuery(name = "Docente.findByCategoria", query = "SELECT d FROM Docente d WHERE d.categoria = :categoria")
    , @NamedQuery(name = "Docente.findByC", query = "SELECT d FROM Docente d WHERE d.c = :c")
    , @NamedQuery(name = "Docente.findByEmail", query = "SELECT d FROM Docente d WHERE d.email = :email")
    , @NamedQuery(name = "Docente.findByFechaIngreso", query = "SELECT d FROM Docente d WHERE d.fechaIngreso = :fechaIngreso")})
public class Docente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_DOCENTE")
    @Expose private Integer idDocente;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    @Expose private String nombre;
    @Basic(optional = false)
    @Column(name = "A_PATERNO")
    @Expose private String aPaterno;
    @Basic(optional = false)
    @Column(name = "A_MATERNO")
    @Expose private String aMaterno;
    @Basic(optional = false)
    @Column(name = "GRADO_ACADEMICO")
    @Expose private String gradoAcademico;
    @Basic(optional = false)
    @Column(name = "GA")
    @Expose private int ga;
    @Basic(optional = false)
    @Column(name = "CATEGORIA")
    @Expose private String categoria;
    @Basic(optional = false)
    @Column(name = "C")
    @Expose private int c;
    @Basic(optional = false)
    @Column(name = "EMAIL")
    @Expose private String email;
    @Basic(optional = false)
    @Column(name = "FECHA_INGRESO")
    @Temporal(TemporalType.DATE)
    @Expose private Date fechaIngreso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDocente")
    private List<Jurado> juradoList;
    @JoinColumn(name = "ESCUELA", referencedColumnName = "ID_ESCUELA")
    @ManyToOne(optional = false)
    @Expose private Escuela escuela;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docenteId")
    private List<LineaDocente> lineaDocenteList;

    public Docente() {
    }

    public Docente(Integer idDocente) {
        this.idDocente = idDocente;
    }

    public Docente(Integer idDocente, String nombre, String aPaterno, String aMaterno, String gradoAcademico, int ga, String categoria, int c, String email, Date fechaIngreso) {
        this.idDocente = idDocente;
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
        this.gradoAcademico = gradoAcademico;
        this.ga = ga;
        this.categoria = categoria;
        this.c = c;
        this.email = email;
        this.fechaIngreso = fechaIngreso;
    }

    public Integer getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Integer idDocente) {
        this.idDocente = idDocente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAPaterno() {
        return aPaterno;
    }

    public void setAPaterno(String aPaterno) {
        this.aPaterno = aPaterno;
    }

    public String getAMaterno() {
        return aMaterno;
    }

    public void setAMaterno(String aMaterno) {
        this.aMaterno = aMaterno;
    }

    public String getGradoAcademico() {
        return gradoAcademico;
    }

    public void setGradoAcademico(String gradoAcademico) {
        this.gradoAcademico = gradoAcademico;
    }

    public int getGa() {
        return ga;
    }

    public void setGa(int ga) {
        this.ga = ga;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    @XmlTransient
    public List<Jurado> getJuradoList() {
        return juradoList;
    }

    public void setJuradoList(List<Jurado> juradoList) {
        this.juradoList = juradoList;
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
        hash += (idDocente != null ? idDocente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Docente)) {
            return false;
        }
        Docente other = (Docente) object;
        if ((this.idDocente == null && other.idDocente != null) || (this.idDocente != null && !this.idDocente.equals(other.idDocente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Docente[ idDocente=" + idDocente + " ]";
    }
    
}
