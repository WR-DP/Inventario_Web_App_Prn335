package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
            throw new IllegalArgumentException("El parametro no es valido",ex);
        }
       }
//       return Collections.emptyList();
        return List.of();
    }

    //buscar todos
    public List<Producto> findAllProducto(int first, int max) {
        try{
            TypedQuery<Producto> q = em.createNamedQuery("Producto.findAllProducto", Producto.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        } catch (Exception ex) {
            throw new IllegalArgumentException("El parametro no es valido",ex);
        }
    }

    //buscar por activo
    public List<Producto> findByActivo(Boolean activo, int first, int max) {
        if(activo != null){
         try{
             TypedQuery<Producto> q = em.createNamedQuery("Producto.findByActivo", Producto.class);
             q.setParameter("activo", activo);
             q.setFirstResult(first);
             q.setMaxResults(max);
             return q.getResultList();
         } catch (Exception ex) {
             throw new IllegalArgumentException("El parametro no es valido",ex);
         }
        }
        return List.of();
     }

    //contar todos los productos
    public int countAllProductos() {
        try{
            TypedQuery<Long> q = em.createNamedQuery("Producto.countAllProducto", Long.class);
            return q.getSingleResult().intValue();
        } catch (Exception ex) {
            throw new IllegalArgumentException("El parametro no es valido",ex);
        }
    }

    //contar por activo
    public int countByActivo(Boolean activo) {
        if(activo != null){
            try{
                TypedQuery<Long> q = em.createNamedQuery("Producto.countByActivo", Long.class);
                q.setParameter("activo", activo);
                return q.getSingleResult().intValue();
            } catch (Exception ex) {
                throw new IllegalArgumentException("El parametro no es valido",ex);
            }
        }
        return List.of().size();
    }

    public int countByIdProducto(UUID idProducto) {
        if(idProducto != null){
            try{
                TypedQuery<Long> q = em.createNamedQuery("Producto.countByIdProducto", Long.class);
                q.setParameter("id", idProducto);
                return q.getSingleResult().intValue();
            } catch (Exception ex) {
                throw new IllegalArgumentException("El parametro no es valido",ex);
            }
        }
        return List.of().size();
    }


}
