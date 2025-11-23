package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.enterprise.context.Dependent;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CompraDetalleDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle;
import java.math.BigDecimal;

import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

@Dependent
@Named
public class CompraDetalleFrm extends DefaultFrm<CompraDetalle> implements Serializable {
    @Inject
    FacesContext facesContext;
    @Inject
    CompraDetalleDAO compraDetalleDAO;
    @Inject
    ProductoDAO productoDAO;

    protected Long idCompra;

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<CompraDetalle, Object> getDao() {
        return compraDetalleDAO;
    }

    @Override
    protected String getIdAsText(CompraDetalle r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected CompraDetalle getIdByText(String id) {
        if (id != null && !id.isBlank() && this.modelo.getWrappedData() != null && !this.modelo.getWrappedData().isEmpty()) {
            try {
                UUID buscado = UUID.fromString(id);
                return this.modelo.getWrappedData().stream().filter(r -> r.getId().equals(buscado)).findFirst().orElse(null);
            } catch (IllegalArgumentException e) {
                Logger.getLogger(CompraDetalleFrm.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    protected CompraDetalle nuevoRegistro() {
        CompraDetalle compraDetalle = new CompraDetalle();
        compraDetalle.setId(UUID.randomUUID());
        compraDetalle.setCantidad(BigDecimal.valueOf(0));
        compraDetalle.setPrecio(BigDecimal.valueOf(0));
        compraDetalle.setEstado("ACTIVA");
        compraDetalle.setObservaciones("");
        return compraDetalle;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return compraDetalleDAO;
    }

    @Override
    protected CompraDetalle buscarRegistroPorId(Object id) {
        if (id instanceof UUID buscado && this.modelo != null) {
            return this.modelo.getWrappedData().stream().filter(r -> r.getId().equals(buscado)).findFirst().orElse(null);
        }
        return null;
    }

    public List<sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto> buscarProductosPorNombre(final String nombreProducto){
        try{
            if(nombreProducto!=null && !nombreProducto.isBlank()){
                return productoDAO.buscarProductosPorNombre(nombreProducto, 0, Integer.MAX_VALUE);
            }
        } catch (Exception ex) {
            Logger.getLogger(CompraDetalleFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public void btnSeleccionarProductoHandler(ActionEvent event){
        try {
            Object sel = event.getComponent().getAttributes().get("selectedProducto");
            if (!(sel instanceof sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto)) {
                return;
            }
            sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto seleccionado =
                    (sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto) sel;

            if (this.registro == null) {
                this.registro = nuevoRegistro();
            }

            this.registro.setIdProducto(seleccionado);

            if (this.registro.getCantidad() == null) {
                this.registro.setCantidad(java.math.BigDecimal.ONE);
            }

            facesContext.addMessage(null,
                    new jakarta.faces.application.FacesMessage(jakarta.faces.application.FacesMessage.SEVERITY_INFO,
                            "Producto seleccionado",
                            seleccionado.getNombreProducto()));
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CompraDetalleFrm.class.getName()).log(java.util.logging.Level.SEVERE, ex.getMessage(), ex);
            facesContext.addMessage(null,
                    new jakarta.faces.application.FacesMessage(jakarta.faces.application.FacesMessage.SEVERITY_ERROR,
                            "Error al seleccionar producto",
                            ex.getMessage()));
        }
    }

    public void seleccionarProducto(SelectEvent<Producto> event) {
        try {
            sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto seleccionado = (event != null) ? event.getObject() : null;
            if (seleccionado == null) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se seleccionó producto", null));
                return;
            }
            if (this.registro == null) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No hay compra activa", null));
                return;
            }
            this.registro.setIdProducto(seleccionado);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Producto seleccionado", seleccionado.getNombreProducto()));
        } catch (Exception ex) {
            Logger.getLogger(CompraDetalleFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al seleccionar producto", ex.getMessage()));
        }
    }

    public void validarCantidad(jakarta.faces.context.FacesContext context, jakarta.faces.component.UIComponent component, Object value) {
        if (value == null) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cantidad inválida", "La cantidad es obligatoria"));
        }
        if (!(value instanceof BigDecimal)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cantidad inválida", "Tipo de dato no válido"));
        }
        BigDecimal bd = (BigDecimal) value;
        if (bd.compareTo(BigDecimal.ONE) < 0) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cantidad inválida", "La cantidad debe ser un entero positivo"));
        }
        if (bd.stripTrailingZeros().scale() > 0) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cantidad inválida", "La cantidad debe ser un número entero"));
        }
    }

    public void validarPrecio(jakarta.faces.context.FacesContext context, jakarta.faces.component.UIComponent component, Object value) {
        if (value == null) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Precio inválido", "El precio es obligatorio"));
        }
        if (!(value instanceof BigDecimal)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Precio inválido", "Tipo de dato no válido"));
        }
        BigDecimal bd = (BigDecimal) value;
        if (bd.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Precio inválido", "El precio debe ser positivo"));
        }
        // Limitar a 2 decimales
        if (bd.scale() > 2) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Precio inválido", "El precio debe tener como máximo 2 decimales"));
        }
    }

    @Override
    public List<sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle> cargarDatos(int first, int max) {
        try {
            if (first >= 0 && max > 0 && this.idCompra != null) {
                // llamar al DAO que devuelve solo los detalles de la compra
                return compraDetalleDAO.findByIdCompra(this.idCompra, first, max);
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, ex.getMessage(), ex);
        }
        return java.util.List.of();
    }

    @Override
    public int contarDatos() {
        try {
            if (this.idCompra != null) {
                // usar el método de conteo del DAO
                return this.compraDetalleDAO.countByIdCompra(this.idCompra);
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, ex.getMessage(), ex);
        }
        return 0;
    }

    @Override
    public void btnGuardarHandler(ActionEvent actionEvent) {
        try {
            if (this.idCompra != null) {
                if (this.registro == null) {
                    this.registro = nuevoRegistro();
                }
                if (this.registro.getIdCompra() == null) {
                    Compra c = new Compra();
                    c.setId(this.idCompra);
                    this.registro.setIdCompra(c);
                } else if (this.registro.getIdCompra().getId() == null) {
                    this.registro.getIdCompra().setId(this.idCompra);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CompraDetalleFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        super.btnGuardarHandler(actionEvent);
    }

    @Override
    public void btnModificarHandler(ActionEvent actionEvent) {
        try {
            if (this.idCompra != null && this.registro != null) {
                if (this.registro.getIdCompra() == null) {
                    Compra c = new Compra();
                    c.setId(this.idCompra);
                    this.registro.setIdCompra(c);
                } else if (this.registro.getIdCompra().getId() == null) {
                    this.registro.getIdCompra().setId(this.idCompra);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CompraDetalleFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        super.btnModificarHandler(actionEvent);
    }

    public Long getIdCompra() {
        return idCompra;
    }

    protected void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    protected String nombreBean = "page.coompraDetalle";

    @Override
    public String getNombreBean() {
        return this.nombreBean;
    }
}
