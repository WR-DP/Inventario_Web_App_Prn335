package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.UnidadMedida;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@LocalBean
public class UnidadMedidaDAO extends InventarioDefaultDataAccess<UnidadMedida, Object> implements Serializable {
    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public UnidadMedidaDAO() {
        super(UnidadMedida.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<UnidadMedida> getEntityClass() {
        return UnidadMedida.class;
    }

    // buscar por idUnidadMedida
    public List<UnidadMedida> findByIdUnidadMedida(Integer idUnidadMedida, int first, int max) {
        if (idUnidadMedida != null) {
            try {
                TypedQuery<UnidadMedida> q = em.createNamedQuery("UnidadMedida.findByIdUnidadMedida", UnidadMedida.class);
                q.setParameter("idUnidadMedida", idUnidadMedida);
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
    public List<UnidadMedida> findAllUnidadMedida(int first, int max) {
        try {
            TypedQuery<UnidadMedida> q = em.createNamedQuery("UnidadMedida.findAllUnidadMedida", UnidadMedida.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        } catch (Exception ex) {
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }

    // buscar por activo
    public List<UnidadMedida> findByActivo(Boolean activo, int first, int max) {
        if (activo != null) {
            try {
                TypedQuery<UnidadMedida> q = em.createNamedQuery("UnidadMedida.findByActivo", UnidadMedida.class);
                q.setParameter("activo", activo);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    //contar todos
    public int countAllUnidadMedida() {
        try {
            TypedQuery<Long> q = em.createNamedQuery("UnidadMedida.countAllUnidadMedida", Long.class);
            return q.getSingleResult().intValue();
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo contar", ex);
        }
    }

    //contar por activo
    public int countByActivo(Boolean activo) {
        if (activo != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("UnidadMedida.countByActivo", Long.class);
                q.setParameter("activo", activo);
                return ((Long) q.getSingleResult()).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

    public List<UnidadMedida> findByIdTipoUnidadMedida(Integer idTipoUnidadMedida, int first, int max) {
        if (idTipoUnidadMedida != null) {
            try {
                TypedQuery<UnidadMedida> q = em.createNamedQuery("UnidadMedida.findByIdTipoUnidadMedida", UnidadMedida.class);
                q.setParameter("idTipoUnidadMedida", idTipoUnidadMedida);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                Logger.getLogger(ProductoTipoProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Collections.emptyList();
    }

    public Long countByIdTipoUnidadMedida(Integer idTipoUnidadMedida) {
        if (idTipoUnidadMedida != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("UnidadMedida.countByIdTipoUnidadMedida", Long.class);
                q.setParameter("idTipoUnidadMedida", idTipoUnidadMedida);
                return q.getSingleResult();
            } catch (Exception ex) {
                Logger.getLogger(ProductoTipoProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0L;
    }
}
