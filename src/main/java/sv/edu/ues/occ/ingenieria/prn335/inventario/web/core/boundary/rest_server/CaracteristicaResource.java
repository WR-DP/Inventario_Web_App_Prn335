package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Caracteristica;

import java.io.Serializable;

@Path("caracteristica")
public class CaracteristicaResource extends AbstractResource<Caracteristica, Integer> implements Serializable {
    @Inject
    CaracteristicaDAO caracteristicaDAO;
    @Inject
    InventarioDefaultDataAccess<Caracteristica, Integer> bean;


    @Override
    protected InventarioDefaultDataAccess<Caracteristica, Integer> getBean() {
        return bean;
    }

    @Override
    protected Integer getIdEntity(Caracteristica entity) {
        return entity.getId();
    }
}
