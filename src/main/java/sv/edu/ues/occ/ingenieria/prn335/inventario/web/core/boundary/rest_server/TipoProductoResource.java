package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;

import java.io.Serial;
import java.io.Serializable;


@Path("tipo_producto")
public class TipoProductoResource extends AbstractResource<TipoProducto, Long> implements Serializable {
    @Inject
    TipoProductoDAO tipoProductoDAO;
    @Inject
    InventarioDefaultDataAccess<TipoProducto, Long> bean;

    @Override
    protected InventarioDefaultDataAccess<TipoProducto, Long> getBean() {
        return bean;
    }

    @Override
    protected Long getIdEntity(TipoProducto entity) {
        return entity.getId();
    }
}