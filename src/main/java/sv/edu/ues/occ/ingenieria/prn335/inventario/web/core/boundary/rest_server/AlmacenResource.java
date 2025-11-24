package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.AlmacenDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Almacen;

import java.io.Serializable;

@Path("almacen")
public class AlmacenResource extends AbstractResource<Almacen, Integer> implements Serializable {
    @Inject
    AlmacenDAO almacenDAO;

    @Inject
    InventarioDefaultDataAccess<Almacen, Integer> bean;


    @Override
    protected InventarioDefaultDataAccess<Almacen, Integer> getBean() {
        return bean;
    }

    @Override
    protected Integer getIdEntity(Almacen entity) {
        return entity.getId();
    }


}
