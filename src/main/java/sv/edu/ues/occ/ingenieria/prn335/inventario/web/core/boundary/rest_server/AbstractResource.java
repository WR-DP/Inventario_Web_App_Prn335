package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractResource<T, I> implements Serializable {
    protected InventarioDefaultDataAccess<T, I> bean;

    //para obtener el bean DAO específico
    protected abstract InventarioDefaultDataAccess<T, I> getBean();

    //para poder obtener el ID de la entidad (UUID, Integer o Long)
    protected abstract I getIdEntity(T entity);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(T entity, @Context UriInfo uriInfo) {
        if (entity == null) {
            return Response.status(422).header("Wrong-Parameters", "Entity is null").build();
        }
        I id = getIdEntity(entity);
        if (id != null) {
            return Response.status(422).header("Wrong-Parameters", "Entity already has ID: " + id).build();
        }
        try {
            bean = getBean();
            bean.create(entity);
            I newId = getIdEntity(entity);
            if (newId != null) {
                UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
                uriBuilder.path(newId.toString());
                return Response.created(uriBuilder.build()).type(MediaType.APPLICATION_JSON).build();
            }
            return Response.status(400).header("Process-Error", "Record could not be created").build();
        } catch (Exception e) {
            Logger.getLogger(AbstractResource.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRange(@DefaultValue("0") @QueryParam("first") int firstResult,
                              @DefaultValue("100") @QueryParam("max") int maxResult) {

        if (firstResult < 0 || maxResult < firstResult) {
            return Response.status(422).header("Wrong-Parameter", "first:" + firstResult + ", max:" + maxResult).build();
        }
        try {
            bean = getBean();
            List<T> lista = bean.findRange(firstResult, maxResult);
            Integer total = bean.count();
            if (lista != null) {
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
    public Response findById(@PathParam("id") I id) {
        if (id == null) {
            return Response.status(422).header("Wrong-Parameter", "Id is null").build();
        }
        try {
            bean = getBean();
            T encontrado = bean.findById(id);
            if (encontrado != null) {
                return Response.ok(encontrado).type(MediaType.APPLICATION_JSON).build();
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
    public Response update(T entity) {
        if (entity == null) {
            return Response.status(422).header("Wrong-Parameters", "Entity is null").build();
        }
        I id = getIdEntity(entity);
        if (id == null) {
            return Response.status(422).header("Wrong-Parameters", "Entity ID is null").build();
        }
        try {
            bean = getBean();
            bean.update(entity);
            T actualizado = bean.findById(id);
            if (actualizado != null && actualizado.equals(entity)) {
                return Response.ok(actualizado).type(MediaType.APPLICATION_JSON).build();
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
