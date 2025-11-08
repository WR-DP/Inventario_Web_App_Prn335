package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Kardex;

import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class KardexDAO extends InventarioDefaultDataAccess<Kardex, Object> implements Serializable {
    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public KardexDAO() {
        super(Kardex.class);
    }


    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Kardex> getEntityClass() {
        return Kardex.class;
    }

}
