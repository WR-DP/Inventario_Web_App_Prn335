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

    //Validacion ya existe compra para ese proveedor
    public boolean existeCompraDeProveedor(Integer idProveedor) {
        TypedQuery<Compra> q = em.createNamedQuery("Compra.findByProveedor", Compra.class);
        q.setParameter("idProveedor", idProveedor);
        q.setMaxResults(1);
        return !q.getResultList().isEmpty();
    }

    // Opcionales ya existentes
    public List<Compra> findAllCompra(int first, int max) {
        TypedQuery<Compra> q = em.createNamedQuery("Compra.findAllCompra", Compra.class);
        q.setFirstResult(first);
        q.setMaxResults(max);
        return q.getResultList();
    }

    public List<Compra> findByEstado(String estado, int first, int max) {
        TypedQuery<Compra> q = em.createNamedQuery("Compra.findByEstado", Compra.class);
        q.setParameter("estado", estado);
        q.setFirstResult(first);
        q.setMaxResults(max);
        return q.getResultList();
    }
}
