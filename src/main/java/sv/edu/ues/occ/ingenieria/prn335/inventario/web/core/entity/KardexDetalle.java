package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;

import java.util.UUID;

@Entity
@Table(name = "kardex_detalle", schema = "public")
@NamedQueries({
        @NamedQuery(name="KardexDetalle.findByIdKardexDetalle", query = "SELECT kd FROM KardexDetalle kd WHERE kd.id = :idKardexDetalle"),
        @NamedQuery(name="KardexDetalle.findAllKardexDetalle", query = "SELECT kd FROM KardexDetalle kd"),
        @NamedQuery(name="KardexDetalle.findByLote", query = "SELECT kd FROM KardexDetalle kd WHERE kd.lote = :lote"),
        @NamedQuery(name="KardexDetalle.countByIdKardexDetalle", query = "SELECT COUNT(kd) FROM KardexDetalle kd WHERE kd.id = :idKardexDetalle"),
        @NamedQuery(name="KardexDetalle.countAllKardexDetalle", query = "SELECT COUNT(kd) FROM KardexDetalle kd"),
        @NamedQuery(name="KardexDetalle.countByLote", query = "SELECT COUNT(kd) FROM KardexDetalle kd WHERE kd.lote = :lote")
})
public class KardexDetalle {
    @Id
    @Column(name = "id_kardex_detalle", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "id_kardex")
    private Kardex idKardex;

    @Column(name = "lote", length = Integer.MAX_VALUE)
    private String lote;

    @Column(name = "activo")
    private Boolean activo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Kardex getIdKardex() {
        return idKardex;
    }

    public void setIdKardex(Kardex idKardex) {
        this.idKardex = idKardex;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

}