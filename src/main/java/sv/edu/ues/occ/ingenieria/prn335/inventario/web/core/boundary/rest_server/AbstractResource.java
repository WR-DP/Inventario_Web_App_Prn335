package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractResource<T, I> implements Serializable {

    protected InventarioDefaultDataAccess<T, I> bean;

    protected abstract InventarioDefaultDataAccess<T, I> getBean();
    protected abstract I getIdEntity(T entity);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create new entity", description = "Creates a new record in the database. ID must be null.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Entity created"),
            @APIResponse(responseCode = "400", description = "Entity could not be created"),
            @APIResponse(responseCode = "422", description = "Invalid entity or entity already has ID"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response create(T entity, @Context UriInfo uriInfo) {
        if (entity == null || getIdEntity(entity) != null) {
            return Response.status(422).header("Wrong-Parameters", "Entity must not be null and ID must be null").build();
        }
        try {
            bean = getBean();
            bean.create(entity);
            I newId = getIdEntity(entity);
            if (newId == null) {
                return Response.status(400).header("Process-Error", "Record could not be created").build();
            }
            return Response.created(uriInfo.getAbsolutePathBuilder().path(newId.toString())
                    .build()).type(MediaType.APPLICATION_JSON).build();

            //por si tenemos fallas con la comprobacion anterior
//            if (newId != null) {
//                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
//                uriBuilder.path(newId.toString());
//                return Response.created(uriBuilder.build())
//                        .type(MediaType.APPLICATION_JSON)
//                        .build();
//            }
//
//            return Response.status(400)
//                    .header("Process-Error", "Record could not be created")
//                    .build();


        } catch (Exception e) {
            Logger.getLogger(AbstractResource.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find a range of entities", description = "Returns entities based on the provided range parameters.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "List of entities",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "404", description = "No records found"),
            @APIResponse(responseCode = "422", description = "Invalid range parameters"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response findRange(@DefaultValue("0") @QueryParam("first") int firstResult,
                              @DefaultValue("100") @QueryParam("max") int maxResult) {

        if (firstResult < 0 || maxResult < firstResult) {
            return Response.status(422).header("Wrong-Parameter", "first:" + firstResult + ", max:" + maxResult).build();
        }
        try {
            bean = getBean();
            List<T> lista = bean.findRange(firstResult, maxResult);
            Integer total = bean.count();
            if (lista != null && !lista.isEmpty()) {
                return Response.ok(lista).header("Total-Records", total).type(MediaType.APPLICATION_JSON).build();
            }
            return Response.status(404).header("Records-Not-Found", "Min " + firstResult + " Max " + maxResult).build();
        } catch (Exception e) {
            Logger.getLogger(AbstractResource.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find entity by ID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Entity found"),
            @APIResponse(responseCode = "404", description = "Entity not found"),
            @APIResponse(responseCode = "422", description = "Invalid ID"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response findById(@PathParam("id") I id) {
        if (id == null) {
            return Response.status(422).header("Wrong-Parameter", "Id is null").build();
        }
        try {
            bean = getBean();
            T found = bean.findById(id);
            if (found != null) {
                return Response.ok(found).type(MediaType.APPLICATION_JSON).build();
            }
            return Response.status(404).header("Record-Not-Found", "Id " + id).build();
        } catch (Exception e) {
            Logger.getLogger(AbstractResource.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update entity")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Entity updated"),
            @APIResponse(responseCode = "400", description = "Update failed"),
            @APIResponse(responseCode = "422", description = "Invalid entity or missing ID"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response update(T entity) {
        if (entity == null || getIdEntity(entity) == null) {
            return Response.status(422).header("Wrong-Parameters", "Entity must not be null and must have ID").build();
        }
        I id = getIdEntity(entity);
        try {
            bean = getBean();
            bean.update(entity);
            T updated = bean.findById(id);
            if (updated != null && updated.equals(entity)) {
                return Response.ok(updated).type(MediaType.APPLICATION_JSON).build();
            }
            return Response.status(400).header("Process-Error", "Record could not be updated").build();
        } catch (Exception e) {
            Logger.getLogger(AbstractResource.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Delete entity by ID")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Entity deleted"),
            @APIResponse(responseCode = "404", description = "Entity not found"),
            @APIResponse(responseCode = "422", description = "Invalid ID"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response delete(@PathParam("id") I id) {
        if (id == null) {
            return Response.status(422).header("Wrong-Parameter", "Id is null").build();
        }
        try {
            bean = getBean();
            T entity = bean.findById(id);
            if (entity == null) {
                return Response.status(404).header("Record-Not-Found", "Record could not be deleted").build();
            }
            bean.delete(entity);
            return Response.ok(entity).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            Logger.getLogger(AbstractResource.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.status(500).entity(e.getMessage()).build();
        }
    }
}
