package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoTipoProductoCaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.ProductoTipoProductoCaracteristica;

import java.io.Serializable;
import java.util.UUID;


@Path("producto/{idTipoProducto}/tipoProducto/caracteristica")
public class ProductoTipoProductoCaracteristicaResource extends AbstractResource<ProductoTipoProductoCaracteristica, UUID> implements Serializable {
    @Inject
    ProductoTipoProductoCaracteristicaDAO productoTipoProductoCaracteristicaDAO;
    @Inject
    InventarioDefaultDataAccess<ProductoTipoProductoCaracteristica, UUID> bean;

    @Override
    protected InventarioDefaultDataAccess<ProductoTipoProductoCaracteristica, UUID> getBean() {
        return bean;
    }

    @Override
    protected UUID getIdEntity(ProductoTipoProductoCaracteristica entity) {
        return entity.getId();
    }
}
