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

    public List<Producto> findByIdProducto(UUID idProducto, int first, int max) {
        if(idProducto != null){
            try{
                TypedQuery<Producto> q = em.createNamedQuery("Producto.findByIdProducto", Producto.class);
                q.setParameter("id", idProducto);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return List.of();
    }
    public List<Producto> findAllProducto(int first, int max) {
        try{
            TypedQuery<Producto> q = em.createNamedQuery("Producto.findAllProducto", Producto.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
    public List<Producto> findByActivo(Boolean activo, int first, int max) {
        if(activo != null){
            try{
                TypedQuery<Producto> q = em.createNamedQuery("Producto.findByActivo", Producto.class);
                q.setParameter("activo", activo);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return List.of();
    }
    public int countAllProductos() {
        try{
            TypedQuery<Long> q = em.createNamedQuery("Producto.countAllProducto", Long.class);
            return q.getSingleResult().intValue();
        } catch (Exception ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return 0;
    }
    public int countByActivo(Boolean activo) {
        if(activo != null){
            try{
                TypedQuery<Long> q = em.createNamedQuery("Producto.countByActivo", Long.class);
                q.setParameter("activo", activo);
                return q.getSingleResult().intValue();
            } catch (Exception ex) {
                Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return 0;
    }
    public int countByIdProducto(UUID idProducto) {
        if(idProducto != null){
            try{
                TypedQuery<Long> q = em.createNamedQuery("Producto.countByIdProducto", Long.class);
                q.setParameter("id", idProducto);
                return q.getSingleResult().intValue();
            } catch (Exception ex) {
                Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return 0;
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


}