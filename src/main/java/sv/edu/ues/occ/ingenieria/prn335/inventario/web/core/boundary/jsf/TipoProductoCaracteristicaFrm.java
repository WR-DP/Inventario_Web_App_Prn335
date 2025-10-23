package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoCaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProductoCaracteristica;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class TipoProductoCaracteristicaFrm extends DefaultFrm<TipoProductoCaracteristica> implements Serializable {

    @Inject
    FacesContext facesContext;

    @Inject
    TipoProductoCaracteristicaDAO  tipoProductoCaracteristicaDAO;

    //implementar las funcionalidad de cada metodo
    //agregar la verificacion de los datos y objetos qeu se van a traer e inyectar

    protected String nombrebean="page.tipoproductocaracteristica";
    public String getNombrebean() {
        return nombrebean;
    }

    public TipoProductoCaracteristicaFrm() {}

    List<TipoProductoCaracteristica> listaTipoProductoCaracteristica;

    public List<TipoProductoCaracteristica> getTipoProductoCaracteristicas() {
        return listaTipoProductoCaracteristica;
    }

    public void setListaTipoProductoCaracteristica(List<TipoProductoCaracteristica> listaTipoProductoCaracteristica) {
        this.listaTipoProductoCaracteristica = listaTipoProductoCaracteristica;
    }


    @Override
    protected FacesContext getFacesContext() {
        return null;
    }

    @Override
    protected InventarioDAOInterface<TipoProductoCaracteristica, Object> getDao() {
        return null;
    }

    @Override
    protected String getIdAsText(TipoProductoCaracteristica r) {
        return "";
    }

    @Override
    protected TipoProductoCaracteristica getIdByText(String id) {
        return null;
    }

    @Override
    protected TipoProductoCaracteristica nuevoRegistro() {
        return null;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return null;
    }

    @Override
    protected TipoProductoCaracteristica buscarRegistroPorId(Object id) {
        return null;
    }
}
