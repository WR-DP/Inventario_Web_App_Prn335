package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.enterprise.context.Dependent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.VentaDetalleDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.VentaDetalle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
@Named
public class VentaDetalleFrm extends DefaultFrm<VentaDetalle> implements Serializable {

    @Inject
    FacesContext facesContext;
    @Inject
    VentaDetalleDAO ventaDetalleDAO;


    protected UUID idVenta;

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<VentaDetalle, Object> getDao() {
        return ventaDetalleDAO;
    }

    @Override
    protected String getIdAsText(VentaDetalle r) {
        if(r != null && r.getId() != null){
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected VentaDetalle getIdByText(String id) {
        if(id != null && !id.isBlank() && this.modelo.getWrappedData() != null && !this.modelo.getWrappedData().isEmpty()){
            try{
                UUID buscado = UUID.fromString(id);
                return this.modelo.getWrappedData().stream().filter(r -> r.getId().equals(buscado)).findFirst().orElse(null);
            }catch(IllegalArgumentException e){
                Logger.getLogger(VentaDetalleFrm.class.getName()).log(Level.SEVERE,e.getMessage(),e);
            }
        }
        return null;
    }

    @Override
    protected VentaDetalle nuevoRegistro() {
        VentaDetalle ventaDetalle = new VentaDetalle();
        ventaDetalle.setId(UUID.randomUUID());
        ventaDetalle.setCantidad(BigDecimal.valueOf(0));
        ventaDetalle.setPrecio(BigDecimal.valueOf(0.0));
        ventaDetalle.setEstado("ACTIVA");
        ventaDetalle.setObservaciones("");
        return ventaDetalle;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return ventaDetalleDAO;
    }

    @Override
    protected VentaDetalle buscarRegistroPorId(Object id) {
        if(id instanceof UUID buscado && this.modelo != null){
            return this.modelo.getWrappedData().stream().filter(r -> r.getId().equals(buscado)).findFirst().orElse(null);
        }
        return null;
    }



    public UUID getIdVenta() {return idVenta;}

    protected void setIdVenta(UUID idVenta) {
        this.idVenta = idVenta;
    }

    protected String nombreBean="page.ventaDetalle";
    @Override
    public String getNombreBean() {return this.nombreBean;}

}