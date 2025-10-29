package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;

import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class VentaDAO extends InventarioDefaultDataAccess<Venta, Object> implements Serializable {

    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public VentaDAO() {
        super(Venta.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Venta> getEntityClass() {
        return Venta.class;
    }

    @Override
    public int count() throws IllegalStateException {
        return super.count();
    }

    // Buscar por idVenta
    public List<Venta> findByIdVenta(Integer idVenta, int first, int max) {
        if (idVenta != null) {
            try {
                TypedQuery<Venta> q = em.createNamedQuery("Venta.findByIdVenta", Venta.class);
                q.setParameter("idVenta", idVenta);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Error al buscar por ID", ex);
            }
        }
        return List.of();
    }

    // Buscar todas las ventas
    public List<Venta> findAllVenta(int first, int max) {
        try {
            TypedQuery<Venta> q = em.createNamedQuery("Venta.findAll", Venta.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        } catch (Exception ex) {
            throw new IllegalStateException("Error al listar ventas", ex);
        }
    }

    // Buscar por cliente
    public List<Venta> findByIdCliente(Integer idCliente, int first, int max) {
        if (idCliente != null) {
            try {
                TypedQuery<Venta> q = em.createNamedQuery("Venta.findByIdCliente", Venta.class);
                q.setParameter("idCliente", idCliente);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Error al buscar por cliente", ex);
            }
        }
        return List.of();
    }

    // Buscar por estado
//    public List<Venta> findByEstado(String estado, int first, int max) {
//        if (estado != null) {
//            try {
//                TypedQuery<Venta> q = em.createNamedQuery("Venta.findByEstado", Venta.class);
//                q.setParameter("estado", estado);
//                q.setFirstResult(first);
//                q.setMaxResults(max);
//                return q.getResultList();
//            } catch (Exception ex) {
//                throw new IllegalStateException("Error al buscar por estado", ex);
//            }
//        }
//        return List.of();
//    }

    // Buscar por fecha
//    public List<Venta> findByFecha(java.util.Date fecha, int first, int max) {
//        if (fecha != null) {
//            try {
//                TypedQuery<Venta> q = em.createNamedQuery("Venta.findByFecha", Venta.class);
//                q.setParameter("fecha", fecha);
//                q.setFirstResult(first);
//                q.setMaxResults(max);
//                return q.getResultList();
//            } catch (Exception ex) {
//                throw new IllegalStateException("Error al buscar por fecha", ex);
//            }
//        }
//        return List.of();
//    }


//    public int countAllVenta() {
//        try {
//            TypedQuery<Long> q = em.createNamedQuery("Venta.countAllVenta", Long.class);
//            return q.getSingleResult().intValue();
//        } catch (Exception ex) {
//            throw new IllegalStateException("Error al contar todas las ventas", ex);
//        }
//    }


//    public int countByEstado(String estado) {
//        if (estado != null) {
//            try {
//                TypedQuery<Long> q = em.createNamedQuery("Venta.countByEstado", Long.class);
//                q.setParameter("estado", estado);
//                return q.getSingleResult().intValue();
//            } catch (Exception ex) {
//                throw new IllegalStateException("Error al contar por estado", ex);
//            }
//        }
//        return 0;
//    }
}
