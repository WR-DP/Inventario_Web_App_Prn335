package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "producto", schema = "public")
@NamedQueries({
        @NamedQuery(name = "Producto.findByIdProducto", query = "SELECT p FROM Producto p WHERE p.id = :id"),
        @NamedQuery(name = "Producto.findAllProducto", query = "SELECT p FROM Producto p"),
        @NamedQuery(name = "Producto.findByActivo", query = "SELECT p FROM Producto p WHERE p.activo = :activo"),
        @NamedQuery(name = "Producto.countAllProducto", query = "SELECT COUNT(p) FROM Producto p"),
        @NamedQuery(name = "Producto.countByActivo", query = "SELECT COUNT(p) FROM Producto p WHERE p.activo = :activo"),
        @NamedQuery(name = "Producto.countByIdProducto", query = "SELECT COUNT(p) FROM Producto p WHERE p.id = :id"),
        @NamedQuery(name="Producto.buscarProductosPorNombre", query = "SELECT p FROM Producto p WHERE upper(p.nombreProducto) LIKE upper(:nombreProducto)")
})
public class Producto {
    @Id
    @Column(name = "id_producto", nullable = false)
    private UUID id;

    @Size(max = 155)
    @Column(name = "nombre_producto", length = 155)
    private String nombreProducto;

    @Column(name = "referencia_externa", length = Integer.MAX_VALUE)
    private String referenciaExterna;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "comentarios", length = Integer.MAX_VALUE)
    private String comentarios;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

}