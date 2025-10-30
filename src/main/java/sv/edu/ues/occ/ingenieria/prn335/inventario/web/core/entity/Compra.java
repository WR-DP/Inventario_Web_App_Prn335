package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "compra", schema = "public")
@NamedQueries({
        @NamedQuery(name = "Compra.findAllCompra", query = "SELECT c FROM Compra c"),
        @NamedQuery(name = "Compra.findByIdCompra", query = "SELECT c FROM Compra c WHERE c.id = :idCompra"),
        @NamedQuery(name = "Compra.findByIdProveedor", query = "SELECT c FROM Compra c WHERE c.idProveedor.id = :idProveedor"),
        @NamedQuery(name = "Compra.countAllCompra", query = "SELECT COUNT(c) FROM Compra c")
})
public class Compra implements Serializable {

    @Id
    @Column(name = "id_compra", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private Proveedor idProveedor;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private OffsetDateTime fecha;

    @Size(max = 20)
    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    // --------- GETTERS & SETTERS -----------

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
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
}





