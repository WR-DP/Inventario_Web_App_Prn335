package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoUnidadMedidaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoUnidadMedida;

import java.io.Serializable;

@Path("TipoUnidadMedida")
public class TipoUnidadMedidaResource  implements Serializable {
    @Inject
    TipoUnidadMedidaDAO tipoUnidadMedidaDAO;



}
