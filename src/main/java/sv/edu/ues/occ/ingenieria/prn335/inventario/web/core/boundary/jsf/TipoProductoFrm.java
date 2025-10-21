package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class TipoProductoFrm extends DefaultFrm<TipoProducto> implements Serializable {
    @Inject
    FacesContext facesContext;

    @Inject
    TipoProductoDAO tipoProductoDAO;

    @Override
    public String getTituloPag() {
        return "Tipo de Productos";
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
    }


    //------------> yo digo que deberiamos pode agregar el Tipo (columna que se muestra id_tipo_producto_padre)
    @Override
    protected TipoProducto nuevoRegistro() {
        TipoProducto tipoProducto = new TipoProducto();
        tipoProducto.setActivo(true);
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

}

