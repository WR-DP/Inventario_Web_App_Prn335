package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;

import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class VentaDAO extends InventarioDefaultDataAccess<Venta, Object> implements Serializable {
    @PersistenceContext(unitName="InventarioPU")
    private EntityManager em;

    public VentaDAO() {
        super(Venta.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return null;
    }

    @Override
    protected Class<Venta> getEntityClass() {
        return null;
    }

    // buscar por idVenta
    List<Venta> findByIdVenta(Integer idVenta, int first, int max) {
        if(idVenta !=null){
            try{
                TypedQuery<Venta> q = em.createNamedQuery("Venta.findByIdVenta", Venta.class);
                q.setParameter("idVenta", idVenta);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            }catch(Exception ex){
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    //buscar por todos
    List<Venta> findAllVenta(int first, int max) {
        try{
            TypedQuery<Venta> q = em.createNamedQuery("Venta.findAllVenta", Venta.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        }catch(Exception ex){
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }

    //buscar por cliente
    List<Venta> findByIdCliente(Integer idCliente, int first, int max) {
        if(idCliente !=null){
            try{
                TypedQuery<Venta> q = em.createNamedQuery("Venta.findByIdCliente", Venta.class);
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

    //buscar por estado
    List<Venta> findByEstado(Boolean estado, int first, int max) {
        if(estado !=null){
            try{
                TypedQuery<Venta> q = em.createNamedQuery("Venta.findByEstado", Venta.class);
                q.setParameter("estado", estado);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            }catch(Exception ex){
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    //buscar por fecha
    List<Venta> findByFecha(java.util.Date fecha, int first, int max) {
        if(fecha !=null){
            try{
                TypedQuery<Venta> q = em.createNamedQuery("Venta.findByFecha", Venta.class);
                q.setParameter("fecha", fecha);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            }catch(Exception ex){
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    //contar todos
    public int countAllVenta() {
        try {
            TypedQuery<Long> q = em.createNamedQuery("Venta.countAllVenta", Long.class);
            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception ex) {
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }

    //contar por estado
    public int countByEstado(Boolean estado) {
        if (estado != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("Venta.countByEstado", Long.class);
                q.setParameter("estado", estado);
                return ((Long) q.getSingleResult()).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

    //contar por fecha
    public int countByFecha(java.util.Date fecha) {
        if (fecha != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("Venta.countByFecha", Long.class);
                q.setParameter("fecha", fecha);
                return ((Long) q.getSingleResult()).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }
}
