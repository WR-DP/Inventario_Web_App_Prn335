package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.VentaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;

import java.io.Serializable;
import java.util.UUID;

@Path("venta")
public class VentaResource  implements Serializable {
    @Inject
    VentaDAO ventaDAO;


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
                int total = ventaDAO.count();
                return Response.ok(ventaDAO.findRange(first, max)).header("Total-records", total).build();
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
                Venta resp = ventaDAO.findById(id);
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
                Venta resp = ventaDAO.findById(id);
                if (resp != null) {
                    ventaDAO.delete(resp);
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
    public Response create(Venta entity, @Context UriInfo uriInfo) {

        // Validación básica
        if (entity == null || entity.getId() != null) {
            return Response.status(422)
                    .header("Missing-parameter", "entity must not be null and entity.id must be null")
                    .build();
        }

        try {
            // Validación de dependencia: Cliente
            if (entity.getIdCliente() == null ||
                    entity.getIdCliente().getId() == null) {

                return Response.status(422)
                        .header("Missing-parameter", "idCliente.id is required")
                        .build();
            }

            // Buscar el cliente real en DB
            Cliente tipo = ventaDAO.findClienteById(entity.getIdCliente().getId());

            if (tipo == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .header("Not-found", "TipoUnidadMedida with id "
                                + entity.getIdCliente().getId() + " not found")
                        .build();
            }

            // Asignar la versión administrada por JPA
            entity.setIdCliente(tipo);

            // Persistir
            ventaDAO.create(entity);

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
    public Response update(@PathParam("id") UUID id, Venta entity) {
        if (id == null || entity == null) {
            return Response.status(422).header("Missing-parameter", "id and entity must not be null").build();
        }
        try {
            Venta existing = ventaDAO.findById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Record with id " + id + " not found").build();
            }

            if (entity.getIdCliente() == null || entity.getIdCliente().getId() == null) {
                return Response.status(422).header("Missing-parameter", "idCliente.id is required").build();
            }

            Cliente cliente = ventaDAO.findClienteById(entity.getIdCliente().getId());
            if (cliente == null) {
                return Response.status(Response.Status.NOT_FOUND).header("Not-found", "Cliente with id " + entity.getIdCliente().getId() + " not found").build();
            }

            entity.setIdCliente(cliente);
            entity.setId(id);
            Venta updated = ventaDAO.update(entity);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception", e.getMessage()).build();
        }
    }


}
