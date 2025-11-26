package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.*;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.*;

import java.io.Serializable;
import java.util.UUID;

@Path("tipoProducto/{idTipoProducto}/caracteristica")
public class TipoProductoCaracteristicaResource  implements Serializable {
    @Inject
    TipoProductoCaracteristicaDAO tipoProductoCaracteristicaDAO;

    @Inject
    TipoProductoDAO tipoProductoDAO;

    @Inject
    CaracteristicaDAO caracteristicaDAO;

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
            @PathParam("idTipoProducto")
            Long idTipoProducto)
    {
        if (first >= 0 && max <= 100) {
            try {
                int total = tipoProductoCaracteristicaDAO.count();
                return Response.ok(tipoProductoCaracteristicaDAO.findRange(first, max)).header("Total-records", total).build();
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
                TipoProductoCaracteristica resp = tipoProductoCaracteristicaDAO.findById(id);
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
                TipoProductoCaracteristica resp = tipoProductoCaracteristicaDAO.findById(id);
                if (resp != null) {
                    tipoProductoCaracteristicaDAO.delete(resp);
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
    public Response create(
            TipoProductoCaracteristica entity,
            @PathParam("idTipoProducto") Long idTipoProducto,
            @Context UriInfo uriInfo) {

        if (entity == null) {
            return Response.status(422)
                    .header("Missing-parameter", "Body cannot be null")
                    .build();
        }

        try {
            TipoProducto tp = tipoProductoDAO.findById(idTipoProducto);
            if (tp == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "Compra with id " + idTipoProducto + " not found")
                        .build();
            }

            if (entity.getIdCaracteristica() == null || entity.getIdCaracteristica().getId() == null) {
                return Response.status(422)
                        .header("Missing-parameter", "entity.idCaracteristica.id is required")
                        .build();
            }

            Caracteristica c = caracteristicaDAO.findById(entity.getIdCaracteristica().getId());
            if (c == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "Producto with id " + entity.getIdCaracteristica().getId() + " not found")
                        .build();
            }

            entity.setIdTipoProducto(tp);
            entity.setIdCaracteristica(c);
            tipoProductoCaracteristicaDAO.create(entity);

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


}
