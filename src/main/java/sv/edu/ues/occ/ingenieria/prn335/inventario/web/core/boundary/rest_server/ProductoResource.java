package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto;

import java.io.Serializable;
import java.util.UUID;

@Path("producto")
public class ProductoResource implements Serializable {
    @Inject
    ProductoDAO productoDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRange(
            @Min(0)
            @DefaultValue("0")
            @QueryParam("first")
            int first,
            @Max(20)
            @DefaultValue("10")
            @QueryParam("max")
            int max) {
        if (first >= 0 && max <= 100) {
            try {
                int total = productoDAO.count();
                return Response.ok(productoDAO.findRange(first, max)).header("Total-records", total).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "first,max").build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") UUID id) {
        if (id != null) {
            try {
                Producto resp = productoDAO.findById(id);
                if (resp != null) {
                    return Response.ok(resp).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Record with id " + id + " not found").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "id").build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") UUID id) {
        if (id != null) {
            try {
                Producto resp = productoDAO.findById(id);
                if (resp != null) {
                    productoDAO.delete(resp);
                    return Response.noContent().build();
                }
                return Response.status(Response.Status.NOT_FOUND).header("Not-Found", "Record with id " + id + " not found").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot acces db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "id").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Producto entity, @Context UriInfo uriInfo) {
        if (entity == null) {
            return Response.status(422)
                    .header("Missing-parameter", "entity must not be null")
                    .build();
        }
        try {
            // si el id viene null, el PrePersist lo genera
            productoDAO.create(entity);
            return Response.created(uriInfo.getAbsolutePathBuilder()
                    .path(entity.getId().toString()).build()).entity(entity).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header("Server-exception", e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") UUID id, Producto entity) {
        if (id == null || entity == null) {
            return Response.status(422).header("Missing-parameter", "id and entity must not be null").build();
        }
        try {
            Producto existing = productoDAO.findById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Record with id " + id + " not found").build();
            }
            // asegurar que la entidad tenga el mismo id de la ruta
            entity.setId(id);
            Producto updated = productoDAO.update(entity);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", e.getMessage()).build();
        }
    }
}
