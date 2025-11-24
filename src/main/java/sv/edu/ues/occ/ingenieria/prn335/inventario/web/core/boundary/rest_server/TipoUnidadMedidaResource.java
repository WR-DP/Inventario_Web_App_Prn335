package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoUnidadMedidaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoUnidadMedida;

import java.io.Serializable;

@Path("TipoUnidadMedida")
public class TipoUnidadMedidaResource extends AbstractResource<TipoUnidadMedida, Integer> implements Serializable {
    @Inject
    TipoUnidadMedidaDAO tipoUnidadMedidaDAO;
    @Inject
    InventarioDefaultDataAccess<TipoUnidadMedida, Integer> bean;


    @Override
    protected InventarioDefaultDataAccess<TipoUnidadMedida, Integer> getBean() {
        return bean;
    }

    @Override
    protected Integer getIdEntity(TipoUnidadMedida entity) {
        return entity.getId();
    }
}
