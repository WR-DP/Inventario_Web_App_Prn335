package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "producto_tipo_producto", schema = "public")
@NamedQueries({
        @NamedQuery(name = "ProductoTipoProducto.findAll", query = "SELECT p FROM ProductoTipoProducto p"),
        @NamedQuery(name = "ProductoTipoProducto.findByIdProductoTipoProducto", query = "SELECT p FROM ProductoTipoProducto p WHERE p.id = :id"),
        @NamedQuery(name = "ProductoTipoProducto.findByIdProducto", query = "SELECT p FROM ProductoTipoProducto p WHERE p.idProducto.id = :idProducto"),
        @NamedQuery(name = "ProductoTipoProducto.countAll", query = "SELECT COUNT(p) FROM ProductoTipoProducto p"),
        @NamedQuery(name = "ProductoTipoProducto.countByIdProductoTipoProducto", query = "SELECT COUNT(p) FROM ProductoTipoProducto p WHERE p.id = :id"),
        @NamedQuery(name = "ProductoTipoProducto.countByIdProducto", query = "SELECT COUNT(p) FROM ProductoTipoProducto p WHERE p.idProducto.id = :idProducto")
})
public class ProductoTipoProducto {
    @Id
    @Column(name = "id_producto_tipo_producto", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto idProducto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tipo_producto")
    @NotNull
    private TipoProducto idTipoProducto;

    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public Producto getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }
    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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
    public java.util.stream.Stream<ProductoTipoProducto> stream() {
        return java.util.stream.Stream.of(this);
    }
    public @NotNull TipoProducto getIdTipoProducto() {
        return idTipoProducto;
    }
    public void setIdTipoProducto(@NotNull TipoProducto idTipoProducto) {
        this.idTipoProducto = idTipoProducto;
    }
}