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
        return null;
    }

    @Override
    protected Class<Compra> getEntityClass() {
        return null;
    }

    // buscar por idCompra
    List<Compra> findByIdCompra(Integer idCompra, int first, int max) {
        if (idCompra != null) {
            try {
                TypedQuery<Compra> q = em.createNamedQuery("Compra.findByIdCompra", Compra.class);
                q.setParameter("idCompra", idCompra);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    // buscar todos
    List<Compra> findAllCompra(int first, int max) {
        try {
            TypedQuery<Compra> q = em.createNamedQuery("Compra.findAllCompra", Compra.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        } catch (Exception ex) {
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }

    // buscar por estado
    List<Compra> findByEstado(Boolean estado, int first, int max) {
        if (estado != null) {
            try {
                TypedQuery<Compra> q = em.createNamedQuery("Compra.findByEstado", Compra.class);
                q.setParameter("estado", estado);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    // buscar por fecha
    List<Compra> findByFecha(java.util.Date fecha, int first, int max) {
        if (fecha != null) {
            try {
                TypedQuery<Compra> q = em.createNamedQuery("Compra.findByFecha", Compra.class);
                q.setParameter("fecha", fecha);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    //contar por idCompra
    public int countByIdCompra(Integer idCompra) {
        if (idCompra != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("Compra.countByIdCompra", Long.class);
                q.setParameter("idCompra", idCompra);
                return ((Long) q.getSingleResult()).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

    //contar todos
    public int countAllCompra() {
        try {
            TypedQuery<Long> q = em.createNamedQuery("Compra.countAllCompra", Long.class);
            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception ex) {
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }


}
