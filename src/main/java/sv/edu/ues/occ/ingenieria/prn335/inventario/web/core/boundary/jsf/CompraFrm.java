package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CompraDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class CompraFrm extends DefaultFrm<Compra> implements Serializable {

    @Inject
    private FacesContext facesContext;

    @Inject
    private CompraDAO compraDAO;

    @Inject
    private ProveedorFrm proveedorFrm;

    private List<Proveedor> proveedoresActivos;
    private Integer idProveedorSeleccionado;

    @PostConstruct
    public void inicializar() {
        super.inicializar();
        proveedoresActivos = proveedorFrm.getListaProveedores().stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .toList();
    }

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDefaultDataAccess getDao() {
        return compraDAO;
    }

    @Override
    protected String getIdAsText(Compra r) {
        return "";
    }

    @Override
    protected Compra getIdByText(String id) {
        return null;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return compraDAO;
    }

    @Override
    protected Compra buscarRegistroPorId(Object id) {
        return null;
    }

    @Override
    protected Compra nuevoRegistro() {
        Compra c = new Compra();
        c.setFecha(OffsetDateTime.now());
        c.setEstado("CREADA");
        c.setObservaciones("");
        return c;
    }

    public void btnGuardarHandler(ActionEvent actionEvent) {

        if (registro == null) {
            enviarMensaje("No hay datos para guardar", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (idProveedorSeleccionado == null) {
            enviarMensaje("Debe seleccionar un proveedor", FacesMessage.SEVERITY_ERROR);
            return;
        }

        // VALIDACIÓN — SOLO UNA COMPRA POR PROVEEDOR
        if (compraDAO.existeCompraDeProveedor(idProveedorSeleccionado.intValue())) {
            enviarMensaje("Ya existe una compra registrada para este proveedor", FacesMessage.SEVERITY_ERROR);
            return;
        }

        Proveedor p = proveedoresActivos.stream()
                .filter(pr -> pr.getId().equals(idProveedorSeleccionado))
                .findFirst()
                .orElse(null);

        if (p == null) {
            enviarMensaje("Proveedor no encontrado", FacesMessage.SEVERITY_ERROR);
            return;
        }

        registro.setProveedor(p);

        try {
            compraDAO.create(registro);
            enviarMensaje("Compra registrada correctamente", FacesMessage.SEVERITY_INFO);
            this.estado = ESTADO_CRUD.NADA;
            this.registro = null;
            this.idProveedorSeleccionado = null;
            inicializarRegistros();
        } catch (Exception ex) {
            enviarMensaje("Error al crear la compra: " + ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }
    public List<Proveedor> getProveedoresActivos() {
        return proveedoresActivos;
    }

    public Integer getIdProveedorSeleccionado() { return idProveedorSeleccionado; }
    public void setIdProveedorSeleccionado(Integer idProveedorSeleccionado) {
        this.idProveedorSeleccionado = idProveedorSeleccionado;
    }

    @Override
    public void enviarMensaje(String mensaje, FacesMessage.Severity severidad) {
        facesContext.addMessage(null, new FacesMessage(severidad, mensaje, null));
    }
}
