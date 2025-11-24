package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProveedorDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

@Path("proveedor")
public class ProveedorResource extends AbstractResource<Proveedor, Integer> {
    @Inject
    ProveedorDAO proveedorDAO;
    @Inject
    InventarioDefaultDataAccess<Proveedor, Integer> bean;

    @Override
    protected InventarioDefaultDataAccess<Proveedor, Integer> getBean() {
        return bean;
    }

    @Override
    protected Integer getIdEntity(Proveedor entity) {
        return entity.getId();
    }
}
