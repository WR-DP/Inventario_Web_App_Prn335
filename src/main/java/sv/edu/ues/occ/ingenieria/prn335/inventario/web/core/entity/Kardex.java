package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "kardex", schema = "public")
@NamedQueries({
        //query para buscar el almacen con mas productos desde kardex
        @NamedQuery(name = "Kardex.findAlmacenWithMostProductos", query = "SELECT k.idAlmacen, COUNT(k.idProducto) AS productCount FROM Kardex k GROUP BY k.idAlmacen ORDER BY productCount DESC"),
        @NamedQuery(name= "Kardex.findByIdKardex", query = "SELECT k FROM Kardex k WHERE k.id = :idKardex"),
        @NamedQuery(name= "Kardex.findAllKardex", query = "SELECT k FROM Kardex k"),
        @NamedQuery(name= "Kardex.findByIdCompra", query = "SELECT k FROM Kardex k WHERE k.idCompraDetalle.id = :idCompraDetalle"),
        @NamedQuery(name= "Kardex.findByIdVenta", query = "SELECT k FROM Kardex k WHERE k.idVentaDetalle.id = :idVentaDetalle"),
        @NamedQuery(name= "Kardex.findByIdAlmacen", query = "SELECT k FROM Kardex k WHERE k.idAlmacen.id = :idAlmacen"),
        @NamedQuery(name= "Kardex.findByCantidad", query = "SELECT k FROM Kardex k WHERE k.cantidad = :cantidad"),
        @NamedQuery(name= "Kardex.findByPrecioRange", query = "SELECT k FROM Kardex k WHERE k.precio BETWEEN :minPrecio AND :maxPrecio"),
        @NamedQuery(name= "Kardex.countByIdKardex", query = "SELECT COUNT(k) FROM Kardex k WHERE k.id = :idKardex"),
        @NamedQuery(name= "Kardex.countAll", query = "SELECT COUNT(k) FROM Kardex k")
})
public class Kardex {
    @Id
    @Column(name = "id_kardex", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "id_producto")
    private Producto idProducto;

    @Column(name = "fecha")
    private OffsetDateTime fecha;

    @Size(max = 25)
    @Column(name = "tipo_movimiento", length = 25)
    private String tipoMovimiento;

    @Column(name = "cantidad", precision = 8, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "precio", precision = 8, scale = 2)
    private BigDecimal precio;

    @Column(name = "cantidad_actual", precision = 8, scale = 2)
    private BigDecimal cantidadActual;

    @Column(name = "precio_actual", precision = 8, scale = 2)
    private BigDecimal precioActual;

    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "id_compra_detalle")
    private CompraDetalle idCompraDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "id_venta_detalle")
    private VentaDetalle idVentaDetalle;

    @Column(name = "referencia_externa", length = Integer.MAX_VALUE)
    private String referenciaExterna;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_almacen")
    private Almacen idAlmacen;

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

    public OffsetDateTime getFecha() {
        return fecha;
    }

    public void setFecha(OffsetDateTime fecha) {
        this.fecha = fecha;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getCantidadActual() {
        return cantidadActual;
    }

    public void setCantidadActual(BigDecimal cantidadActual) {
        this.cantidadActual = cantidadActual;
    }

    public BigDecimal getPrecioActual() {
        return precioActual;
    }

    public void setPrecioActual(BigDecimal precioActual) {
        this.precioActual = precioActual;
    }

    public CompraDetalle getIdCompraDetalle() {
        return idCompraDetalle;
    }

    public void setIdCompraDetalle(CompraDetalle idCompraDetalle) {
        this.idCompraDetalle = idCompraDetalle;
    }

    public VentaDetalle getIdVentaDetalle() {
        return idVentaDetalle;
    }

    public void setIdVentaDetalle(VentaDetalle idVentaDetalle) {
        this.idVentaDetalle = idVentaDetalle;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Almacen getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(Almacen idAlmacen) {
        this.idAlmacen = idAlmacen;
    }


}
