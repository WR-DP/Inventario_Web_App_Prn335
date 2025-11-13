package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;

import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class ClienteDAO extends InventarioDefaultDataAccess<Cliente, Object> implements Serializable {
    @PersistenceContext(unitName="InventarioPU")
    private EntityManager em;

    public ClienteDAO() {
        super(Cliente.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Cliente> getEntityClass() {
        return Cliente.class;
    }

    public List<Cliente> buscarClientePorNombre(String nombre, int first, int max) {
        try {
            if (nombre != null && !nombre.isBlank() && first >= 0 && max > 0) {

                TypedQuery<Cliente> q = em.createNamedQuery(
                        "Cliente.buscarClientePorNombre",
                        Cliente.class
                );

                q.setParameter("nombre", "%" + nombre.toUpperCase() + "%");
                q.setFirstResult(first);
                q.setMaxResults(max);

                return q.getResultList();
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Error al buscar clientes por nombre", ex);
        }

        return List.of();
    }


}