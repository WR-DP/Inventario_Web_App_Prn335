package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Kardex;

import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class KardexDAO extends InventarioDefaultDataAccess<Kardex, Object> implements Serializable {
    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public KardexDAO() {
        super(Kardex.class);
    }


    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Kardex> getEntityClass() {
        return Kardex.class;
    }

    //buscar por idKardex
    public List<Kardex> findByIdKardex(Integer idKardex) {
        if (idKardex != null) {
            try {
                TypedQuery<Kardex> q = em.createNamedQuery("Kardex.findByIdKardex", Kardex.class);
                q.setParameter("idKardex", idKardex);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalArgumentException("El parametro no es valido", ex);
            }
        }
        return List.of();
    }

    //buscar todos
    public List<Kardex> findAllKardex(int first, int max) {
        try {
            TypedQuery<Kardex> q = em.createNamedQuery("Kardex.findAllKardex", Kardex.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        } catch (Exception ex) {
            throw new IllegalArgumentException("El parametro no es valido", ex);
        }
    }

    //buscar por idCompra
    public List<Kardex> findByIdCompra(Integer idCompra) {
        if (idCompra != null) {
            try {
                TypedQuery<Kardex> q = em.createNamedQuery("Kardex.findByIdCompra", Kardex.class);
                q.setParameter("idCompraDetalle", idCompra);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalArgumentException("El parametro no es valido", ex);
            }
        }
        return List.of();
    }

    //buscar por idVenta
    public List<Kardex> findByIdVenta(Integer idVenta) {
        if (idVenta != null) {
            try {
                TypedQuery<Kardex> q = em.createNamedQuery("Kardex.findByIdVenta", Kardex.class);
                q.setParameter("idVentaDetalle", idVenta);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalArgumentException("El parametro no es valido", ex);
            }
        }
        return List.of();
    }

    //buscar por idAlmacen
    public List<Kardex> findByIdAlmacen(Integer idAlmacen) {
        if (idAlmacen != null) {
            try {
                TypedQuery<Kardex> q = em.createNamedQuery("Kardex.findByIdAlmacen", Kardex.class);
                q.setParameter("idAlmacen", idAlmacen);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalArgumentException("El parametro no es valido", ex);
            }
        }
        return List.of();
    }

    //buscar por cantidad
    public List<Kardex> findByCantidad(Double cantidad) {
        if (cantidad != null) {
            try {
                TypedQuery<Kardex> q = em.createNamedQuery("Kardex.findByCantidad", Kardex.class);
                q.setParameter("cantidad", cantidad);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalArgumentException("El parametro no es valido", ex);
            }
        }
        return List.of();
    }

    //buscar por rango de precio
    public List<Kardex> findByRangoPrecio(Double precioMin, Double precioMax) {
        if (precioMin != null && precioMax != null) {
            try {
                TypedQuery<Kardex> q = em.createNamedQuery("Kardex.findByPrecioRange", Kardex.class);
                q.setParameter("minPrecio", precioMin);
                q.setParameter("maxPrecio", precioMax);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalArgumentException("El parametro no es valido", ex);
            }
        }
        return List.of();
    }

    //contar por idKardex
    public int countByIdKardex(Integer idKardex) {
        if (idKardex != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("Kardex.countByIdKardex", Long.class);
                q.setParameter("idKardex", idKardex);
                return q.getSingleResult().intValue();
            } catch (Exception ex) {
                throw new IllegalArgumentException("El parametro no es valido", ex);
            }
        }
        return 0;
    }

    //contar todos
    public int countAllKardex() {
        try {
            TypedQuery<Long> q = em.createNamedQuery("Kardex.countAll", Long.class);
            return q.getSingleResult().intValue();
        } catch (Exception ex) {
            throw new IllegalArgumentException("El parametro no es valido", ex);
        }
    }


}
