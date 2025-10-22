package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.awt.image.BufferedImageOp;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "tipo_producto", schema = "public")
@NamedQueries({
        //aqui si debemos implementar varias queries para buscar por diferentes campos
        @NamedQuery(name = "TipoProducto.findAllTipoProducto", query = "SELECT t FROM TipoProducto t"),
        @NamedQuery(name = "TipoProducto.findByIdTipoProducto", query = "SELECT t FROM TipoProducto t WHERE t.id = :id"),
        @NamedQuery(name = "TipoProducto.findByActivo", query = "SELECT t FROM TipoProducto t WHERE t.activo = :activo"),
        @NamedQuery(name = "TipoProducto.countByIdTipoProducto", query = "SELECT COUNT(t.id) FROM TipoProducto t where t.idTipoProductoPadre.id = :idTipoProducto"), // a lo mejor no funciona bien
        @NamedQuery(name="TipoProducto.findByNombreLike", query = "SELECT t FROM TipoProducto t WHERE upper(t.nombre) like :nombre")
})
public class TipoProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_producto", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "id_tipo_producto_padre")
    private TipoProducto idTipoProductoPadre;

    @Size(max = 155)
    @Column(name = "nombre", length = 155)
    private String nombre;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "comentarios", length = Integer.MAX_VALUE)
    private String comentarios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoProducto getIdTipoProductoPadre() {
        return idTipoProductoPadre;
    }

    public void setIdTipoProductoPadre(TipoProducto idTipoProductoPadre) {
        this.idTipoProductoPadre = idTipoProductoPadre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public boolean isEmpty() {
        return this.id == null || this.id == 0;
    }

    public Long getIdTipoProducto() {
        return id;
    }
    public java.util.stream.Stream<TipoProducto> stream() {
        return java.util.stream.Stream.of(this);
    }

}