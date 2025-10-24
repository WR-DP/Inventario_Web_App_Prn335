package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
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


    private TreeNode root;
    private TreeNode selectedNode;

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<TipoProducto, Object> getDao() {
        return tipoProductoDAO;
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
                return this.modelo.getWrappedData().stream().filter(r -> r.getId().equals(buscado)).findFirst().orElse(null);
            } catch (NumberFormatException e) {
                Logger.getLogger(TipoProducto.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    List<TipoProducto> listaTipoProducto;

    public TipoProductoFrm(){};

    @PostConstruct
    @Override
    public void inicializar() {
        super.inicializar();
        listaTipoProducto = tipoProductoDAO.findRange(0, Integer.MAX_VALUE);
        cargarArbol();
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
            this.estado = "MODIFICAR";
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


    /**
     * Sobrescribe el método de guardado para recargar el árbol
     */
    @Override
    public void btnGuardarHandler() {
        super.btnGuardarHandler();
        cargarArbol(); // Recargar el árbol después de guardar
    }

    /**
     * Sobrescribe el método de eliminación para recargar el árbol
     */
    @Override
    public void btnEliminarHandler() {
        super.btnEliminarHandler();
        cargarArbol(); // Recargar el árbol después de eliminar
    }


    //------------> yo digo que deberiamos pode agregar el Tipo (columna que se muestra id_tipo_producto_padre)
    @Override
    protected TipoProducto nuevoRegistro() {
        TipoProducto tipoProducto = new TipoProducto();
        tipoProducto.setActivo(true);
        //corregir el setIdTipoProductoPadre
        tipoProducto.setIdTipoProductoPadre(null);
        tipoProducto.setComentarios("Comentario");
        return tipoProducto;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return tipoProductoDAO;
    }

    @Override
    protected TipoProducto buscarRegistroPorId(Object id) {
        if(id instanceof Long buscado && this.registro != null && !this.registro.getId().equals(buscado)){
            return this.registro.stream().filter(r->r.getId().equals(buscado)).findFirst().orElse(null);
        }
        return null;
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
protected String nombreBean="page.tipoProducto";

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

