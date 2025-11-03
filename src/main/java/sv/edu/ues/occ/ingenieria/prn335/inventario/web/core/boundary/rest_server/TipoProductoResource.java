package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;

import java.util.List;

@Path("tipo_producto")
public class TipoProductoResource {

    @Inject
    TipoProductoDAO tipoProductoDAO;

    //tecnologia marchal
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fidnReange(
            @Min(0)
            @DefaultValue("0")
            @QueryParam("first")
            int first,
            @Max(20)
            @DefaultValue("10")
            @QueryParam("max")
            int max) {


        if(first>=0 && max<=50) {

            try{
                tipoProductoDAO.count();
                return Response.ok(tipoProductoDAO.findRange(first, max)).header("Total-records", "").build();
            }catch(Exception e){
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception","").build();
            }


        }
        Response.status(422).header("Missing-parameter", "first,max").build();


        //public List<TipoProducto> getTipoProducto()
        return (Response) tipoProductoDAO.findRange(0, 100);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") Long id) {
        if(id!=null){

            try{
                TipoProducto resp= tipoProductoDAO.findById(id);
                if(resp!=null){
                    return Response.ok(resp).build();
                }
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception","").build();

            }catch(Exception e){
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception","").build();
            }
        }

        return Response.status(422).header("Missing-parameter", "id").build();
    }


    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id){

        if(id!=null){

            try{
                TipoProducto resp= tipoProductoDAO.findById(id);
                if(resp!=null){
                    tipoProductoDAO.delete(resp);
                    return Response.noContent().build();
                }
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception","").build();

            }catch(Exception e){
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header("Server-exception","").build();
            }
        }

        return Response.status(422).header("Missing-parameter", "id").build();

    }
}
