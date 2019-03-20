/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author JhanxD
 */
@Entity
@Table(name = "alumno")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alumno.findAll", query = "SELECT a FROM Alumno a")
    , @NamedQuery(name = "Alumno.findByIdAlumno", query = "SELECT a FROM Alumno a WHERE a.idAlumno = :idAlumno")
    , @NamedQuery(name = "Alumno.findByNombre", query = "SELECT a FROM Alumno a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "Alumno.findByAPaterno", query = "SELECT a FROM Alumno a WHERE a.aPaterno = :aPaterno")
    , @NamedQuery(name = "Alumno.findByAMaterno", query = "SELECT a FROM Alumno a WHERE a.aMaterno = :aMaterno")
    , @NamedQuery(name = "Alumno.findByDni", query = "SELECT a FROM Alumno a WHERE a.dni = :dni")
    , @NamedQuery(name = "Alumno.findByEmail", query = "SELECT a FROM Alumno a WHERE a.email = :email")
    , @NamedQuery(name = "Alumno.findByCelular", query = "SELECT a FROM Alumno a WHERE a.celular = :celular")
    , @NamedQuery(name = "Alumno.findByGradoAcademico", query = "SELECT a FROM Alumno a WHERE a.gradoAcademico = :gradoAcademico")
    , @NamedQuery(name = "Alumno.findByUniOrigen", query = "SELECT a FROM Alumno a WHERE a.uniOrigen = :uniOrigen")
    , @NamedQuery(name = "Alumno.findByEstadot", query = "SELECT a FROM Alumno a WHERE a.estadot = :estadot")})
public class Alumno implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_ALUMNO")
    @Expose private Integer idAlumno;
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
    @Column(name = "DNI")
    @Expose private int dni;
    @Basic(optional = false)
    @Column(name = "EMAIL")
    @Expose private String email;
    @Basic(optional = false)
    @Column(name = "CELULAR")
    @Expose private int celular;
    @Basic(optional = false)
    @Column(name = "GRADO_ACADEMICO")
    @Expose private String gradoAcademico;
    @Basic(optional = false)
    @Column(name = "UNI_ORIGEN")
    @Expose private String uniOrigen;
    @Basic(optional = false)
    @Column(name = "ESTADOT")
    @Expose private int estadot;
    @JoinColumn(name = "ESCUELA", referencedColumnName = "ID_ESCUELA")
    @ManyToOne(optional = false)
    @Expose private Escuela escuela;

    public Alumno() {
    }

    public Alumno(Integer idAlumno) {
        this.idAlumno = idAlumno;
    }

    public Alumno(Integer idAlumno, String nombre, String aPaterno, String aMaterno, int dni, String email, int celular, String gradoAcademico, String uniOrigen, int estadot) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.aPaterno = aPaterno;
        this.aMaterno = aMaterno;
        this.dni = dni;
        this.email = email;
        this.celular = celular;
        this.gradoAcademico = gradoAcademico;
        this.uniOrigen = uniOrigen;
        this.estadot = estadot;
    }

    public Integer getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Integer idAlumno) {
        this.idAlumno = idAlumno;
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

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCelular() {
        return celular;
    }

    public void setCelular(int celular) {
        this.celular = celular;
    }

    public String getGradoAcademico() {
        return gradoAcademico;
    }

    public void setGradoAcademico(String gradoAcademico) {
        this.gradoAcademico = gradoAcademico;
    }

    public String getUniOrigen() {
        return uniOrigen;
    }

    public void setUniOrigen(String uniOrigen) {
        this.uniOrigen = uniOrigen;
    }

    public int getEstadot() {
        return estadot;
    }

    public void setEstadot(int estadot) {
        this.estadot = estadot;
    }

    public Escuela getEscuela() {
        return escuela;
    }

    public void setEscuela(Escuela escuela) {
        this.escuela = escuela;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAlumno != null ? idAlumno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alumno)) {
            return false;
        }
        Alumno other = (Alumno) object;
        if ((this.idAlumno == null && other.idAlumno != null) || (this.idAlumno != null && !this.idAlumno.equals(other.idAlumno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Beans.Alumno[ idAlumno=" + idAlumno + " ]";
    }
    
}
