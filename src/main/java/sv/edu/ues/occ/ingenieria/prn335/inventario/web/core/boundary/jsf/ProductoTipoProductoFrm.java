package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.enterprise.context.Dependent;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
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
    List<TipoProductoCaracteristica> caracteristicasAsignadas;
    List<ProductoTipoProductoCaracteristica> asignacionesPersistentes;
    List<ProductoTipoProductoCaracteristica> asignacionesAEliminar;

    private TipoProductoCaracteristica seleccionPosibleCaracteristica;
    private TipoProductoCaracteristica seleccionCaracteristicaAsignada;

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
            Long idTipo = this.registro.getIdTipoProducto().getIdTipoProducto();
            // 1) todas las posibles del tipo
            this.posibleCaracteristicas = tipoProductoCaracteristicaDAO.findByIdTipoProducto(idTipo, 0, Integer.MAX_VALUE);

            // 2) cargar asignaciones existentes (intermedia) para este ProductoTipoProducto
            if (this.registro != null && this.registro.getId() != null) {
                this.asignacionesPersistentes = productoTipoProductoCaracteristicaDAO.findByProductoTipoProductoId(this.registro.getId(), 0, Integer.MAX_VALUE);
            } else {
                this.asignacionesPersistentes = new java.util.ArrayList<>();
            }

            // 3) construir la lista de TipoProductoCaracteristica asignadas para la vista
//            this.caracteristicasAsignadas = this.asignacionesPersistentes.stream()
//                    .map(ap -> ap.getIdTipoProductoCaracteristica().getIdCaracteristica() != null ? ap.getIdTipoProductoCaracteristica().getIdCaracteristica() : ap.getIdTipoProductoCaracteristica().getIdCaracteristica())
//                    // en tu modelo TipoProductoCaracteristica contiene getIdCaracteristica() -> Caracteristica,
//                    // pero en la vista trabajas con TipoProductoCaracteristica; por simplicidad extrae el TipoProductoCaracteristica:
//                    .map(ap -> ap.getIdTipoProductoCaracteristica().getIdCaracteristica()) // si quieres mostrar Caracteristica en la vista en lugar de TipoProductoCaracteristica cambia el UI
//                    .toList();

            //opcion mas simple:
//            this.caracteristicasAsignadas = this.asignacionesPersistentes.stream()
//                    .map(ap -> ap.getIdTipoProductoCaracteristica() != null ? ap.getIdTipoProductoCaracteristica() : null)
//                    .toList();



            // Si prefieres mantener caracteristicasAsignadas como List<TipoProductoCaracteristica>, mejor:
            this.caracteristicasAsignadas = this.asignacionesPersistentes.stream()
                    .map(ProductoTipoProductoCaracteristica::getIdTipoProductoCaracteristica)
                    .map(tpc -> tpc) // TipoProductoCaracteristica
                    .toList();

            if (this.posibleCaracteristicas != null) {
                this.posibleCaracteristicas = new java.util.ArrayList<>(this.posibleCaracteristicas);
            } else {
                this.posibleCaracteristicas = new java.util.ArrayList<>();
            }

            if (this.caracteristicasAsignadas != null) {
                this.caracteristicasAsignadas = new java.util.ArrayList<>(this.caracteristicasAsignadas);
            } else {
                this.caracteristicasAsignadas = new java.util.ArrayList<>();
            }

            if (this.asignacionesPersistentes != null) {
                this.asignacionesPersistentes = new java.util.ArrayList<>(this.asignacionesPersistentes);
            } else {
                this.asignacionesPersistentes = new java.util.ArrayList<>();
            }

// ahora se pueden usar removeIf/add sin lanzar UnsupportedOperationException
            if (this.posibleCaracteristicas != null) {
                this.posibleCaracteristicas.removeIf(pc ->
                        this.asignacionesPersistentes.stream()
                                .anyMatch(ap -> ap.getIdTipoProductoCaracteristica().getId().equals(pc.getId()))
                );
            }

//            // 4) remover de posibles las que ya están asignadas (comparando por id de TipoProductoCaracteristica)
//            if (this.posibleCaracteristicas != null) {
//                this.posibleCaracteristicas.removeIf(pc ->
//                        this.asignacionesPersistentes.stream()
//                                .anyMatch(ap -> ap.getIdTipoProductoCaracteristica().getId().equals(pc.getId()))
//                );
//            }

            // 5) asegurar obligatorias del tipo (crear en memoria si faltan)
            List<TipoProductoCaracteristica> obligatorias = tipoProductoCaracteristicaDAO.findObligatoriasByTipo(idTipo);
            for (TipoProductoCaracteristica tpc : obligatorias) {
                boolean ya = this.asignacionesPersistentes.stream()
                        .anyMatch(ap -> ap.getIdTipoProductoCaracteristica().getId().equals(tpc.getId()));
                if (!ya) {
                    ProductoTipoProductoCaracteristica nueva = new ProductoTipoProductoCaracteristica();
                    nueva.setId(UUID.randomUUID());
                    nueva.setIdProductoTipoProducto(this.registro);
                    nueva.setIdTipoProductoCaracteristica(tpc);
                    // obligatorias no tienen flag en la entidad intermedia; la obligación está en TipoProductoCaracteristica.obligatorio
                    this.asignacionesPersistentes.add(nueva);
                }
            }

            return;
        } catch(Exception ex){
            Logger.getLogger(ProductoTipoProductoFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        this.posibleCaracteristicas = List.of();
        this.caracteristicasAsignadas = new java.util.ArrayList<>();
        this.asignacionesPersistentes = new java.util.ArrayList<>();
    }

    public void btnAgregarPosibleCaracteristicaHandler(ActionEvent event){
        //la funcionalidad de este boton es agregar la caracteristica seleccionada a la lista de caracteristicas asignadas
        try{
            Object sel = event.getComponent().getAttributes().get("selectedCaracteristica");
            if(!(sel instanceof TipoProductoCaracteristica)){
                return;
            }
            TipoProductoCaracteristica seleccion = (TipoProductoCaracteristica) sel;

            if (this.asignacionesPersistentes == null) this.asignacionesPersistentes = new java.util.ArrayList<>();

            // crear asignacion intermedia en memoria
            ProductoTipoProductoCaracteristica nueva = new ProductoTipoProductoCaracteristica();
            nueva.setId(UUID.randomUUID());
            nueva.setIdProductoTipoProducto(this.registro);
            nueva.setIdTipoProductoCaracteristica(seleccion);
            this.asignacionesPersistentes.add(nueva);

            // actualizar vista
            if (this.caracteristicasAsignadas == null) this.caracteristicasAsignadas = new java.util.ArrayList<>();
            if (!this.caracteristicasAsignadas.contains(seleccion)) {
                this.caracteristicasAsignadas.add(seleccion);
            }
            if (this.posibleCaracteristicas != null) {
                this.posibleCaracteristicas.removeIf(pc -> pc.getId().equals(seleccion.getId()));
            }

        }catch(Exception ex){
            Logger.getLogger(ProductoTipoProductoFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void btnEliminarCaracteristicaAsignadaHandler(ActionEvent event){
        //la funcionalidad de este boton es eliminar la caracteristica seleccionada de la lista de caracteristicas asignadas
        //pero ojo, solo se puede eliminar si la caracteristica no es obligatoria
        try{
            Object sel = event.getComponent().getAttributes().get("selectedCaracteristica");
            if(!(sel instanceof TipoProductoCaracteristica)){
                return;
            }
            TipoProductoCaracteristica seleccion = (TipoProductoCaracteristica) sel;

            // si es obligatoria según el tipo, bloquear
            if (Boolean.TRUE.equals(seleccion.getObligatorio())) {
                Logger.getLogger(ProductoTipoProductoFrm.class.getName()).log(Level.INFO, "Intento de eliminar caracteristica obligatoria denegado");
                return;
            }

            // encontrar la asignacion intermedia en memoria
            ProductoTipoProductoCaracteristica encontrada = null;
            if (this.asignacionesPersistentes != null) {
                for (ProductoTipoProductoCaracteristica ap : this.asignacionesPersistentes) {
                    if (ap.getIdTipoProductoCaracteristica().getId().equals(seleccion.getId())) {
                        encontrada = ap;
                        break;
                    }
                }
            }

            if (encontrada != null) {
                // si ya estaba persistida: eliminar DB
                if (productoTipoProductoCaracteristicaDAO.exists(encontrada.getId())) {
                    productoTipoProductoCaracteristicaDAO.delete(encontrada); // usa método delete del DAO genérico
                }
                // quitar en memoria
                ProductoTipoProductoCaracteristica finalEncontrada = encontrada;
                this.asignacionesPersistentes.removeIf(a -> a.getId().equals(finalEncontrada.getId()));
            }

            // actualizar listas de vista
            if (this.caracteristicasAsignadas != null) {
                this.caracteristicasAsignadas.removeIf(c -> c.getId().equals(seleccion.getId()));
            }
            if (this.posibleCaracteristicas == null) this.posibleCaracteristicas = new java.util.ArrayList<>();
            if (this.posibleCaracteristicas.stream().noneMatch(p -> p.getId().equals(seleccion.getId()))) {
                this.posibleCaracteristicas.add(seleccion);
            }

        }catch(Exception ex){
            Logger.getLogger(ProductoTipoProductoFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public void btnModificarHandler(ActionEvent actionEvent) {
        if (this.registro == null) {
            this.enviarMensaje("No hay registro seleccionado", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            // cargar estado actual en BD
            List<ProductoTipoProductoCaracteristica> bdActuales =
                    productoTipoProductoCaracteristicaDAO.findByProductoTipoProductoId(this.registro.getId(), 0, Integer.MAX_VALUE);

            // detectar a crear (en asignacionesPersistentes pero no en bdActuales)
            List<ProductoTipoProductoCaracteristica> aCrear = new java.util.ArrayList<>();
            for (ProductoTipoProductoCaracteristica ap : getAsignacionesPersistentes()) {
                boolean found = bdActuales.stream()
                        .anyMatch(b -> b.getIdTipoProductoCaracteristica().getId().equals(ap.getIdTipoProductoCaracteristica().getId()));
                if (!found) aCrear.add(ap);
            }

            // detectar a eliminar (en bdActuales pero no en asignacionesPersistentes)
            List<ProductoTipoProductoCaracteristica> aEliminar = new java.util.ArrayList<>();
            for (ProductoTipoProductoCaracteristica b : bdActuales) {
                boolean found = getAsignacionesPersistentes().stream()
                        .anyMatch(ap -> ap.getIdTipoProductoCaracteristica().getId().equals(b.getIdTipoProductoCaracteristica().getId()));
                if (!found) aEliminar.add(b);
            }

            // aplicar cambios en BD
            for (ProductoTipoProductoCaracteristica crear : aCrear) {
                productoTipoProductoCaracteristicaDAO.create(crear);
            }
            for (ProductoTipoProductoCaracteristica eliminar : aEliminar) {
                productoTipoProductoCaracteristicaDAO.delete(eliminar);
            }

            // actualizar el registro principal si tiene cambios
            this.getDao().update(this.registro);

            enviarMensaje("Registro modificado", FacesMessage.SEVERITY_INFO);
            this.inicializarRegistros();
            this.estado = ESTADO_CRUD.NADA;
            this.registro = null;
        } catch (Exception ex) {
            enviarMensaje("Error al modificar el registro: " + ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }


    //En caracteristicas asignadas deben de estar las caracteristicas que ya tiene el producto tipo producto
    //incluyendo las obligatorias siempre
    public List<TipoProductoCaracteristica> getCaracteristicasAsignadas() {
        if (this.caracteristicasAsignadas == null) {
            this.caracteristicasAsignadas = new java.util.ArrayList<>();
        }
        return caracteristicasAsignadas;
    }

    //En posible caracteristicas deben de estar las caracteristicas que puede tener el producto tipo producto
    //sin incluir las que ya tiene asignadas y las que son obligatorias por que ya estan asignadas
    public List<TipoProductoCaracteristica> getPosibleCaracteristicas() {
        if (this.posibleCaracteristicas == null) {
            this.posibleCaracteristicas = new java.util.ArrayList<>();
        }
        return posibleCaracteristicas;
    }

    public TipoProductoCaracteristica getSeleccionPosibleCaracteristica() {
        return seleccionPosibleCaracteristica;
    }

    public void setSeleccionPosibleCaracteristica(TipoProductoCaracteristica seleccionPosibleCaracteristica) {
        this.seleccionPosibleCaracteristica = seleccionPosibleCaracteristica;
    }

    public TipoProductoCaracteristica getSeleccionCaracteristicaAsignada() {
        return seleccionCaracteristicaAsignada;
    }

    public void setSeleccionCaracteristicaAsignada(TipoProductoCaracteristica seleccionCaracteristicaAsignada) {
        this.seleccionCaracteristicaAsignada = seleccionCaracteristicaAsignada;
    }

    public List<ProductoTipoProductoCaracteristica> getAsignacionesPersistentes() {
        if (this.asignacionesPersistentes == null) this.asignacionesPersistentes = new java.util.ArrayList<>();
        return this.asignacionesPersistentes;
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
