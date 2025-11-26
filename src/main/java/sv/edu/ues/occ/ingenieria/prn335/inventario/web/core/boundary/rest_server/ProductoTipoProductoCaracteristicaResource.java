package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoTipoProductoCaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoTipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoCaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.*;

import java.io.Serializable;
import java.util.UUID;


@Path("productoTipoProducto/{idProductoTipoProducto}/tipoProductocaracteristica")
public class ProductoTipoProductoCaracteristicaResource implements Serializable {
    @Inject
    ProductoTipoProductoCaracteristicaDAO ptpcDAO;

    @Inject
    ProductoTipoProductoDAO productoTipoProductoDAO;

    @Inject
    TipoProductoCaracteristicaDAO tipoProductoCaracteristicaDAO;

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
            @PathParam("idProductoTipoProducto")
            Long idProductoTipoProducto)
    {
        if (first >= 0 && max <= 100) {
            try {
                int total = ptpcDAO.count();
                return Response.ok(ptpcDAO.findRange(first, max)).header("Total-records", total).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "first,max").build();
    }


    @GET
    @Path("{idProductoTipoProducto}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("idProductoTipoProducto") UUID idProductoTipoProducto) {
        if (idProductoTipoProducto != null) {
            try {
                ProductoTipoProductoCaracteristica resp = ptpcDAO.findById(idProductoTipoProducto);
                if (resp != null) {
                    return Response.ok(resp).build();
                }
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Record with id "+idProductoTipoProducto+" not found").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot access db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "id").build();
    }

    @DELETE
    @Path("{idProductoTipoProducto}")
    public Response delete(@PathParam("idProductoTipoProducto") UUID idProductoTipoProducto) {
        if (idProductoTipoProducto != null) {
            try {
                ProductoTipoProductoCaracteristica resp = ptpcDAO.findById(idProductoTipoProducto);
                if (resp != null) {
                    ptpcDAO.delete(resp);
                    return Response.noContent().build();
                }
                return Response.status(Response.Status.NOT_FOUND).header("Not-Found", "Record with id " + idProductoTipoProducto + " not found").build();
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
            ProductoTipoProductoCaracteristica entity,
            @PathParam("idProductoTipoProducto") Long idProductoTipoProducto,
            @Context UriInfo uriInfo) {

        if (entity == null) {
            return Response.status(422)
                    .header("Missing-parameter", "Body cannot be null")
                    .build();
        }

        try {
            ProductoTipoProducto ptp = productoTipoProductoDAO.findById(idProductoTipoProducto);
            if (ptp == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "Compra with id " + idProductoTipoProducto + " not found")
                        .build();
            }

            if (entity.getIdTipoProductoCaracteristica() == null || entity.getIdTipoProductoCaracteristica().getId() == null) {
                return Response.status(422)
                        .header("Missing-parameter", "entity.idProducto.id is required")
                        .build();
            }

            TipoProductoCaracteristica tpc = tipoProductoCaracteristicaDAO.findById(entity.getIdTipoProductoCaracteristica().getId());
            if (tpc == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "Producto with id " + entity.getIdTipoProductoCaracteristica().getId() + " not found")
                        .build();
            }

            entity.setId(UUID.randomUUID());
            entity.setIdProductoTipoProducto(ptp);
            entity.setIdTipoProductoCaracteristica(tpc);
            ptpcDAO.create(entity);

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


    // update

    @PUT
    @Path("{idProductoTipoProducto}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("idProductoTipoProducto") java.util.UUID idProductoTipoProducto, ProductoTipoProductoCaracteristica entity) {
        if (idProductoTipoProducto == null || entity == null) {
            return Response.status(422).header("Missing-parameter", "id and entity must not be null").build();
        }
        try {
            ProductoTipoProductoCaracteristica existing = ptpcDAO.findById(idProductoTipoProducto);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Record with id " + idProductoTipoProducto + " not found").build();
            }

            if (entity.getIdTipoProductoCaracteristica() == null || entity.getIdTipoProductoCaracteristica().getId() == null) {
                return Response.status(422).header("Missing-parameter", "entity.idTipoProductoCaracteristica.id is required").build();
            }

            TipoProductoCaracteristica tpc = tipoProductoCaracteristicaDAO.findById(entity.getIdTipoProductoCaracteristica().getId());
            if (tpc == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "TipoProductoCaracteristica with id " + entity.getIdTipoProductoCaracteristica().getId() + " not found")
                        .build();
            }

            // Preserve parent ProductoTipoProducto association
            entity.setIdProductoTipoProducto(existing.getIdProductoTipoProducto());
            entity.setIdTipoProductoCaracteristica(tpc);
            entity.setId(idProductoTipoProducto);

            ProductoTipoProductoCaracteristica updated = ptpcDAO.update(entity);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", e.getMessage()).build();
        }
    }

}
