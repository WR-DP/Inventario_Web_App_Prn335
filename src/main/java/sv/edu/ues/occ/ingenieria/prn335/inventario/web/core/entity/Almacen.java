package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "almacen", schema = "public")
@NamedQueries({
        @NamedQuery(name = "Almacen.findByIdAlmacen", query = "SELECT a FROM Almacen a WHERE a.id = :id"),
        @NamedQuery(name = "Almacen.findAlmacenWithTipoAlmacen", query = "SELECT a FROM Almacen a JOIN a.idTipoAlmacen ta WHERE ta.id = :idTipoAlmacen"),
        @NamedQuery(name = "Almacen.findAll", query = "SELECT a FROM Almacen a")
})
public class Almacen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_almacen", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_almacen")
    private TipoAlmacen idTipoAlmacen;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "observaciones", length = Integer.MAX_VALUE)
    private String observaciones;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public TipoAlmacen getIdTipoAlmacen() {
        return idTipoAlmacen;
    }

    public void setIdTipoAlmacen(TipoAlmacen idTipoAlmacen) {
        this.idTipoAlmacen = idTipoAlmacen;
    }
}