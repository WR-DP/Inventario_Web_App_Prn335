package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.KardexDetalle;

import java.io.Serializable;
import java.util.List;

@Stateless
@LocalBean
public class KardexDetalleDAO extends InventarioDefaultDataAccess<KardexDetalle, Object> implements Serializable {
    @PersistenceContext(unitName="InventarioPU")
    private EntityManager em;

    public KardexDetalleDAO() {
        super(KardexDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<KardexDetalle> getEntityClass() {
        return KardexDetalle.class;
    }

    //buscar por idKardexDetalle
    List<KardexDetalle> findByIdKardexDetalle(Integer idKardexDetalle, int first, int max) {
            if(idKardexDetalle !=null){
                try{
                    TypedQuery<KardexDetalle> q = em.createNamedQuery("KardexDetalle.findByIdKardexDetalle", KardexDetalle.class);
                    q.setParameter("idKardexDetalle", idKardexDetalle);
                    q.setFirstResult(first);
                    q.setMaxResults(max);
                    return q.getResultList();
                }catch(Exception ex){
                    throw new IllegalStateException("Parametro no valido", ex);
                }
            }
            return List.of();
    }

    //buscar todos
    List <KardexDetalle> findAllKardexDetalle(int first, int max) {
        try{
            TypedQuery<KardexDetalle> q = em.createNamedQuery("KardexDetalle.findAllKardexDetalle", KardexDetalle.class);
            q.setFirstResult(first);
            q.setMaxResults(max);
            return q.getResultList();
        }catch(Exception ex){
            throw new IllegalStateException("Parametro no valido", ex);
        }
    }
    //buscar por lote
    List <KardexDetalle> findByLote(String lote, int first, int max) {
        if(lote != null && !lote.isBlank()){
            try{
                TypedQuery<KardexDetalle> q = em.createNamedQuery("KardexDetalle.findByLote", KardexDetalle.class);
                q.setParameter("lote", "%" + lote + "%");
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            }catch(Exception ex){
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    //contar por idKardexDetalle
    public int countByIdKardexDetalle(Integer idKardexDetalle) {
        if (idKardexDetalle != null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("KardexDetalle.countByIdKardexDetalle", Long.class);
                q.setParameter("idKardexDetalle", idKardexDetalle);
                return q.getSingleResult().intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

    //contar todos
    public int countAllKardexDetalle() {
        try{
            TypedQuery<Long> q = em.createNamedQuery("KardexDetalle.countAllKardexDetalle", Long.class);
            return q.getSingleResult().intValue();
        } catch (Exception ex) {
            throw new IllegalArgumentException("El parametro no es valido",ex);
        }
    }

    //contar por lote
    public int countByLote(String lote) {
        if (lote != null && !lote.isBlank()) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("KardexDetalle.countByLote", Long.class);
                q.setParameter("lote", "%" + lote + "%");
                return q.getSingleResult().intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

}
