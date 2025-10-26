package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoCaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Caracteristica;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProductoCaracteristica;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@Named
@ViewScoped
public class TipoProductoCaracteristicaFrm extends DefaultFrm<TipoProductoCaracteristica> implements Serializable {

    @Inject
    FacesContext facesContext;

    @Inject
    TipoProductoCaracteristicaDAO tipoProductoCaracteristicaDAO;

    @Inject
    CaracteristicaDAO caracteristicaDAO;

    List<Caracteristica> posibleCaracteristicas;

    List<TipoProductoCaracteristica> caracteristicasAsignadas;

    protected Long idCaracteristica;

    protected String nombrebean = "page.tipoproductocaracteristica";

    @Named("tipoProductoFrm")
    @Inject
    private TipoProductoFrm tipoProductoFrm;

    public String getNombrebean() {
        return nombrebean;
    }

    public TipoProductoCaracteristicaFrm() {}

    public Long getIdTipoProductoCaracteristicaSeleccionado() {
    if(this.registro != null && this.registro.getIdTipoProducto() != null){
            return this.registro.getIdTipoProducto().getId();
        }
        return null;
    }

    public void setIdTipoProductoCaracteristicaSeleccionado(final Long idTipoProductoSeleccionado) {
        if(this.registro != null && this.listaTipoProductoCaracteristica != null && !this.listaTipoProductoCaracteristica.isEmpty()){
            this.registro.setIdTipoProducto(this.listaTipoProductoCaracteristica.stream()
                    .filter(tp-> tp.getId().equals(idTipoProductoSeleccionado))
                    .findFirst().orElse(null).getIdTipoProducto());
        }
    }


    List<TipoProductoCaracteristica> listaTipoProductoCaracteristica;

    @Override
    public List<TipoProductoCaracteristica> cargarDatos(int first, int max) {
        try{
            if(first>=0 && max>0 && this.idCaracteristica!=null){
                return tipoProductoCaracteristicaDAO.findByIdCaracteristica(this.idCaracteristica, first, max);
            }
        }catch(Exception ex){
            Logger.getLogger(TipoProductoCaracteristicaFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return listaTipoProductoCaracteristica;
    }

    @Override
    public int contarDatos() {
        try{
            if(this.idCaracteristica!=null){
                return this.tipoProductoCaracteristicaDAO.countByIdCaracteristica(this.idCaracteristica).intValue();
            }
        }catch(Exception ex){
            Logger.getLogger(TipoProductoCaracteristicaFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return 0;
    }


    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<TipoProductoCaracteristica, Object> getDao() {
        return tipoProductoCaracteristicaDAO;
    }

    @Override
    protected String getIdAsText(TipoProductoCaracteristica r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected TipoProductoCaracteristica getIdByText(String id) {
        if (id != null) {
            try {
                Long buscado = Long.parseLong(id);
                return this.modelo.getWrappedData().stream().filter(r -> r.getId().equals(buscado)).findFirst().orElse(null);
            } catch (IllegalArgumentException e) {
                Logger.getLogger(TipoProductoCaracteristicaFrm.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return null;
    }

    @PostConstruct
    @Override
    public void inicializar() {
        super.inicializar();
        if (idCaracteristica != null) {
            listaTipoProductoCaracteristica =
                    tipoProductoCaracteristicaDAO.findByIdCaracteristica(idCaracteristica, 0, Integer.MAX_VALUE);
        }
    }

    @Override
    protected TipoProductoCaracteristica nuevoRegistro() {
        TipoProductoCaracteristica tipoProductoCaracteristica = new TipoProductoCaracteristica();
        tipoProductoCaracteristica.setFechaCreacion(OffsetDateTime.now());
        tipoProductoCaracteristica.setObligatorio(true);
        return tipoProductoCaracteristica;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return tipoProductoCaracteristicaDAO;
    }

    @Override
    protected TipoProductoCaracteristica buscarRegistroPorId(Object id) {
        if (id != null && this.modelo != null) {
            try {
                Long idLong = Long.parseLong(id.toString());
                return this.modelo.getWrappedData().stream().filter(r -> r.getId().equals(idLong)).findFirst().orElse(null);
            } catch (IllegalArgumentException e) {
                Logger.getLogger(TipoProductoCaracteristicaFrm.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return null;
    }

    public List<Caracteristica> buscarCaracteristicaPorNombres(final String nombres) {
        try {
            if (nombres != null && !nombres.isBlank()) {
                return caracteristicaDAO.findByNombreLike(nombres, 0, 25);
            }
        } catch (Exception ex) {
            Logger.getLogger(TipoProductoCaracteristicaFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    @Override
    public void btnGuardarHandler(ActionEvent actionEvent) {
        // ✅ Vincular el TipoProducto seleccionado
        if (this.registro != null && tipoProductoFrm != null && tipoProductoFrm.getRegistro() != null) {
            this.registro.setIdTipoProducto(tipoProductoFrm.getRegistro());
        }

        super.btnGuardarHandler(actionEvent);

        // ✅ Recargar datos luego de guardar
        if (this.estado == ESTADO_CRUD.NADA) {
            this.inicializar();
        }
    }


    public void btnSeleccionarCaracteristicaHandler(ActionEvent actionEvent) {
        if (this.registro != null && this.registro.getIdCaracteristica() != null) {
            this.posibleCaracteristicas = caracteristicaDAO.findByIdCaracteristica(this.registro.getIdCaracteristica().getId(), 0, Integer.MAX_VALUE);
        } else {
            this.posibleCaracteristicas = List.of();
        }

        //        try{
//            this.posibleCaracteristicas= caracteristicaDAO.findByIdCaracteristica(this.registro.getIdCaracteristica().getId(),0,Integer.MAX_VALUE);
//            return;
//        }catch(Exception ex){
//            Logger.getLogger(TipoProductoCaracteristicaFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
//        }
//        this.posibleCaracteristicas = List.of();
    }

    public List<TipoProductoCaracteristica> getTipoProductoCaracteristicas() {
        return listaTipoProductoCaracteristica;
    }

    public void setListaTipoProductoCaracteristica(List<TipoProductoCaracteristica> listaTipoProductoCaracteristica) {
        this.listaTipoProductoCaracteristica = listaTipoProductoCaracteristica;
    }

    public List<Caracteristica> getPosibleCaracteristicas() {
        return posibleCaracteristicas;
    }

    public void setPosibleCaracteristicas(List<Caracteristica> posibleCaracteristicas) {
        this.posibleCaracteristicas = posibleCaracteristicas;
    }

    public List<TipoProductoCaracteristica> getCaracteristicasAsignadas() {
        return caracteristicasAsignadas;
    }

    public void setCaracteristicasAsignadas(List<TipoProductoCaracteristica> caracteristicasAsignadas) {
        this.caracteristicasAsignadas = caracteristicasAsignadas;
    }

    public Long getIdCaracteristica() {
        return idCaracteristica;
    }

    public void setIdCaracteristica(Long idCaracteristica) {
        this.idCaracteristica = idCaracteristica;
    }
}
