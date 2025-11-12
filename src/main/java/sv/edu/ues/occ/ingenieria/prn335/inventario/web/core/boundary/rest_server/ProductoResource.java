package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto;

import java.io.Serializable;
import java.util.UUID;

@Path("producto")
public class ProductoResource extends AbstractResource<Producto, Object> implements Serializable {
    @Inject
    ProductoDAO productoDAO;

    @Override
    public InventarioDefaultDataAccess<Producto, Object> getBean(){ return this.productoDAO;}

    @Override
    public UUID getIdEntity(Producto entity) {
        return entity.getId();
    }

    //Metodo propio
//    @GET
//    @Path("/{...}/producto")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response findBy...@PathParam("idProducto") UUID id) {
//        if (id != null) {
//            try {
//
//            } catch (Exception e) {
//                return Response.status(500).header("Server-Exception", e.getMessage()).build();
//            }
//
//        }
//        return Response.status(422).header("Missing-parameter", "id producto must not be null and entity.id be null").build();
//    }

}
