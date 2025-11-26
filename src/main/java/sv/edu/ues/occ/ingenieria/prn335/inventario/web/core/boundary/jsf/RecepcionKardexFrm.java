package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CompraDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class RecepcionKardexFrm extends DefaultFrm<Compra> implements Serializable {

    @Inject
    CompraDAO compraDAO;

    @Inject
    ProductoDAO productoDAO;

    @Inject
    FacesContext facesContext;


    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<Compra, Object> getDao() {
        return compraDAO;
    }

    @Override
    protected String getIdAsText(Compra dato) {
        if (dato != null && dato.getId() !=null){
            return dato.getId().toString();
        }
        return null;
    }

    @Override
    protected Compra getIdByText(String id) {
        if(id != null && !id.isBlank() && this.modelo != null && this.modelo.getWrappedData() != null && this.modelo.getWrappedData().size() > 0) {
            try{
                Long idLong = Long.parseLong(id);
                return this.modelo.getWrappedData().stream().filter(compra -> compra.getId().equals(idLong)).findFirst().orElse(null);
            } catch (NumberFormatException e){
                Logger.getLogger(CompraFrm.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    protected Compra nuevoRegistro() {
        Compra compra = new Compra();
        compra.setFecha(new Date());
        compra.setEstado("ACTIVA");
        return compra;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return compraDAO;
    }

    @Override
    protected Compra buscarRegistroPorId(Object id) {
        return null;
    }

    public String getNombreBean(){
        return "Recibir Productos";
    }


    @Override
    public void btnNuevoHandler (ActionEvent actionEvent) { return; }

    @Override
    public void btnModificarHandler (ActionEvent actionEvent) { return;}

    @Override
    public void btnEliminarHandler (ActionEvent actionEvent) { return; }

    @Override
    public void btnGuardarHandler (ActionEvent actionEvent) { return; }

    @Override
    public void btnCancelarHandler (ActionEvent actionEvent) { return; }

    @Override
    public List<Compra> cargarDatos(int first, int max){
        return compraDAO.buscarLibrosParaRecepcion(first, max);

    }

    @Override
    public int contarDatos(){
        return compraDAO.contarLibrosParaRecepcion().intValue();

    }

    public String getRandom(){
        return UUID.randomUUID().toString();
    }

    public void actualizarTabla(ActionEvent  actionEvent) {
        System.out.println("Actualizar la tabla");
    }

}
