package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ClienteDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;

import java.io.Serializable;
import java.util.UUID;

@Path("cliente")
public class ClienteResource extends AbstractResource<Cliente, UUID> implements Serializable {
    @Inject
    ClienteDAO clienteDAO;
    @Inject
    InventarioDefaultDataAccess<Cliente, UUID> bean;

    @Override
    protected InventarioDefaultDataAccess<Cliente, UUID> getBean() {
        return bean;
    }

    @Override
    protected UUID getIdEntity(Cliente entity) {
        return entity.getId();
    }
}
