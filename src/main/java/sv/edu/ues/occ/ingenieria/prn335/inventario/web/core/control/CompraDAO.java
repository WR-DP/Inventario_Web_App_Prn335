package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

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

    public List<Compra> buscarLibrosParaRecepcion(int first, int max) {
        TypedQuery<Compra> q = em.createQuery(
                "SELECT c FROM Compra c WHERE c.estado = 'ACTIVA'",
                Compra.class
        );

        q.setFirstResult(first);
        q.setMaxResults(max);

        return q.getResultList();
    }

    public Long contarLibrosParaRecepcion() {
        return em.createQuery(
                "SELECT COUNT(c) FROM Compra c WHERE c.estado = 'ACTIVA'",
                Long.class
        ).getSingleResult();
    }
}
