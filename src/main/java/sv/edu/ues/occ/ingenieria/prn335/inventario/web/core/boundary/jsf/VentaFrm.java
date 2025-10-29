package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ClienteDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.VentaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class VentaFrm extends DefaultFrm<Venta> implements Serializable {

    @Inject
    FacesContext facesContext;

    @Inject
    VentaDAO ventaDAO;

    @Inject
    ClienteDAO clienteDAO;

    private List<Venta> listaVentas;
    private List<Cliente> listaClientes;

    private String nombreBean = "page.venta";

    public VentaFrm() {}

    @PostConstruct
    @Override
    public void inicializar() {
        super.inicializar();
        try {
            listaVentas = ventaDAO.findRange(0, Integer.MAX_VALUE);
            listaClientes = clienteDAO.findRange(0, Integer.MAX_VALUE);
        } catch (Exception ex) {
            Logger.getLogger(VentaFrm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<Venta, Object> getDao() {
        return ventaDAO;
    }

    @Override
    protected String getIdAsText(Venta r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Venta getIdByText(String id) {
        if (id != null) {
            try {
                UUID buscado = UUID.fromString(id);
                return this.modelo.getWrappedData().stream()
                        .filter(r -> r.getId().equals(buscado))
                        .findFirst()
                        .orElse(null);
            } catch (IllegalArgumentException e) {
                Logger.getLogger(VentaFrm.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    @Override
    protected Venta nuevoRegistro() {
        Venta v = new Venta();
        v.setId(UUID.randomUUID());
        v.setFecha(OffsetDateTime.now());
        v.setEstado("ACTIVA");
        v.setObservaciones("");
        return v;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return ventaDAO;
    }

    @Override
    protected Venta buscarRegistroPorId(Object id) {
        if (id instanceof UUID buscado && this.modelo != null) {
            return this.modelo.getWrappedData().stream()
                    .filter(r -> r.getId().equals(buscado))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    // ✅ Validaciones antes de guardar o modificar
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
        if (registro.getIdCliente() == null) {
            enviarMensaje("Debe seleccionar un cliente.", FacesMessage.SEVERITY_WARN);
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

    // Getters y Setters
    public List<Venta> getListaVentas() {
        return listaVentas;
    }

    public void setListaVentas(List<Venta> listaVentas) {
        this.listaVentas = listaVentas;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public String getNombreBean() {
        return nombreBean;
    }

    public void setNombreBean(String nombreBean) {
        this.nombreBean = nombreBean;
    }
}