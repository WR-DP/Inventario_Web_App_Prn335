package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.SelectEvent;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.VentaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.VentaDetalleDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.VentaDetalle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class DespachoKardexFrm extends DefaultFrm<Venta> implements Serializable {

    @Inject
    VentaDAO ventaDAO;

    @Inject
    FacesContext facesContext;

    @Inject
    VentaDetalleDAO ventaDetalleDAO;

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<Venta, Object> getDao() {
        return ventaDAO;
    }

    @Override
    protected String getIdAsText(Venta dato) {
        if (dato != null && dato.getId() != null) {
            return dato.getId().toString();
        }
        return "";
    }

    @Override
    protected Venta getIdByText(String id) {
        if (id != null && !id.isBlank() &&
                this.modelo != null &&
                this.modelo.getWrappedData() != null &&
                this.modelo.getWrappedData().size() > 0) {

            try {
                UUID idUUID = UUID.fromString(id);
                return this.modelo.getWrappedData()
                        .stream()
                        .filter(v -> v.getId().equals(idUUID))
                        .findFirst()
                        .orElse(null);

            } catch (IllegalArgumentException e) {
                Logger.getLogger(DespachoKardexFrm.class.getName())
                        .log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return null;
    }


    @Override
    protected Venta nuevoRegistro() {
        Venta venta = new Venta();
        venta.setFecha(new Date());
        venta.setEstado("ACTIVA");
        return venta;
    }


    @Override
    public void seleccionarRegistro(SelectEvent<Venta> event) {
        super.seleccionarRegistro(event);

        try {
            if (this.registro != null && this.registro.getId() != null) {
                List<VentaDetalle> detalles =
                        ventaDetalleDAO.findByIdVenta(this.registro.getId(), 0, Integer.MAX_VALUE);

                this.registro.setDetalles(detalles);
            }
        } catch (Exception ex) {
            Logger.getLogger(DespachoKardexFrm.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
        }

        this.estado = ESTADO_CRUD.MODIFICAR;
    }



    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return ventaDAO;
    }

    @Override
    protected Venta buscarRegistroPorId(Object id) {
        return null; // lo mismo que en tu RecepcionKardexFrm
    }

    public String getNombreBean() {
        return "Despachar Productos (Kardex)";
    }

    @Override
    public void btnNuevoHandler(ActionEvent actionEvent) { return; }

    @Override
    public void btnModificarHandler(ActionEvent actionEvent) { return; }

    @Override
    public void btnEliminarHandler(ActionEvent actionEvent) { return; }

    @Override
    public void btnGuardarHandler(ActionEvent actionEvent) { return; }

    @Override
    public void btnCancelarHandler(ActionEvent actionEvent) {
        try {
            this.estado = ESTADO_CRUD.NADA;
            this.registro = null;
            inicializarRegistros();

        } catch (Exception ex) {
            Logger.getLogger(DespachoKardexFrm.class.getName())
                    .log(Level.SEVERE, ex.getMessage(), ex);
        }
    }


    @Override
    public List<Venta> cargarDatos(int first, int max) {
        return ventaDAO.buscarVentasParaDespacho(first, max);
    }

    @Override
    public int contarDatos() {
        return ventaDAO.contarVentasParaDespacho().intValue();
    }

    public String getRandom() {
        return UUID.randomUUID().toString();
    }

    public void actualizarTabla(ActionEvent actionEvent) {
        System.out.println("Actualizar tabla de despachos");
    }
}
