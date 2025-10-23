package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoCaracteristicaDAO;
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

    protected String nombrebean = "page.tipoproductocaracteristica";

    public String getNombrebean() {
        return nombrebean;
    }

    public TipoProductoCaracteristicaFrm() {
    }

    List<TipoProductoCaracteristica> listaTipoProductoCaracteristica;

    public List<TipoProductoCaracteristica> getTipoProductoCaracteristicas() {
        return listaTipoProductoCaracteristica;
    }

    public void setListaTipoProductoCaracteristica(List<TipoProductoCaracteristica> listaTipoProductoCaracteristica) {
        this.listaTipoProductoCaracteristica = listaTipoProductoCaracteristica;
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
        listaTipoProductoCaracteristica = tipoProductoCaracteristicaDAO.findRange(0, Integer.MAX_VALUE);
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






}
