package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CompraDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProveedorDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class CompraFrm extends DefaultFrm<Compra> implements Serializable {

    @Inject
    FacesContext facesContext;

    @Inject
    CompraDAO compraDAO;

    @Inject
    ProveedorDAO proveedorDAO;

    @Inject
    protected CompraDetalleFrm compraDetalleFrm;

    private List<Compra> listaCompras;
    private List<Proveedor> listaProveedores;

    private String nombreBean = "page.compra";

    public CompraFrm() {}

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<Compra, Object> getDao() {
        return compraDAO;
    }

    @Override
    protected String getIdAsText(Compra r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Compra getIdByText(String id) {
        if (id != null) {
            try {
                UUID buscado = UUID.fromString(id);
                return this.modelo.getWrappedData().stream()
                        .filter(r -> r.getId().equals(buscado))
                        .findFirst().orElse(null);
            } catch (IllegalArgumentException e) {
                Logger.getLogger(CompraFrm.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    @PostConstruct
    @Override
    public void inicializar() {
        super.inicializar();
        try {
            listaCompras = compraDAO.findRange(0, Integer.MAX_VALUE);
            listaProveedores = proveedorDAO.findRange(0, Integer.MAX_VALUE);
        } catch (Exception ex) {
            Logger.getLogger(CompraFrm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected Compra nuevoRegistro() {
        Compra c = new Compra();
        c.setId(UUID.randomUUID());
        c.setEstado("ACTIVA");
        c.setObservaciones("");
        return c;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return compraDAO;
    }

    @Override
    protected Compra buscarRegistroPorId(Object id) {
        if (id instanceof UUID buscado && this.modelo != null) {
            return this.modelo.getWrappedData().stream()
                    .filter(r -> r.getId().equals(buscado))
                    .findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public void btnGuardarHandler(ActionEvent actionEvent) {
        if (!validarCampos()) return;
        super.btnGuardarHandler(actionEvent);
    }

    @Override
    public void btnModificarHandler(ActionEvent actionEvent) {
        if (!validarCampos()) return;
        super.btnModificarHandler(actionEvent);
    }

    private boolean validarCampos() {
        if (registro.getIdProveedor() == null) {
            enviarMensaje("Debe seleccionar un proveedor.", FacesMessage.SEVERITY_WARN);
            return false;
        }
        if (registro.getEstado() == null || registro.getEstado().trim().isEmpty()) {
            enviarMensaje("Debe seleccionar un estado válido.", FacesMessage.SEVERITY_WARN);
            return false;
        }
        if (registro.getFecha() == null) {
            enviarMensaje("Debe seleccionar una fecha válida.", FacesMessage.SEVERITY_WARN);
            return false;
        }
        return true;
    }

    // -------- GETTERS & SETTERS ----------

    public List<Compra> getListaCompras() {
        return listaCompras;
    }

    public void setListaCompras(List<Compra> listaCompras) {
        this.listaCompras = listaCompras;
    }

    public List<Proveedor> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(List<Proveedor> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    public String getNombreBean() {
        return nombreBean;
    }

    public void setNombreBean(String nombreBean) {
        this.nombreBean = nombreBean;
    }

    public CompraDetalleFrm getCompraDetalleFrm() {
        if (this.registro != null && this.registro.getId() != null) {
            compraDetalleFrm.setIdCompra(this.registro.getId());
        }
        return compraDetalleFrm;
    }

}
