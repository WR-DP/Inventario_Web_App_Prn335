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
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Caracteristica;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoUnidadMedida;

import java.io.Serializable;

@Path("compra")
public class CompraResource  implements Serializable {
    @Inject
    CompraDAO compraDAO;

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
            int max) {
        if (first >= 0 && max <= 100) {
            try {
                int total = compraDAO.count();
                return Response.ok(compraDAO.findRange(first, max)).header("Total-records", total).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "first,max").build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        if (id != null) {
            try {
                Compra resp = compraDAO.findById(id);
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
    public Response delete(@PathParam("id") Long id) {
        if (id != null) {
            try {
                Compra resp = compraDAO.findById(id);
                if (resp != null) {
                    compraDAO.delete(resp);
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
    public Response create(Compra entity, @Context UriInfo uriInfo) {

        // Validación básica
        if (entity == null || entity.getId() != null) {
            return Response.status(422)
                    .header("Missing-parameter", "entity must not be null and entity.id must be null")
                    .build();
        }

        try {
            // Validación de dependencia: Proveedor
            if (entity.getIdProveedor() == null ||
                    entity.getIdProveedor().getId() == null) {

                return Response.status(422)
                        .header("Missing-parameter", "idTipoUnidadMedida.id is required")
                        .build();
            }

            // Buscar el proveedor real en DB
            Proveedor tipo = compraDAO.findProveedorById(entity.getIdProveedor().getId());

            if (tipo == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "TipoUnidadMedida with id "
                                + entity.getIdProveedor().getId() + " not found")
                        .build();
            }

            // Asignar la versión administrada por JPA
            entity.setIdProveedor(tipo);

            // Persistir
            compraDAO.create(entity);

            // Devolver Location + JSON
            return Response.created(
                            uriInfo.getAbsolutePathBuilder()
                                    .path(String.valueOf(entity.getId()))
                                    .build()
                    )
                    .entity(entity)
                    .build();

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
    public Response update(@PathParam("id") Long id, Compra entity) {
        if (id == null || entity == null) {
            return Response.status(422).header("Missing-parameter", "id and entity must not be null").build();
        }
        try {
            Compra existing = compraDAO.findById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Record with id " + id + " not found").build();
            }

            if (entity.getIdProveedor() == null || entity.getIdProveedor().getId() == null) {
                return Response.status(422).header("Missing-parameter", "idProveedor.id is required").build();
            }

            Proveedor proveedor = compraDAO.findProveedorById(entity.getIdProveedor().getId());
            if (proveedor == null) {
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Proveedor with id " + entity.getIdProveedor().getId() + " not found").build();
            }

            entity.setIdProveedor(proveedor);
            entity.setId(id);
            Compra updated = compraDAO.update(entity);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", e.getMessage()).build();
        }
    }


}
