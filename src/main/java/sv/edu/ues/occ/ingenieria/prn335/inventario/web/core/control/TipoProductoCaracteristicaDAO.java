package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.ProductoTipoProductoCaracteristica;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProductoCaracteristica;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@LocalBean
public class TipoProductoCaracteristicaDAO extends InventarioDefaultDataAccess<TipoProductoCaracteristica, Object> implements Serializable {

    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public TipoProductoCaracteristicaDAO() {
        super(TipoProductoCaracteristica.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<TipoProductoCaracteristica> getEntityClass() {
        return TipoProductoCaracteristica.class;
    }

    @Override
    public int count() throws IllegalStateException {
        return super.count();
    }

    /**
     * Buscar por idTipoProducto (usar Long para el id)
     */
    public List<TipoProductoCaracteristica> findByIdTipoProducto(final Long idTipoProducto, int first, int max) {
        if (idTipoProducto != null) {
            try {
                return em.createNamedQuery("TipoProductoCaracteristica.findByIdTipoProducto", TipoProductoCaracteristica.class)
                        .setParameter("idTipoProducto", idTipoProducto)
                        .setFirstResult(first)
                        .setMaxResults(max)
                        .getResultList();
            } catch (Exception ex) {
                Logger.getLogger(TipoProductoCaracteristicaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return List.of();
    }

    /**
     * Contar por idTipoProducto
     */
    public Long countByIdTipoProducto(final Long idTipoProducto) {
        if (idTipoProducto != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("TipoProductoCaracteristica.countByIdTipoProducto", Long.class);
                q.setParameter("idTipoProducto", idTipoProducto);
                return q.getSingleResult();
            } catch (Exception ex) {
                Logger.getLogger(TipoProductoCaracteristicaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return 0L;
    }

    /**
     * Mantengo métodos auxiliares existentes (buscar por id de la entidad TipoProductoCaracteristica)
     */
    public List<TipoProductoCaracteristica> findByIdCaracteristica(final Long id, int first, int max) {
        if (id != null) {
            try {
                return em.createNamedQuery("TipoProductoCaracteristica.findById", TipoProductoCaracteristica.class)
                        .setParameter("id", id)
                        .setFirstResult(first)
                        .setMaxResults(max)
                        .getResultList();
            } catch (Exception ex) {
                Logger.getLogger(TipoProductoCaracteristicaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return List.of();
    }

    public Long countByIdCaracteristica(final Long id) {
        if (id != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("TipoProductoCaracteristica.countById", Long.class);
                q.setParameter("id", id);
                return q.getSingleResult();
            } catch (Exception ex) {
                Logger.getLogger(TipoProductoCaracteristicaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return 0L;
    }

    public List<TipoProductoCaracteristica> findByIdPadre(final Long idPadre, int first, int max) {
        if (idPadre != null) {
            try {
                return em.createNamedQuery("TipoProductoCaracteristica.findByIdPadre", TipoProductoCaracteristica.class)
                        .setParameter("idTipoProductoPadre", idPadre)
                        .setFirstResult(first)
                        .setMaxResults(max)
                        .getResultList();
            } catch (Exception ex) {
                Logger.getLogger(TipoProductoCaracteristicaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return List.of();
    }

    public List<TipoProductoCaracteristica> findByNombreCaracteristica(final String nombreCaracteristica, int first, int max) {
        if (nombreCaracteristica != null && !nombreCaracteristica.isBlank()) {
            try {
                return em.createNamedQuery("TipoProductoCaracteristica.findByNombreCaracteristica", TipoProductoCaracteristica.class)
                        .setParameter("nombreCaracteristica", "%" + nombreCaracteristica + "%")
                        .setFirstResult(first)
                        .setMaxResults(max)
                        .getResultList();
            } catch (Exception ex) {
                Logger.getLogger(TipoProductoCaracteristicaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return List.of();
    }

    public List<ProductoTipoProductoCaracteristica> findByProductoTipoProducto(UUID idProductoTipoProducto, int first, int max) {
        if (idProductoTipoProducto == null) return Collections.emptyList();
        try {
            TypedQuery<ProductoTipoProductoCaracteristica> q =
                    em.createNamedQuery("ProductoTipoProductoCaracteristica.findByIdProductoTipoProducto", ProductoTipoProductoCaracteristica.class);
            q.setParameter("id", idProductoTipoProducto);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ProductoTipoProductoCaracteristicaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return Collections.emptyList();
        }
    }

    public ProductoTipoProductoCaracteristica save(ProductoTipoProductoCaracteristica entidad) {
        try {
            if (entidad.getId() == null) {
                em.persist(entidad);
                return entidad;
            } else {
                return em.merge(entidad);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProductoTipoProductoCaracteristicaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean removeByProductoTipoProductoAndCaracteristica(UUID idProductoTipoProducto, Long idTipoProductoCaracteristica) {
        try {
            int deleted = em.createNamedQuery(
                            "ProductoTipoProductoCaracteristica.removeByProductoTipoProductoAndCaracteristica")
                    .setParameter("idPtpp", idProductoTipoProducto)
                    .setParameter("idCar", idTipoProductoCaracteristica)
                    .executeUpdate();
            return deleted > 0;
        } catch (Exception ex) {
            Logger.getLogger(ProductoTipoProductoCaracteristicaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public TipoProductoCaracteristica findById(final Long id) {
        if (id == null) return null;
        try {
            return em.find(TipoProductoCaracteristica.class, id);
        } catch (Exception ex) {
            Logger.getLogger(TipoProductoCaracteristicaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

}
