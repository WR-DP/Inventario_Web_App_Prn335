package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "venta", schema = "public")
@NamedQueries({
        @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v"),
        @NamedQuery(name = "Venta.findByIdVenta", query = "SELECT v FROM Venta v WHERE v.id = :idVenta"),
        @NamedQuery(name = "Venta.findByIdCliente", query = "SELECT v FROM Venta v WHERE v.idCliente.id = :idCliente"),
        @NamedQuery(name = "Venta.countAll", query = "SELECT COUNT(v) FROM Venta v")
})
public class Venta implements Serializable {

    @Id
    @Column(name = "id_venta", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente idCliente;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP) // Para almacenar fecha y hora
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @Size(max = 20)
    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "observaciones", length = 500)
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
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
