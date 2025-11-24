package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.VentaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;

import java.io.Serializable;
import java.util.UUID;

@Path("venta")
public class VentaResource extends AbstractResource<Venta, UUID> implements Serializable {
    @Inject
    VentaDAO ventaDAO;
    @Inject
    InventarioDefaultDataAccess<Venta, UUID> bean;


    @Override
    protected InventarioDefaultDataAccess<Venta, UUID> getBean() {
        return bean;
    }

    @Override
    protected UUID getIdEntity(Venta entity) {
        return entity.getId();
    }
}
