package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CompraDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CompraDetalleDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.*;

import java.io.Serializable;
import java.util.UUID;

@Path("compra/{idCompra}/compraDetalle")
public class CompraDetalleResource  implements Serializable {
    @Inject
    CompraDetalleDAO compraDetalleDAO;

    @Inject
    CompraDAO compraDAO;

    @Inject
    ProductoDAO productoDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRange(
            @Min(0)
            @DefaultValue("0")
            @QueryParam("first")
            int first,
            @Max(100)
            @DefaultValue("100")
            @QueryParam("max")
            int max,
            @PathParam("idCompra")
            Long idCompra)
    {
        if (first >= 0 && max <= 100) {
            try {
                int total = compraDetalleDAO.count();
                return Response.ok(compraDetalleDAO.findRange(first, max)).header("Total-records", total).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "first,max").build();
    }


    @GET
    @Path("{idCompra}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("idCompra") UUID idCompra) {
        if (idCompra != null) {
            try {
                CompraDetalle resp = compraDetalleDAO.findById(idCompra);
                if (resp != null) {
                    return Response.ok(resp).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Record with id "+idCompra+" not found").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "id").build();
    }

    @DELETE
    @Path("{idCompra}")
    public Response delete(@PathParam("idCompra") UUID idCompra) {
        if (idCompra != null) {
            try {
                CompraDetalle resp = compraDetalleDAO.findById(idCompra);
                if (resp != null) {
                    compraDetalleDAO.delete(resp);
                    return Response.noContent().build();
                }
                return Response.status(Response.Status.NOT_FOUND).header("Not-Found", "Record with id " + idCompra + " not found").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot acces db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "id").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(
            CompraDetalle entity,
            @PathParam("idCompra") Long idCompra,
            @Context UriInfo uriInfo) {

        if (entity == null) {
            return Response.status(422)
                    .header("Missing-parameter", "Body cannot be null")
                    .build();
        }

        try {
            Compra compra = compraDAO.findById(idCompra);
            if (compra == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "Compra with id " + idCompra + " not found")
                        .build();
            }

            if (entity.getIdProducto() == null || entity.getIdProducto().getId() == null) {
                return Response.status(422)
                        .header("Missing-parameter", "entity.idProducto.id is required")
                        .build();
            }

            Producto producto = productoDAO.findById(entity.getIdProducto().getId());
            if (producto == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "Producto with id " + entity.getIdProducto().getId() + " not found")
                        .build();
            }

            entity.setId(UUID.randomUUID());
            entity.setIdCompra(compra);
            entity.setIdProducto(producto);
            compraDetalleDAO.create(entity);

            return Response.created(
                            uriInfo.getAbsolutePathBuilder()
                                    .path(entity.getId().toString())
                                    .build()
                    )
                    .entity(entity)
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header("Server-exception", e.getMessage())
                    .build();
        }
    }

    //update

    @PUT
    @Path("{idCompra}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("idCompra") java.util.UUID idCompra, CompraDetalle entity) {
        if (idCompra == null || entity == null) {
            return Response.status(422).header("Missing-parameter", "id and entity must not be null").build();
        }
        try {
            CompraDetalle existing = compraDetalleDAO.findById(idCompra);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Record with id " + idCompra + " not found").build();
            }

            if (entity.getIdProducto() == null || entity.getIdProducto().getId() == null) {
                return Response.status(422).header("Missing-parameter", "entity.idProducto.id is required").build();
            }

            Producto producto = productoDAO.findById(entity.getIdProducto().getId());
            if (producto == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "Producto with id " + entity.getIdProducto().getId() + " not found")
                        .build();
            }

            // Preserve parent Compra association
            entity.setIdCompra(existing.getIdCompra());
            entity.setIdProducto(producto);
            entity.setId(idCompra);

            CompraDetalle updated = compraDetalleDAO.update(entity);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", e.getMessage()).build();
        }
    }

}
