package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProductoCaracteristica;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
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


//    @Override
//    public int count(TipoProductoCaracteristicaDAO registro) throws IllegalStateException {
//        return em.createQuery("select count(t) from TipoProductoCaracteristica t", Integer.class).getSingleResult();
//    }

    //si no obtiene resultado deseado revisar la query
    public List<TipoProductoCaracteristica> findByTipoIdProducto(final Long idTipoProducto, int first, int max) {
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

    //contar por id tipo producto caracteristica
    public int countByIdProducto(final Long id) {
        if (id != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("TipoProductoCaracteristica.countByIdProducto", Long.class);
                q.setParameter("id", id);
                return q.getSingleResult().intValue();
            } catch (Exception ex) {
                Logger.getLogger(TipoProductoCaracteristicaDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return 0;
    }

    //Si no funciona la busqueda por id padre, revisar la query
    //busqueda por id padre.... podria el tipo de variable TipoProducto o Long
    public List<TipoProductoCaracteristica> findByIdPadre(final TipoProducto idPadre, int first, int max) {
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

    //busqueda por nombre
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

}
