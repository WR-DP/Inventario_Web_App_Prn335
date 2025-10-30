package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;

import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class CompraDAO extends InventarioDefaultDataAccess<Compra, Object> implements Serializable {

    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public CompraDAO() {
        super(Compra.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Compra> getEntityClass() {
        return Compra.class;
    }

    @Override
    public int count() throws IllegalStateException {
        return super.count();
    }

    // Buscar por idCompra
    public List<Compra> findByIdCompra(Long idCompra, int first, int max) {
        if (idCompra != null) {
            TypedQuery<Compra> q = em.createNamedQuery("Compra.findByIdCompra", Compra.class);
            q.setParameter("idCompra", idCompra);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        }
        return List.of();
    }

    // Buscar todas las compras
    public List<Compra> findAllCompra(int first, int max) {
        TypedQuery<Compra> q = em.createNamedQuery("Compra.findAllCompra", Compra.class);
        q.setFirstResult(first);
        q.setMaxResults(max);
        return q.getResultList();
    }

    // Buscar por proveedor
    public List<Compra> findByProveedor(Object idProveedor, int first, int max) {
        if (idProveedor != null) {
            TypedQuery<Compra> q = em.createNamedQuery("Compra.findByProveedor", Compra.class);
            q.setParameter("idProveedor", idProveedor);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        }
        return List.of();
    }


}
