package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.VentaDetalleDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.VentaDetalle;

import java.io.Serializable;
import java.util.UUID;

@Path("venta/{idVenta}/detalle")
public class VentaDetalleResource extends AbstractResource<VentaDetalle, UUID> implements Serializable {
    @Inject
    VentaDetalleDAO ventaDetalleDAO;
    @Inject
    InventarioDefaultDataAccess<VentaDetalle, UUID> bean;

    @Override
    protected InventarioDefaultDataAccess<VentaDetalle, UUID> getBean() {
        return null;
    }

    @Override
    protected UUID getIdEntity(VentaDetalle entity) {
        return null;
    }
}
