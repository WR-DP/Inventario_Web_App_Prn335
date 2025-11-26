package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.KardexDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Kardex;

import java.io.Serializable;

@Named
@ViewScoped
public class KardexFrm extends DefaultFrm<Kardex> implements Serializable {
    @Inject
    KardexDAO kardexDAO;

    @Inject
    FacesContext facesContext;

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<Kardex, Object> getDao() {
        return kardexDAO;
    }

    @Override
    protected String getIdAsText(Kardex r) {

        return null;
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
