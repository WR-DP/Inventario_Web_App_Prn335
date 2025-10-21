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

    List<Cliente> findByIdCliente(Integer idCliente, int first, int max) {
        if(idCliente !=null){
            try{
                TypedQuery<Cliente> q = em.createNamedQuery("Cliente.findByIdCliente", Cliente.class);
                q.setParameter("idCliente", idCliente);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            }catch(Exception ex){
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    List <Cliente> findAllCliente(int first, int max) {
        try{
            TypedQuery<Cliente> q = em.createNamedQuery("Cliente.findAllCliente", Cliente.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        }catch(Exception ex){
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }

    List<Cliente> findByActivo(Boolean activo, int first, int max) {
        if(activo !=null){
            try{
                TypedQuery<Cliente> q = em.createNamedQuery("Cliente.findByActivo", Cliente.class);
                q.setParameter("activo", activo);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            }catch(Exception ex){
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    long countAllCliente() {
        try{
            TypedQuery<Long> q = em.createNamedQuery("Cliente.countAllCliente", Long.class);
            return q.getSingleResult();
        }catch(Exception ex){
            throw new IllegalStateException("No se pudo contar", ex);
        }
    }

    long countByActivo(Boolean activo) {
        if (activo != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("Cliente.countByActivo", Long.class);
                q.setParameter("activo", activo);
                return q.getSingleResult();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }
}
