package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle;

import java.io.Serializable;

@Stateless
@LocalBean
public class CompraDetalleDAO extends InventarioDefaultDataAccess<CompraDetalle, Object> implements Serializable {
    @PersistenceContext(unitName="InventarioPU")
    private EntityManager em;

    public CompraDetalleDAO() {
        super(CompraDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return null;
    }

    @Override
    protected Class<CompraDetalle> getEntityClass() {
        return null;
    }
}
