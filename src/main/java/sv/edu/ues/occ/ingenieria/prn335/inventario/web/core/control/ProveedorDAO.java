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
        return em;
    }

    @Override
    protected Class<Proveedor> getEntityClass() {
        return Proveedor.class;
    }

    public List<Proveedor> buscarProveedorPorNombre(String nombre, int first, int max) {
        try{
            if (nombre != null && !nombre.isBlank() && first >= 0 && max > 0) {

                TypedQuery<Proveedor> q = em.createNamedQuery(
                        "Proveedor.buscarProveedorPorNombre",
                        Proveedor.class
                );

                q.setParameter("nombre", "%" + nombre.toUpperCase() + "%");
                q.setFirstResult(first);
                q.setMaxResults(max);

                return q.getResultList();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Error al buscar proveedores por nombre", ex);
        }
        return List.of();
    }
}