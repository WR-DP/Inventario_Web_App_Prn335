package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        return em;
    }

    @Override
    protected Class<Proveedor> getEntityClass() {
        return Proveedor.class;
    }

    List<Proveedor> findByIdProveedor(Integer idProveedor, int first, int max) {
        if (idProveedor != null) {
            try {
                TypedQuery<Proveedor> q = em.createNamedQuery("Proveedor.findByIdProveedor", Proveedor.class);
                q.setParameter("idProveedor", idProveedor);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Parámetro no válido", ex);
            }
        }
        return List.of();
    }

    List<Proveedor> findAllProveedor(int first, int max) {
        try {
            TypedQuery<Proveedor> q = em.createNamedQuery("Proveedor.findAllProveedor", Proveedor.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        } catch (Exception ex) {
            throw new IllegalStateException("Parámetro no válido", ex);
        }
    }

    List<Proveedor> findByActivo(Boolean activo, int first, int max) {
        if (activo != null) {
            try {
                TypedQuery<Proveedor> q = em.createNamedQuery("Proveedor.findByActivo", Proveedor.class);
                q.setParameter("activo", activo);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Parámetro no válido", ex);
            }
        }
        return List.of();
    }

    long countAllProveedor() {
        try {
            TypedQuery<Long> q = em.createNamedQuery("Proveedor.countAllProveedor", Long.class);
            return q.getSingleResult();
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo contar", ex);
        }
    }

    long countByActivo(Boolean activo) {
        if (activo != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("Proveedor.countByActivo", Long.class);
                q.setParameter("activo", activo);
                return q.getSingleResult();
            } catch (Exception ex) {
                throw new IllegalStateException("Parámetro no válido", ex);
            }
        }
        return 0;
    }

    public List<Proveedor> buscarProveedorPorNombre(final String nombre, int first, int max) {
        try {
            if (nombre != null && !nombre.isBlank() && first >= 0 && max > 0) {
                TypedQuery<Proveedor> q = em.createNamedQuery("Proveedor.buscarProveedorPorNombre", Proveedor.class);
                q.setParameter("nombre", "%" + nombre.trim().toUpperCase() + "%");
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            }
        } catch (Exception ex) {
            Logger.getLogger(ProveedorDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return List.of();
    }
}
