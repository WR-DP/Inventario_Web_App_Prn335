package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;


@Path("tipoProducto")
public class TipoProductoResource {

    @Inject
    TipoProductoDAO tipoProductoDAO;

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
                int total = tipoProductoDAO.count();
                return Response.ok(tipoProductoDAO.findRange(first, max))
                        .header("Total-records", total).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .header("Server-exception", "Cannot access db").build();
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
                TipoProducto resp = tipoProductoDAO.findById(id);
                if (resp != null) {
                    return Response.ok(resp).build();
                }
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "Record with id "+id+" not found").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .header("Server-exception", "Cannot access db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "id").build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        if (id != null) {
            try {
                TipoProducto resp = tipoProductoDAO.findById(id);
                if (resp != null) {
                    tipoProductoDAO.delete(resp);
                    return Response.noContent().build();
                }
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-Found", "Record with id " + id + " not found").build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .header("Server-exception", "Cannot acces db").build();
            }
        }
        return Response.status(422).header("Missing-parameter", "id").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(TipoProducto entity, @Context UriInfo uriInfo) {
        if (entity != null && entity.getId() == null) {
            try {
                if (entity.getIdTipoProductoPadre() != null && entity.getIdTipoProductoPadre().getId() != null) {
                    TipoProducto padre = tipoProductoDAO.findById(entity.getIdTipoProductoPadre().getId());
                    if(padre==null){
                        return Response.status(422)
                                .header("Missing-parameter", "If parent is assigned, must not be null and exist in the db").build();
                    }
                    entity.setIdTipoProductoPadre(padre);
                }
                tipoProductoDAO.create(entity);
                return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(entity.getId())).build()).build();
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", "Cannot acces db").build();
            }
        } else {
            return Response.status(422).header("Missing-parameter", "entity must not be null and entity.id be null").build();
        }
    }

    //update
//    @PUT
//    @Path("{id}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response update(@PathParam("id") Long id, TipoProducto entity) {
//        if (id == null || entity == null) {
//            return Response.status(422).header("Missing-parameter", "id and entity must not be null").build();
//        }
//        try {
//            TipoProducto existing = tipoProductoDAO.findById(id);
//            if (existing == null) {
//                return Response.status(Response.Status.NOT_FOUND)
//                        .header("Not-found", "Record with id " + id + " not found").build();
//            }
//            if (entity.getIdTipoProductoPadre() != null && entity.getIdTipoProductoPadre().getId() != null) {
//                TipoProducto padre = tipoProductoDAO.findById(entity.getIdTipoProductoPadre().getId());
//                if (padre == null) {
//                    return Response.status(422)
//                            .header("Missing-parameter", "If parent is assigned, must not be null and exist in the db").build();
//                }
//                existing.setIdTipoProductoPadre(padre);
//            } else {
//                existing.setIdTipoProductoPadre(null);
//            }
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .header("Server-exception", "Cannot access db").build();
//        }
//        return Response.status(422).header("Missing-parameter", "id").build();
//    }

}