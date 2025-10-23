package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProveedorDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ProveedorFrm extends DefaultFrm<Proveedor> implements Serializable {

    @Inject
    FacesContext facesContext;

    @Inject
    ProveedorDAO proveedorDAO;

    private List<Proveedor> proveedores;

    public ProveedorFrm() {
    }


    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<Proveedor, Object> getDao() {
        return proveedorDAO;
    }

    @Override
    protected String getIdAsText(Proveedor r) {
        if (r != null && r.getId() != null) {
            return String.valueOf(r.getId());
        }
        return null;
    }

    @Override
    protected Proveedor getIdByText(String id) {
        if (id != null) {
            try {
                Integer buscado = Integer.valueOf(id);
                return this.modelo.getWrappedData()
                        .stream()
                        .filter(r -> r.getId().equals(buscado))
                        .findFirst()
                        .orElse(null);
            } catch (NumberFormatException e) {
                Logger.getLogger(ProveedorFrm.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    @PostConstruct
    @Override
    public void inicializar() {
        super.inicializar();
        proveedores = proveedorDAO.findRange(0, Integer.MAX_VALUE);
    }

    @Override
    protected Proveedor nuevoRegistro() {
        Proveedor p = new Proveedor();
        p.setActivo(true);
        p.setNombre("");
        p.setRazonSocial("");
        p.setNit("");
        p.setObservaciones("");
        return p;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return proveedorDAO;
    }

    @Override
    protected Proveedor buscarRegistroPorId(Object id) {
        if (id instanceof Integer buscado && this.modelo != null) {
            return this.modelo.getWrappedData()
                    .stream()
                    .filter(r -> r.getId().equals(buscado))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    protected String nombreBean = "page.proveedor";

    public String getNombreBean() {
        return nombreBean;
    }

    public void setNombreBean(String nombreBean) {
        this.nombreBean = nombreBean;
    }

    public List<Proveedor> getListaProveedores() {
        return proveedores;
    }

    public void setListaProveedores(List<Proveedor> listaProveedores) {
        this.proveedores = listaProveedores;
    }
}
