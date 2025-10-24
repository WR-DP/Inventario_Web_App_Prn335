package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.enterprise.context.Dependent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.*;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent
@Named
public class ProductoTipoProductoFrm extends DefaultFrm<ProductoTipoProducto> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    ProductoTipoProductoDAO productoTipoProductoDAO;

    //============================================================================================
    //-------------->NO INYECTAR EL CONVERSOR AQUI, USAR DIRECTAMENTE EN LA VISTA<----------------
    //============================================================================================

    @Inject
    TipoProductoDAO tipoProductoDAO;

    @Inject
    ProductoTipoProductoCaracteristicaDAO productoTipoProductoCaracteristicaDAO;

    @Inject
    TipoProductoCaracteristicaDAO tipoProductoCaracteristicaDAO;

    List<TipoProductoCaracteristica> posibleCaracteristicas;

    //cuando ya se tenga caracteristica<-------------------
    List<TipoProductoCaracteristica> caracteristicasAsignadas;

    protected UUID idProducto;

    @Override
    public List<ProductoTipoProducto> cargarDatos(int first, int max) {
        try{
        if(first>=0 && max>0 && this.idProducto!=null){
                return productoTipoProductoDAO.findByidProducto(this.idProducto, first, max);
        }
        }catch(Exception ex){
            Logger.getLogger(ProductoTipoProductoFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    @Override
    public int contarDatos() {
        try{
            if(this.idProducto!=null){
                return this.productoTipoProductoDAO.countByidProducto(this.idProducto).intValue();
            }
        }catch(Exception ex){
            Logger.getLogger(ProductoTipoProductoFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return 0;
    }

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<ProductoTipoProducto, Object> getDao() {
        return productoTipoProductoDAO;
    }

    @Override
    protected String getIdAsText(ProductoTipoProducto r) {
        if(r!=null && r.getId()!=null){
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected ProductoTipoProducto getIdByText(String id) {
        if(id!= null && !id.isBlank() && this.modelo!=null && this.modelo.getWrappedData()!=null && !this.modelo.getWrappedData().isEmpty()){
            try{
                UUID idUUID = UUID.fromString(id);
                return this.modelo.getWrappedData().stream().filter(r -> r.getId().equals(idUUID)).findFirst().orElse(null);
            } catch (Exception ex){
                Logger.getLogger(ProductoTipoProductoFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    protected ProductoTipoProducto nuevoRegistro() {
        ProductoTipoProducto producto = new ProductoTipoProducto();
        producto.setActivo(true);
        producto.setId(UUID.randomUUID());
        producto.setFechaCreacion(OffsetDateTime.now());
        if(idProducto!=null){
            producto.setIdProducto(new Producto());
            producto.getIdProducto().setId(idProducto);
        }
        return producto;
    }

    List<ProductoTipoProducto> listaTipoProducto;
    public List<ProductoTipoProducto> getListaTipoProducto() {
        return listaTipoProducto;
    }

    public ProductoTipoProductoFrm(){};

    public Long getIdTipoProductoSeleccionado(){
        if(this.registro != null && this.registro.getIdTipoProducto() != null){
            return this.registro.getIdTipoProducto().getIdTipoProducto();
        }
        return null;
    }

    public void setIdTipoProductoSeleccionado(final Long idTipoProducto){
        if(this.registro != null && this.listaTipoProducto != null && !this.listaTipoProducto.isEmpty()){
            this.registro.setIdTipoProducto(this.listaTipoProducto.stream()
                    .filter(tp -> tp.getIdTipoProducto().equals(idTipoProducto))
                    .findFirst()
                    .orElse(null).getIdTipoProducto());
        }
    }

    public List<TipoProducto> buscarTiposPorNombres(final String nombres){
        try{
            if(nombres!=null && !nombres.isBlank()){
                return tipoProductoDAO.findByNameLike(nombres, 0, 25);
            }
        }catch(Exception ex){
            Logger.getLogger(ProductoTipoProductoFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }

    public void btnSeleccionarTipoProductoHandler(ActionEvent event){
        try{
            //si no obtiene resultado deseado revisar la query de findByTipoIdProducto
            this.posibleCaracteristicas = tipoProductoCaracteristicaDAO.findByTipoIdProducto(this.registro.getIdTipoProducto().getId(), 0, Integer.MAX_VALUE);
            return;
        }catch(Exception ex){
            Logger.getLogger(ProductoTipoProductoFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
         this.posibleCaracteristicas = List.of();
    }


    //implementar funcionalidad cuando ya este caracteristica
    public List<TipoProductoCaracteristica> getCaracteristicasAsignadas() {
        return caracteristicasAsignadas;
    }

    public List<TipoProductoCaracteristica> getPosibleCaracteristicas() {
        return posibleCaracteristicas;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return productoTipoProductoDAO;
    }

    @Override
    protected ProductoTipoProducto buscarRegistroPorId(Object id) {
        if(id instanceof UUID buscado && this.modelo != null){
            return this.modelo.getWrappedData().stream().filter(r -> r.getId().equals(buscado)).findFirst().orElse(null);
        }
        return null;
    }

    public UUID getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(UUID idProducto) {
        this.idProducto = idProducto;
    }

    protected String nombreBean="page.productoTipoProducto";

    @Override
    public String getNombreBean() {
        return nombreBean;
    }


}
