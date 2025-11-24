package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoTipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.ProductoTipoProducto;

import java.io.Serializable;
import java.util.UUID;

@Path("producto/{idTipoProducto}/tipoProducto")
public class ProductoTipoProductoResource extends  AbstractResource<ProductoTipoProducto, UUID> implements Serializable{
    @Inject
    ProductoTipoProductoDAO productoTipoProductoDAO;
    @Inject
    InventarioDefaultDataAccess<ProductoTipoProducto, UUID> bean;

    @Override
    protected InventarioDefaultDataAccess<ProductoTipoProducto, UUID> getBean() {
        return bean;
    }

    @Override
    protected UUID getIdEntity(ProductoTipoProducto entity) {
        return entity.getId();
    }
}
