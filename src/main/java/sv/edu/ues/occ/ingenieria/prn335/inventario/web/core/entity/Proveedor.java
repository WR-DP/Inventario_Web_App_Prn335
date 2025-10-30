package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "proveedor", schema = "public")
@NamedQueries({
        @NamedQuery(name="Proveedor.findByIdProveedor", query = "SELECT p FROM Proveedor p WHERE p.id = :idProveedor"),
        @NamedQuery(name="Proveedor.findAllProveedor", query = "SELECT p FROM Proveedor p"),
        @NamedQuery(name="Proveedor.findByActivo", query = "SELECT p FROM Proveedor p WHERE p.activo = :activo"),
        @NamedQuery(name="Proveedor.countAllProveedor", query = "SELECT COUNT(p) FROM Proveedor p"),
        @NamedQuery(name="Proveedor.countByActivo", query = "SELECT COUNT(p) FROM Proveedor p WHERE p.activo = :activo"),
        @NamedQuery(name="Proveedor.buscarProveedorPorNombre", query = "SELECT p FROM Proveedor p WHERE upper(p.nombre) LIKE :nombre")
})
public class Proveedor {

    @Id
    @Column(name = "id_proveedor", nullable = false)
    private UUID id;

    @Size(max = 155)
    @Column(name = "nombre", length = 155)
    private String nombre;

    @Column(name = "activo")
    private Boolean activo;

    // ========= GETTERS & SETTERS ==========

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
