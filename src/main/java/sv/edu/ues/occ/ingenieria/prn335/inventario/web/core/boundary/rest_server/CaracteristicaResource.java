package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Almacen;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Caracteristica;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoAlmacen;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoUnidadMedida;

import java.io.Serializable;

@Path("caracteristica")
public class CaracteristicaResource  implements Serializable {
    @Inject
    CaracteristicaDAO caracteristicaDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fidnReange(
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
                int total = caracteristicaDAO.count();
                return Response.ok(caracteristicaDAO.findRange(first, max)).header("Total-records", total).build();
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
                Caracteristica resp = caracteristicaDAO.findById(id);
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
                Caracteristica resp = caracteristicaDAO.findById(id);
                if (resp != null) {
                    caracteristicaDAO.delete(resp);
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
    public Response create(Caracteristica entity, @Context UriInfo uriInfo) {

        // Validación básica
        if (entity == null || entity.getId() != null) {
            return Response.status(422)
                    .header("Missing-parameter", "entity must not be null and entity.id must be null")
                    .build();
        }

        try {
            // Validación de dependencia: TipoUnidadMedida
            if (entity.getIdTipoUnidadMedida() == null ||
                    entity.getIdTipoUnidadMedida().getId() == null) {

                return Response.status(422)
                        .header("Missing-parameter", "idTipoUnidadMedida.id is required")
                        .build();
            }

            // Buscar el tipo real en DB
            TipoUnidadMedida tipo = caracteristicaDAO.findTipoUnidadMedidaById(entity.getIdTipoUnidadMedida().getId());

            if (tipo == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "TipoUnidadMedida with id "
                                + entity.getIdTipoUnidadMedida().getId() + " not found")
                        .build();
            }

            // Asignar la versión administrada por JPA
            entity.setIdTipoUnidadMedida(tipo);

            // Persistir
            caracteristicaDAO.create(entity);

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



}
