package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoCaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProductoCaracteristica;

import java.io.Serializable;

@Path("tipoProducto/{idTipoProducto}/caracteristica")
public class TipoProductoCaracteristicaResource extends AbstractResource<TipoProductoCaracteristica, Long> implements Serializable {
    @Inject
    TipoProductoCaracteristicaDAO tipoProductoCaracteristicaDAO;
    @Inject
    InventarioDefaultDataAccess<TipoProductoCaracteristica, Long> bean;

    @Override
    protected InventarioDefaultDataAccess<TipoProductoCaracteristica, Long> getBean() {
        return bean;
    }

    @Override
    protected Long getIdEntity(TipoProductoCaracteristica entity) {
        return entity.getId();
    }
}
