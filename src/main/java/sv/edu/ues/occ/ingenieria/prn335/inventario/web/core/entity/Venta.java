package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "venta", schema = "public")
@NamedQueries({
        @NamedQuery(name = "Venta.findByIdVenta", query = "SELECT v FROM Venta v WHERE v.id = :idVenta"),
        @NamedQuery(name = "Venta.findAllVenta", query = "SELECT v FROM Venta v"),
        @NamedQuery(name = "Venta.findByIdCliente", query = "SELECT v FROM Venta v WHERE v.idCliente.id = :idCliente"),
        @NamedQuery(name = "Venta.findByFecha", query = "SELECT v FROM Venta v WHERE v.fecha = :fecha"),
        @NamedQuery(name = "Venta.findByEstado", query = "SELECT v FROM Venta v WHERE v.estado = :estado"),
        @NamedQuery(name = "Venta.countAllVenta", query = "SELECT COUNT(v) FROM Venta v"),
        @NamedQuery(name = "Venta.countByFecha", query = "SELECT COUNT(v) FROM Venta v WHERE v.fecha = :fecha"),
        @NamedQuery(name = "Venta.countByEstado", query = "SELECT COUNT(v) FROM Venta v WHERE v.estado = :estado")
})
public class Venta {
    @Id
    @Column(name = "id_venta", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "id_cliente")
    private Cliente idCliente;

    @Column(name = "fecha")
    private OffsetDateTime fecha;

    @Size(max = 10)
    @Column(name = "estado", length = 10)
    private String estado;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
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