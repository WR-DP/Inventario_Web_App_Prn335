package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoAlmacenDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoAlmacen;

import java.io.Serializable;

@Path("tipoAlmacen")
public class TipoAlmacenResource implements Serializable{
    @Inject
    TipoAlmacenDAO tipoAlmacenDAO;

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
                int total = tipoAlmacenDAO.count();
                return Response.ok(tipoAlmacenDAO.findRange(first, max)).header("Total-records", total).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "first,max").build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Integer id) {
        if (id != null) {
            try {
                TipoAlmacen resp = tipoAlmacenDAO.findById(id);
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
    public Response delete(@PathParam("id") Integer id) {
        if (id != null) {
            try {
                TipoAlmacen resp = tipoAlmacenDAO.findById(id);
                if (resp != null) {
                    tipoAlmacenDAO.delete(resp);
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
    public Response create(TipoAlmacen entity, @Context UriInfo uriInfo) {
        if (entity == null) {
            return Response.status(422)
                    .header("Missing-parameter", "entity must not be null")
                    .build();
        }

        try {
            // si el id viene null, el PrePersist lo genera
            tipoAlmacenDAO.create(entity);

            return Response.created(uriInfo.getAbsolutePathBuilder()
                    .path(entity.getId().toString()).build()).entity(entity).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header("Server-exception", e.getMessage())
                    .build();
        }
    }
}
