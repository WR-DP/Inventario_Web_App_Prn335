package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.UnidadMedidaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.UnidadMedida;

import java.io.Serializable;

@Path("unidadMedida")
public class UnidadMedidaResource extends AbstractResource<UnidadMedida, Integer> implements Serializable {
    @Inject
    UnidadMedidaDAO unidadMedidaDAO;
    @Inject
    InventarioDefaultDataAccess<UnidadMedida, Integer> bean;

    @Override
    protected InventarioDefaultDataAccess<UnidadMedida, Integer> getBean() {
        return bean;
    }

    @Override
    protected Integer getIdEntity(UnidadMedida entity) {
        return entity.getId();
    }
}
