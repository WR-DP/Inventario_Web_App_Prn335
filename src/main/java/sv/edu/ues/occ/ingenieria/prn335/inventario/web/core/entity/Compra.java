package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "compra", schema = "public")
@NamedQueries({
        @NamedQuery(name= "Compra.findByIdCompra", query = "SELECT c FROM Compra c WHERE c.id = :idCompra"),
        @NamedQuery(name= "Compra.findAllCompra", query = "SELECT c FROM Compra c"),
        @NamedQuery(name= "Compra.findByEstado", query = "SELECT c FROM Compra c WHERE c.estado = :estado"),
        @NamedQuery(name="Compra.findByFecha", query = "SELECT c FROM Compra c WHERE c.fecha = :fecha"),
        @NamedQuery(name= "Compra.countByIdCompra", query = "SELECT COUNT(c) FROM Compra c WHERE c.id = :idCompra"),
        @NamedQuery(name= "Compra.countAllCompra", query = "SELECT COUNT(c) FROM Compra c")
})
public class Compra {
    @Id
    @Column(name = "id_compra", nullable = false)
    private Long id;

    @Column(name = "fecha")
    private OffsetDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id_proveedor")
    private Proveedor idProveedor;

    @Size(max = 10)
    @Column(name = "estado", length = 10)
    private String estado;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getFecha() {
        return fecha;
    }

    public void setFecha(OffsetDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }
    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
    }
}