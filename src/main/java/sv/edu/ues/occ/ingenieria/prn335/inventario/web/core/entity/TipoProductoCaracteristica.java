package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tipo_producto_caracteristica", schema = "public")
@NamedQueries({
        @NamedQuery(name = "TipoProductoCaracteristica.findByIdTipoProducto", query = "SELECT tpc FROM TipoProductoCaracteristica tpc WHERE tpc.idTipoProducto.id = :idTipoProducto"),
        @NamedQuery(name = "TipoProductoCaracteristica.findByNombreCaracteristica", query = "SELECT tpc FROM TipoProductoCaracteristica tpc WHERE tpc.idCaracteristica.nombre = :nombreCaracteristica"),
        @NamedQuery(name = "TipoProductoCaracteristica.findByIdPadre", query = "SELECT tpc FROM TipoProductoCaracteristica tpc WHERE tpc.idTipoProducto.idTipoProductoPadre = :idTipoProductoPadre"),
        @NamedQuery(name = "TipoProductoCaracteristica.countById", query = "SELECT COUNT(tpc) FROM TipoProductoCaracteristica tpc WHERE tpc.id = :id"),
        @NamedQuery(name="TipoProductoCaracteristica.findByIdTipoProductoCaracteristica", query = "SELECT tpc FROM TipoProductoCaracteristica tpc WHERE tpc.idTipoProducto.id = :idTipoProducto"),
        @NamedQuery(name = "TipoProductoCaracteristica.findById", query = "SELECT tpc FROM TipoProductoCaracteristica tpc WHERE tpc.id = :id"),
        @NamedQuery(name = "TipoProductoCaracteristica.countByIdTipoProducto", query = "SELECT COUNT(tpc) FROM TipoProductoCaracteristica tpc WHERE tpc.idTipoProducto.id = :idTipoProducto")
})
public class TipoProductoCaracteristica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_producto_caracteristica", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_caracteristica")
    private Caracteristica idCaracteristica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_producto")
    private TipoProducto idTipoProducto;

    @Column(name = "obligatorio")
    private Boolean obligatorio;

    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof TipoProductoCaracteristica)) return false;
        TipoProductoCaracteristica other = (TipoProductoCaracteristica) o;
        if (this.id == null || other.id == null) {
            return false;
        }
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Caracteristica getIdCaracteristica() {
        return idCaracteristica;
    }

    public void setIdCaracteristica(Caracteristica idCaracteristica) {
        this.idCaracteristica = idCaracteristica;
    }

    public TipoProducto getIdTipoProducto() {
        return idTipoProducto;
    }

    public void setIdTipoProducto(TipoProducto idTipoProducto) {
        this.idTipoProducto = idTipoProducto;
    }

    public Boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public java.util.stream.Stream<TipoProductoCaracteristica> stream() {
        return java.util.stream.Stream.of(this);
    }

}