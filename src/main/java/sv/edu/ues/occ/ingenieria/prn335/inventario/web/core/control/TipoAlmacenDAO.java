package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoAlmacen;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

@Stateless
@LocalBean
public class TipoAlmacenDAO extends InventarioDefaultDataAccess<TipoAlmacen, Object> implements Serializable {
    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public TipoAlmacenDAO() {
        super(TipoAlmacen.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Class<TipoAlmacen> getEntityClass() {
        return TipoAlmacen.class;
    }

    @Override
    public TipoAlmacen findById(Object id) {
        return super.findById(id);
    }

    @Override
    public int count() throws IllegalStateException {
        return super.count();
    }




    private static final Logger log = Logger.getLogger(TipoAlmacenDAO.class.getName());

public List<TipoAlmacen> findAllAlmacen(int first, int max) {
        try{
    TypedQuery<TipoAlmacen> query = em.createNamedQuery("TipoAlmacen.findAllAlmacen", TipoAlmacen.class);
    query.setFirstResult(first);
    query.setMaxResults(max);
    return query.getResultList();
    } catch (Exception e) {
            throw new IllegalArgumentException("Error al buscar todos los tipos de almacen", e);
        }
    }
}
