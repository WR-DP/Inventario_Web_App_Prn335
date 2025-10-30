package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProveedorDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
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

    public ProveedorFrm() {}

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
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Proveedor getIdByText(String id) {
        if (id != null) {
            try {
                UUID buscado = UUID.fromString(id);
                return this.modelo.getWrappedData().stream()
                        .filter(r -> r.getId().equals(buscado))
                        .findFirst().orElse(null);
            } catch (IllegalArgumentException e) {
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
        Proveedor proveedor = new Proveedor();
        proveedor.setId(UUID.randomUUID());
        proveedor.setNombre("");
        proveedor.setActivo(true);
        return proveedor;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return proveedorDAO;
    }

    @Override
    protected Proveedor buscarRegistroPorId(Object id) {
        if (id instanceof UUID buscado && this.modelo != null) {
            return this.modelo.getWrappedData().stream()
                    .filter(r -> r.getId().equals(buscado))
                    .findFirst().orElse(null);
        }
        return null;
    }

    public List<Proveedor> buscarProveedorPorNombre(final String nombre){
        try{
            if(nombre != null && !nombre.isEmpty()) {
                return proveedorDAO.buscarProveedorPorNombre(nombre, 0, Integer.MAX_VALUE);
            }
        }catch (Exception ex){
            Logger.getLogger(ProveedorFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    // GETTERS / SETTERS

    public List<Proveedor> getListaProveedores() {
        return proveedores;
    }

    public void setListaProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }
}

