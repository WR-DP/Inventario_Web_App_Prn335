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
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoTipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Path("producto/{idProducto}/tipoProducto")
public class ProductoTipoProductoResource {

    @Inject
    ProductoTipoProductoDAO productoTipoProductoDAO;

    @Inject
    ProductoDAO productoDAO;

    @Inject
    TipoProductoDAO tipoProductoDAO;


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
            @PathParam("idProducto")
            UUID idProducto)
    {
        if (first >= 0 && max <= 100) {
            try {
                int total = productoTipoProductoDAO.count();
                return Response.ok(productoTipoProductoDAO.findRange(first, max)).header("Total-records", total).build();
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
                ProductoTipoProducto resp = productoTipoProductoDAO.findById(id);
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
    public Response delete(@PathParam("id") UUID id) {
        if (id != null) {
            try {
                ProductoTipoProducto resp = productoTipoProductoDAO.findById(id);
                if (resp != null) {
                    productoTipoProductoDAO.delete(resp);
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
            ProductoTipoProducto entity,
            @PathParam("idProducto") UUID idProducto,
            @Context UriInfo uriInfo) {

        if (entity == null) {
            return Response.status(422)
                    .header("Missing-parameter", "Body cannot be null")
                    .build();
        }

        try {
            Producto producto = productoDAO.findById(idProducto);
            if (producto == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "Compra with id " + idProducto + " not found")
                        .build();
            }

            if (entity.getIdTipoProducto() == null || entity.getIdTipoProducto().getId() == null) {
                return Response.status(422)
                        .header("Missing-parameter", "entity.idProducto.id is required")
                        .build();
            }

            TipoProducto tp = tipoProductoDAO.findById(entity.getIdProducto().getId());
            if (tp == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "Producto with id " + entity.getIdTipoProducto().getId() + " not found")
                        .build();
            }

            entity.setId(UUID.randomUUID());
            entity.setIdProducto(producto);
            entity.setIdTipoProducto(tp);
            productoTipoProductoDAO.create(entity);

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
