package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.VentaDetalle;

import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class VentaDetalleDAO extends InventarioDefaultDataAccess<VentaDetalle, Object> implements Serializable {
    @PersistenceContext(unitName="InventarioPU")
    private EntityManager em;

    public VentaDetalleDAO() {
        super(VentaDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return null;
    }

    @Override
    protected Class<VentaDetalle> getEntityClass() {
        return null;
    }


    // buscar por idVentaDetalle
    List<VentaDetalle> findByIdVentaDetalle(Integer idVentaDetalle, int first, int max) {
        if (idVentaDetalle != null) {
            try {
                TypedQuery<VentaDetalle> q = em.createNamedQuery("VentaDetalle.findByIdVentaDetalle", VentaDetalle.class);
                q.setParameter("idVentaDetalle", idVentaDetalle);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    //buscar todos
    List<VentaDetalle> findAllVentaDetalle(int first, int max) {
        try {
            TypedQuery<VentaDetalle> q = em.createNamedQuery("VentaDetalle.findAllVentaDetalle", VentaDetalle.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        } catch (Exception ex) {
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }

    //buscar por idVenta
    List<VentaDetalle> findByIdVenta(Integer idVenta, int first, int max) {
        if (idVenta != null) {
            try {
                TypedQuery<VentaDetalle> q = em.createNamedQuery("VentaDetalle.findByIdVenta", VentaDetalle.class);
                q.setParameter("idVenta", idVenta);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    // buscar por idProducto
    List<VentaDetalle> findByIdProducto(Integer idProducto, int first, int max) {
        if (idProducto != null) {
            try {
                TypedQuery<VentaDetalle> q = em.createNamedQuery("VentaDetalle.findByIdProducto", VentaDetalle.class);
                q.setParameter("idProducto", idProducto);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    //buscar por cantidad
    List<VentaDetalle> findByCantidad(Integer cantidad, int first, int max) {
        if (cantidad != null) {
            try {
                TypedQuery<VentaDetalle> q = em.createNamedQuery("VentaDetalle.findByCantidad", VentaDetalle.class);
                q.setParameter("cantidad", cantidad);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    //buscar por rango de precio
   List<VentaDetalle> findByPrecio(Double minPrecio, Double maxPrecio, int first, int max) {
       if (minPrecio != null && maxPrecio != null) {
           try {
               TypedQuery<VentaDetalle> q = em.createNamedQuery("VentaDetalle.findByPrecioRange", VentaDetalle.class);
               q.setParameter("minPrecio", minPrecio);
               q.setParameter("maxPrecio", maxPrecio);
               q.setFirstResult(first);
               q.setMaxResults(max);
               return q.getResultList();
           } catch (Exception ex) {
               throw new IllegalStateException("Parámetro no válido", ex);
           }
       }
       return List.of();
   }

    //buscar por estado
    List<VentaDetalle> findByEstado(Boolean estado, int first, int max) {
        if (estado != null) {
            try {
                TypedQuery<VentaDetalle> q = em.createNamedQuery("VentaDetalle.findByEstado", VentaDetalle.class);
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

    //contar todos
    public int countAllVentaDetalle() {
        try {
            TypedQuery<Long> q = em.createNamedQuery("VentaDetalle.countAllVentaDetalle", Long.class);
            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception ex) {
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }

    //contar por idVenta
    public int countByIdVenta(Integer idVenta) {
        if (idVenta != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("VentaDetalle.countByIdVenta", Long.class);
                q.setParameter("idVenta", idVenta);
                return ((Long) q.getSingleResult()).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

    // contar por idProducto
    public int countByIdProducto(Integer idProducto) {
        if (idProducto != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("VentaDetalle.countByIdProducto", Long.class);
                q.setParameter("idProducto", idProducto);
                return ((Long) q.getSingleResult()).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

    // contar por cantidad
    public int countByCantidad(Integer cantidad) {
        if (cantidad != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("VentaDetalle.countByCantidad", Long.class);
                q.setParameter("cantidad", cantidad);
                return ((Long) q.getSingleResult()).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

    // contar por rango de precio
public int countByPrecio(Double minPrecio, Double maxPrecio) {
    if (minPrecio != null && maxPrecio != null) {
        try {
            TypedQuery<Long> q = em.createNamedQuery("VentaDetalle.countByPrecioRange", Long.class);
            q.setParameter("minPrecio", minPrecio);
            q.setParameter("maxPrecio", maxPrecio);
            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception ex) {
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }
    return 0;
    }

    // contar por estado
    public int countByEstado(Boolean estado) {
        if (estado != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("VentaDetalle.countByEstado", Long.class);
                q.setParameter("estado", estado);
                return ((Long) q.getSingleResult()).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

}
