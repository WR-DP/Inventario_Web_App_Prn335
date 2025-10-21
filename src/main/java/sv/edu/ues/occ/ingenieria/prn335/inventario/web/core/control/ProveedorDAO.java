package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class ProveedorDAO extends InventarioDefaultDataAccess<Proveedor, Object> implements Serializable {
    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public ProveedorDAO() {
        super(Proveedor.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return null;
    }

    @Override
    protected Class<Proveedor> getEntityClass() {
        return null;
    }

    // buscar por idProveedor
    List<Proveedor> findByIdProveedor(Integer idProveedor, int first, int max) {
        if (idProveedor != null) {
            try {
                TypedQuery<Proveedor> q = em.createNamedQuery("Proveedor.findByIdProveedor", Proveedor.class);
                q.setParameter("idProveedor", idProveedor);
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
    List<Proveedor> findAllProveedor(int first, int max) {
        try {
            TypedQuery<Proveedor> q = em.createNamedQuery("Proveedor.findAllProveedor", Proveedor.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        } catch (Exception ex) {
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }

    //buscar por activo
    List<Proveedor> findByActivo(Boolean activo, int first, int max) {
        if (activo != null) {
            try {
                TypedQuery<Proveedor> q = em.createNamedQuery("Proveedor.findByActivo", Proveedor.class);
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

    //contar por id
    public int countByIdProveedor(Integer idProveedor) {
        if (idProveedor != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("Proveedor.countByIdProveedor", Long.class);
                q.setParameter("idProveedor", idProveedor);
                return ((Long) q.getSingleResult()).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

    //contar todos
    public int countAllProveedor() {
        try {
            TypedQuery<Long> q = em.createNamedQuery("Proveedor.countAllProveedor", Long.class);
            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception ex) {
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }

    //contar por activo
    public int countByActivo(Boolean activo) {
        if (activo != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("Proveedor.countByActivo", Long.class);
                q.setParameter("activo", activo);
                return ((Long) q.getSingleResult()).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

}
