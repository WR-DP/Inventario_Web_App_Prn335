package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoAlmacenDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoAlmacen;

import java.io.Serializable;

@Path("tipoAlmacen")
public class TipoAlmacenResource extends AbstractResource<TipoAlmacen, Integer> implements Serializable {
    @Inject
    TipoAlmacenDAO tipoAlmacenDAO;
    @Inject
    InventarioDefaultDataAccess<TipoAlmacen, Integer> bean;


    @Override
    protected InventarioDefaultDataAccess<TipoAlmacen, Integer> getBean() {
        return bean;
    }

    @Override
    protected Integer getIdEntity(TipoAlmacen entity) {
        return entity.getId();
    }
}
