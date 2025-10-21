package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity;

import jakarta.persistence.*;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "unidad_medida", schema = "public")
@NamedQueries({
        @NamedQuery(name = "UnidadMedida.findByIdUnidadMedida", query = "SELECT um FROM UnidadMedida um WHERE um.id = :idUnidadMedida"),
        @NamedQuery(name = "UnidadMedida.findAllUnidadMedida", query = "SELECT um FROM UnidadMedida um"),
        @NamedQuery(name = "UnidadMedida.findByActivo", query = "SELECT um FROM UnidadMedida um WHERE um.activo = :activo"),
        @NamedQuery(name = "UnidadMedida.countAllUnidadMedida", query = "SELECT COUNT(um) FROM UnidadMedida um"),
        @NamedQuery(name = "UnidadMedida.countByActivo", query = "SELECT COUNT(um) FROM UnidadMedida um WHERE um.activo = :activo")
})
public class UnidadMedida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidad_medida", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "id_tipo_unidad_medida")
    private TipoUnidadMedida idTipoUnidadMedida;

    @Column(name = "equivalencia", precision = 8, scale = 2)
    private BigDecimal equivalencia;

    @Column(name = "expresion_regular", length = Integer.MAX_VALUE)
    private String expresionRegular;

    @Column(name = "activo")
    private Boolean activo;

    @Column(name = "comentarios", length = Integer.MAX_VALUE)
    private String comentarios;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoUnidadMedida getIdTipoUnidadMedida() {
        return idTipoUnidadMedida;
    }

    public void setIdTipoUnidadMedida(TipoUnidadMedida idTipoUnidadMedida) {
        this.idTipoUnidadMedida = idTipoUnidadMedida;
    }

    public BigDecimal getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(BigDecimal equivalencia) {
        this.equivalencia = equivalencia;
    }

    public String getExpresionRegular() {
        return expresionRegular;
    }

    public void setExpresionRegular(String expresionRegular) {
        this.expresionRegular = expresionRegular;
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

}