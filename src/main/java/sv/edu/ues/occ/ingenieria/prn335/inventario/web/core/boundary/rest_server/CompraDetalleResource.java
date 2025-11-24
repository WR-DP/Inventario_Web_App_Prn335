package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CompraDetalleDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle;

import java.io.Serializable;
import java.util.UUID;

@Path("compra/{idCompra}/compraDetalle")
public class CompraDetalleResource extends AbstractResource<CompraDetalle, UUID> implements Serializable {
    @Inject
    CompraDetalleDAO compraDetalleDAO;
    @Inject
    InventarioDefaultDataAccess<CompraDetalle, UUID> bean;


    @Override
    protected InventarioDefaultDataAccess<CompraDetalle, UUID> getBean() {
        return bean;
    }

    @Override
    protected UUID getIdEntity(CompraDetalle entity) {
        return entity.getId();
    }
}
