package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "producto_tipo_producto_caracteristica", schema = "public")
@NamedQueries({
        //tiene que traer los campos caracteristicas de un producto especifico por medio del id del producto, por ende primero necesitamos mapear bien las entidades
        @NamedQuery(name = "ProductoTipoProductoCaracteristica.findByIdProducto",
                query = "SELECT p FROM ProductoTipoProductoCaracteristica p WHERE p.idProductoTipoProducto.idProducto.id = :idProducto"),
        @NamedQuery(name = "ProductoTipoProductoCaracteristica.findByProductoTipoProductoId",
                query = "SELECT p FROM ProductoTipoProductoCaracteristica p WHERE p.idProductoTipoProducto.id = :idProductoTipoProducto"),
        @NamedQuery(name="ProductoTipoProductoCaracteristica.findByIdProductoTipoProducto",
                query ="SELECT p FROM ProductoTipoProductoCaracteristica p WHERE p.idProductoTipoProducto.id = :id"),
        @NamedQuery(name="ProductoTipoProductoCaracteristica.removeByProductoTipoProductoAndCaracteristica",
                query="DELETE FROM ProductoTipoProductoCaracteristica p WHERE p.idProductoTipoProducto.id = :idPtpp AND p.idTipoProductoCaracteristica.id = :idCar")
})
public class ProductoTipoProductoCaracteristica {
    @Id
    @Column(name = "id_producto_tipo_producto_caracteristica", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto_tipo_producto")
    private ProductoTipoProducto idProductoTipoProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_producto_caracteristica")
    private TipoProductoCaracteristica idTipoProductoCaracteristica;

    @Column(name = "valor", length = Integer.MAX_VALUE)
    private String valor;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public ProductoTipoProducto getIdProductoTipoProducto() {
        return idProductoTipoProducto;
    }
    public void setIdProductoTipoProducto(ProductoTipoProducto idProductoTipoProducto) {
        this.idProductoTipoProducto = idProductoTipoProducto;
    }
    public TipoProductoCaracteristica getIdTipoProductoCaracteristica() {
        return idTipoProductoCaracteristica;
    }
    public void setIdTipoProductoCaracteristica(TipoProductoCaracteristica idTipoProductoCaracteristica) {
        this.idTipoProductoCaracteristica = idTipoProductoCaracteristica;
    }
    public String getValor() {
        return valor;
    }
    public void setValor(String valor) {
        this.valor = valor;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}