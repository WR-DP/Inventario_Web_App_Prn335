package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.*;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
    private ProveedorDAO proveedorDAO;
    @Inject
    private CompraDetalleDAO compraDetalleDAO;
    @Inject
    protected CompraDetalleFrm compraDetalleFrm;

    @Inject
    NotificadorKardex notificadorKardex;

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
                Long buscado = Long.parseLong(id);
                return this.modelo.getWrappedData().stream()
                        .filter(r -> r.getId().equals(buscado))
                        .findFirst().orElse(null);
            } catch (IllegalArgumentException e) {
                Logger.getLogger(CompraFrm.class.getName()).log(Level.SEVERE, e.getMessage(), e);
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
            Logger.getLogger(VentaFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    protected Compra nuevoRegistro() {
        Compra c = new Compra();
        c = new Compra();
        c.setEstado("ACTIVA");
        c.setObservaciones("");
        c.setFecha(new Date());
        return c;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return compraDAO;
    }

    @Override
    protected Compra buscarRegistroPorId(Object id) {
        if (id instanceof Long buscado && this.modelo != null) {
            return this.modelo.getWrappedData().stream()
                    .filter(r -> r.getId().equals(buscado))
                    .findFirst().orElse(null);
        }
        return null;
    }

    public java.math.BigDecimal calcularMontoTotal(Compra compra) {
        if (compra == null || compra.getId() == null) {
            return BigDecimal.ZERO;
        }
        try {
            List<CompraDetalle> detalles = compraDetalleDAO.findByIdCompra(compra.getId(), 0, Integer.MAX_VALUE);
            return compraDetalleDAO.calcularMontoTotal(detalles);
        } catch (Exception ex) {
            Logger.getLogger(CompraFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return BigDecimal.ZERO;
        }
    }

    @Override
    public void btnGuardarHandler(ActionEvent actionEvent) {

        // VALIDACIÓN: no permitir guardar si el proveedor está inactivo
        if (this.registro != null &&
                this.registro.getIdProveedor() != null &&
                Boolean.FALSE.equals(this.registro.getIdProveedor().getActivo())) {

            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Proveedor inactivo",
                            "No se puede crear una compra con un proveedor INACTIVO."));
            return;
        }

        if (!validarCampos()) return;
        try {
            // Asegurar que el proveedor sea una entidad gestionada (evita violaciones de FK al persistir)
            if (this.registro != null && this.registro.getIdProveedor() != null && this.registro.getIdProveedor().getId() != null) {
                try {
                    var prov = proveedorDAO.findById(this.registro.getIdProveedor().getId());
                    if (prov != null) {
                        this.registro.setIdProveedor(prov);
                    }
                } catch (Exception e) {
                    Logger.getLogger(getClass().getName()).log(Level.WARNING, "No se pudo cargar proveedor gestionado antes de guardar", e);
                }
            }

            super.btnGuardarHandler(actionEvent);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            Throwable root = ex;
            while (root.getCause() != null) root = root.getCause();
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Error al crear el registro: " + root.toString(),
                            null));
        }
    }


    @Override
    public void btnModificarHandler(ActionEvent actionEvent) {

        // VALIDACIÓN: no permitir modificar si el proveedor está inactivo
        if (this.registro != null &&
                this.registro.getIdProveedor() != null &&
                Boolean.FALSE.equals(this.registro.getIdProveedor().getActivo())) {

            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Proveedor inactivo",
                            "No se puede modificar esta compra porque el proveedor está INACTIVO."));
            return;
        }

        if (!validarCampos()) return;

        // Asegurar proveedor gestionado al modificar
        if (this.registro != null && this.registro.getIdProveedor() != null && this.registro.getIdProveedor().getId() != null) {
            try {
                var prov = proveedorDAO.findById(this.registro.getIdProveedor().getId());
                if (prov != null) {
                    this.registro.setIdProveedor(prov);
                }
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "No se pudo cargar proveedor gestionado antes de modificar", e);
            }
        }

        super.btnModificarHandler(actionEvent);
    }

    @Override
    public void seleccionarRegistro(SelectEvent<Compra> event) {
        super.seleccionarRegistro(event);
        try {
            if (this.registro != null && this.registro.getId() != null) {
                compraDetalleFrm.setIdCompra(this.registro.getId());
            } else {
                compraDetalleFrm.setIdCompra(null);
            }
            compraDetalleFrm.inicializarRegistros();
        } catch (Exception ex) {
            Logger.getLogger(CompraFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void btnSeleccionarProveedorHandler(ActionEvent event){
        try{
            if (this.registro == null) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No hay compra activa", null));
                return;
            }

            Proveedor sel = this.registro.getIdProveedor();
            if (sel == null) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Seleccione un proveedor", null));
                return;
            }
            if (sel.getId() != null) {
                Proveedor completo = proveedorDAO.findById(sel.getId());
                if (completo != null) {
                    this.registro.setIdProveedor(completo);
                    facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Proveedor asignado", null));
                    return;
                }
            }
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Proveedor asignado (sin carga adicional)", null));
        }catch (Exception ex){
            Logger.getLogger(CompraFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void seleccionarProveedor(final SelectEvent<Proveedor> event) {
        try {
            Proveedor seleccionado = event != null ? event.getObject() : null;
            if (seleccionado == null) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Proveedor no seleccionado", null));
                return;
            }
            if (seleccionado.getId() != null) {
                Proveedor completo = proveedorDAO.findById(seleccionado.getId());
                this.registro.setIdProveedor(completo != null ? completo : seleccionado);
            } else {
                this.registro.setIdProveedor(seleccionado);
            }
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Proveedor seleccionado", null));
        } catch (Exception ex) {
            Logger.getLogger(CompraFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al seleccionar proveedor: " + ex.getMessage(), null));
        }
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

    public List<Proveedor> buscarProveedorPorNombre(final String nombres) {
        try {
            if (nombres != null && !nombres.isBlank()) {
                return proveedorDAO.buscarProveedorPorNombre(nombres, 0, 50);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }


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

    public void notificarCambioKardex(ActionEvent actionEvent) {
        if (this.registro != null && this.registro.getId() != null) {
            this.registro.setEstado("ACTIVA");
            super.btnModificarHandler(actionEvent);
            notificadorKardex.notificarCambioKardex("Cambio en compra ID: ");

        }
    }

}
