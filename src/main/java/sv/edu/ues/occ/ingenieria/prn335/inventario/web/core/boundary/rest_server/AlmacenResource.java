package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.AlmacenDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Almacen;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoAlmacen;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;

import java.io.Serializable;

@Path("almacen")
public class AlmacenResource  implements Serializable {
    @Inject
    AlmacenDAO almacenDAO;

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
                int total = almacenDAO.count();
                return Response.ok(almacenDAO.findRange(first, max)).header("Total-records", total).build();
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
                Almacen resp = almacenDAO.findById(id);
                if (resp != null) {
                    return Response.ok(resp).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Record with id "+id+" not found").build();
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
                Almacen resp = almacenDAO.findById(id);
                if (resp != null) {
                    almacenDAO.delete(resp);
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
    public Response create(Almacen entity, @Context UriInfo uriInfo) {

        if (entity == null || entity.getId() != null) {
            return Response.status(422)
                    .header("Missing-parameter", "entity must not be null and entity.id must be null")
                    .build();
        }

        try {
            // Validar que venga un tipo de almacén
            if (entity.getIdTipoAlmacen() == null || entity.getIdTipoAlmacen().getId() == null) {
                return Response.status(422)
                        .header("Missing-parameter", "idTipoAlmacen.id is required")
                        .build();
            }

            // Buscar el tipo en la BD
            TipoAlmacen tipo = almacenDAO.findTipoAlmacenById(entity.getIdTipoAlmacen().getId());

            if (tipo == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "TipoAlmacen with id " + entity.getIdTipoAlmacen().getId() + " not found")
                        .build();
            }

            // Asociar tipo válido al almacén
            entity.setIdTipoAlmacen(tipo);

            // Persistir
            almacenDAO.create(entity);

            // Retornar Location + JSON
            return Response.created(uriInfo.getAbsolutePathBuilder()
                    .path(String.valueOf(entity.getId())).build()).entity(entity).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header("Server-exception", "Cannot access db")
                    .build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer id, Almacen entity) {
        if (id == null || entity == null) {
            return Response.status(422).header("Missing-parameter", "id and entity must not be null").build();
        }
        try {
            Almacen existing = almacenDAO.findById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Record with id " + id + " not found").build();
            }

            if (entity.getIdTipoAlmacen() == null || entity.getIdTipoAlmacen().getId() == null) {
                return Response.status(422).header("Missing-parameter", "idTipoAlmacen.id is required").build();
            }

            TipoAlmacen tipo = almacenDAO.findTipoAlmacenById(entity.getIdTipoAlmacen().getId());
            if (tipo == null) {
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "TipoAlmacen with id " + entity.getIdTipoAlmacen().getId() + " not found").build();
            }

            entity.setIdTipoAlmacen(tipo);
            entity.setId(id);
            Almacen updated = almacenDAO.update(entity);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", e.getMessage()).build();
        }
    }

}
