package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Kardex;

import java.io.Serializable;

@Named
@ViewScoped
public class KardexFrm extends DefaultFrm<Kardex> implements Serializable {
    @Override
    protected FacesContext getFacesContext() {
        return null;
    }

    @Override
    protected InventarioDAOInterface<Kardex, Object> getDao() {
        return null;
    }

    @Override
    protected String getIdAsText(Kardex r) {
        return "";
    }

    @Override
    protected Kardex getIdByText(String id) {
        return null;
    }

    @Override
    protected Kardex nuevoRegistro() {
        return null;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return null;
    }

    @Override
    protected Kardex buscarRegistroPorId(Object id) {
        return null;
    }
}
