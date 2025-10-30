package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "proveedor", schema = "public")
@NamedQueries({
        @NamedQuery(name = "Proveedor.findByIdProveedor", query = "SELECT p FROM Proveedor p WHERE p.id = :idProveedor"),
        @NamedQuery(name = "Proveedor.findAllProveedor", query = "SELECT p FROM Proveedor p"),
        @NamedQuery(name = "Proveedor.findByActivo", query = "SELECT p FROM Proveedor p WHERE p.activo = :activo"),
        @NamedQuery(name = "Proveedor.countByIdProveedor", query = "SELECT COUNT(p) FROM Proveedor p WHERE p.id = :idProveedor"),
        @NamedQuery(name = "Proveedor.countAllProveedor", query = "SELECT COUNT(p) FROM Proveedor p"),
        @NamedQuery(name = "Proveedor.countByActivo", query = "SELECT COUNT(p) FROM Proveedor p WHERE p.activo = :activo")
})
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor", nullable = false)
    private Integer id;

    @Size(max = 155)
    @Column(name = "nombre", length = 155)
    private String nombre;

    @Size(max = 155)
    @Column(name = "razon_social", length = 155)
    private String razonSocial;

    @Size(max = 14)
    @Column(name = "nit", length = 14)
    private String nit;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}