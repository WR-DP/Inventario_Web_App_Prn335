package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CompraDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;

import java.io.Serializable;

@Path("compra")
public class CompraResource extends AbstractResource<Compra, Long> implements Serializable {
    @Inject
    CompraDAO compraDAO;
    @Inject
    InventarioDefaultDataAccess<Compra, Long> bean;

    @Override
    protected InventarioDefaultDataAccess<Compra, Long> getBean() {
        return bean;
    }

    @Override
    protected Long getIdEntity(Compra entity) {
        return entity.getId();
    }
}
