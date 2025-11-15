package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.DeleteManager;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named("tipoProductoFrm")
@ViewScoped
public class TipoProductoFrm extends DefaultFrm<TipoProducto> implements Serializable {

    @Inject
    FacesContext facesContext;

    @Inject
    TipoProductoDAO tipoProductoDAO;

    @Inject
    protected TipoProductoCaracteristicaFrm tipoProductoCaracteristicaFrm;

    private TreeNode root;
    private TreeNode selectedNode;
    List<TipoProducto> listaTipoProducto;
    protected String nombreBean="page.tipoProducto";

    public TipoProductoFrm(){};

    @PostConstruct
    public void inicializar() {
        super.inicializar();
        if(this.registro ==null){
            this.registro = nuevoRegistro();
        }
        listaTipoProducto = tipoProductoDAO.findRange(0, Integer.MAX_VALUE);
        cargarArbol();
    }

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<TipoProducto, Object> getDao() {
        return tipoProductoDAO;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return tipoProductoDAO;
    }

    protected TipoProducto nuevoRegistro() {
        TipoProducto tipoProducto = new TipoProducto();
        tipoProducto.setActivo(true);
        tipoProducto.setComentarios("Comentario");
        return tipoProducto;
    }

    @Override
    protected String getIdAsText(TipoProducto r) {
        if (r != null && r.getIdTipoProducto() != null) {
            return r.getIdTipoProducto().toString();
        }
        return null;
    }

    @Override
    protected TipoProducto getIdByText(String id) {
        if (id != null) {
            try {
                Long buscado = Long.parseLong(id);
                return this.modelo.getWrappedData()
                        .stream()
                        .filter(r -> r.getId().equals(buscado))
                        .findFirst().orElse(null);
            }
            catch (NumberFormatException e) {
                Logger.getLogger(TipoProducto.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    /**
     * Carga el árbol jerárquico de tipos de producto
     */
    public void cargarArbol() {
        root = new DefaultTreeNode("Root", null);

        // Obtener todos los tipos padre (sin idTipoProductoPadre)
        List<TipoProducto> padres = tipoProductoDAO.findTiposPadre();

        for (TipoProducto padre : padres) {
            TreeNode nodoPadre = new DefaultTreeNode(padre, root);
            cargarHijos(nodoPadre, padre.getIdTipoProducto());
        }
    }

    /**
     * Carga recursivamente los hijos de un nodo
     */
    private void cargarHijos(TreeNode nodoPadre, Long idPadre) {
        List<TipoProducto> hijos = tipoProductoDAO.findHijosByPadre(idPadre);

        for (TipoProducto hijo : hijos) {
            TreeNode nodoHijo = new DefaultTreeNode(hijo, nodoPadre);
            // Recursivo para cargar nietos y descendientes
            cargarHijos(nodoHijo, hijo.getIdTipoProducto());
        }
    }

    /**
     * Maneja la selección de un nodo en el TreeTable
     */
    public void onNodeSelect(NodeSelectEvent event) {
        this.selectedNode = event.getTreeNode();
        if (selectedNode != null && selectedNode.getData() != null) {
            TipoProducto selected = (TipoProducto) selectedNode.getData();
            this.registro = selected;
            this.estado = ESTADO_CRUD.valueOf("MODIFICAR");

            // ---> sincronizar el formulario de características con el TipoProducto seleccionado
            if (tipoProductoCaracteristicaFrm != null) {
                tipoProductoCaracteristicaFrm.setIdCaracteristica(selected.getId()); // compatibilidad
                tipoProductoCaracteristicaFrm.setIdTipoProducto(selected); // opcional para el child
                // forzar recarga en el child
                tipoProductoCaracteristicaFrm.inicializar();
            }
        }
    }


    @Override
    public void btnGuardarHandler(ActionEvent actionEvent) {
        super.btnGuardarHandler(actionEvent);
        if (this.estado == ESTADO_CRUD.NADA) { // Solo si se guardó exitosamente
            cargarArbol();
            listaTipoProducto = tipoProductoDAO.findRange(0, Integer.MAX_VALUE);
        }
    }

    @Inject
    DeleteManager deleteManager;

    @Override
    public void btnEliminarHandler(ActionEvent actionEvent) {

        if (this.registro == null) {
            enviarMensaje("No hay tipo de producto seleccionado", FacesMessage.SEVERITY_ERROR);
            return;
        }

        Long id = this.registro.getId();

        // ===== CONTAR DEPENDENCIAS =====
        int totalProductos = deleteManager.contarProductosDeTipo(id);
        int totalCaracteristicas = deleteManager.contarCaracteristicasDeTipo(id);

        if (totalProductos > 0 || totalCaracteristicas > 0) {
            enviarMensaje(
                    "Este tipo de producto tenia " + totalProductos +
                            " productos relacionados y " + totalCaracteristicas +
                            " características dependientes.",
                    FacesMessage.SEVERITY_WARN
            );
        }

        try {

            // ===== ELIMINAR EN CASCADA (HIJOS) =====
            deleteManager.eliminarTipoProductoEnCascada(id);

            // ===== AHORA ELIMINAR EL PADRE (TipoProducto) =====
            super.btnEliminarHandler(actionEvent);

            if (this.estado == ESTADO_CRUD.NADA) {
                cargarArbol();
                listaTipoProducto = tipoProductoDAO.findRange(0, Integer.MAX_VALUE);
            }

            enviarMensaje(
                    "Tipo de producto eliminado correctamente.",
                    FacesMessage.SEVERITY_INFO
            );

        } catch (Exception ex) {
            enviarMensaje("Error al eliminar: " + ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }



    /**
     * Obtiene la lista de tipos disponibles para ser padres
     * (excluye el registro actual para evitar ciclos)
     */
    public List<TipoProducto> getTiposPadreDisponibles() {
        if (listaTipoProducto == null) {
            return List.of();
        }

        return listaTipoProducto.stream()
                .filter(t -> registro == null ||
                        !t.getIdTipoProducto().equals(registro.getIdTipoProducto()))
                .collect(Collectors.toList());
    }

    public String getAncestrosAsString(TipoProducto tipoProducto) {
        if (tipoProducto == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        TipoProducto current = tipoProducto.getIdTipoProductoPadre();
        while (current != null) {
            sb.insert(0, " > ").insert(0, current.getIdTipoProductoPadre());
        }
        return sb.toString();
    }

    public TipoProductoCaracteristicaFrm getTipoProductoCaracteristicaFrm() {
        if ( this.registro != null && this.registro.getId() != null ) {
            tipoProductoCaracteristicaFrm.setIdCaracteristica(this.registro.getId());
            tipoProductoCaracteristicaFrm.setIdTipoProducto(this.registro);
        }
        return tipoProductoCaracteristicaFrm;
    }

    @Override
    protected TipoProducto buscarRegistroPorId(Object id) {
        if(id instanceof Long buscado && this.registro != null && !this.registro.getId().equals(buscado)){
            return this.registro.stream().filter(r->r.getId().equals(buscado)).findFirst().orElse(null);
        }
        return null;
    }

    public String getNombreBean() {
        return nombreBean;
    }

    public void setNombreBean(String nombreBean) {
        this.nombreBean = nombreBean;
    }

    public List<TipoProducto> getListaTipoProducto() {
        return listaTipoProducto;
    }
    public void setListaTipoProducto(List<TipoProducto> listaTipoProducto) {
        this.listaTipoProducto = listaTipoProducto;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
}
