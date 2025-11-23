package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.VentaDetalle;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@LocalBean
public class ProductoDAO extends InventarioDefaultDataAccess<Producto, Object> implements Serializable {
    @PersistenceContext(unitName="InventarioPU")
    private EntityManager em;
    public ProductoDAO() {
        super(Producto.class);
    }
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    @Override
    protected Class<Producto> getEntityClass() {
        return Producto.class;
    }
    @Override
    public int count() throws IllegalStateException {
        return super.count();
    }

    public List<Producto> buscarProductosPorNombre(final String nombreProducto, int first, int max) {
        try {
            if (nombreProducto != null && !nombreProducto.isBlank() && first >= 0 && max > 0) {
                TypedQuery<Producto> query = em.createNamedQuery("Producto.buscarProductosPorNombre",Producto.class);
                query.setParameter("nombreProducto", "%" + nombreProducto + "%");
                query.setFirstResult(first);
                query.setMaxResults(max);
                return query.getResultList();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Error al buscar productos por nombre", ex);
        }
        return List.of();
    }

    public List<Producto> buscarProductoPorNombre(final String nombreProducto, int first, int max) {
        try {
            if (nombreProducto != null && !nombreProducto.isBlank() && first >= 0 && max > 0) {
                TypedQuery<Producto> query = em.createNamedQuery("Producto.buscarProductosPorNombre",Producto.class);
                query.setParameter("nombreProducto", "%" + nombreProducto + "%");
                query.setFirstResult(first);
                query.setMaxResults(max);
                return query.getResultList();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Error al buscar productos por nombre", ex);
        }
        return List.of();
    }


}