package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoDAO;

import java.io.Serializable;

@Path("producto")
public class ProductoResource implements Serializable {
    @Inject
    ProductoDAO productoDAO;


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
