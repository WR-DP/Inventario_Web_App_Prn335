package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Almacen;

import java.io.Serializable;
import java.util.List;

//Revisar esta clase, algo malo tiene xd

@Stateless
@LocalBean
public class AlmacenDAO extends InventarioDefaultDataAccess<Almacen, Object> implements Serializable {
    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public AlmacenDAO() {
        super(Almacen.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Almacen> getEntityClass() {
        return Almacen.class;
    }

    @Override
    public int count() throws IllegalStateException {
        return super.count();
    }

    public List<Almacen> findByIdAlmacen(Integer idAlmacen) {
        if (idAlmacen != null) {
            try {
                if (em != null) {
                    Query query = em.createNamedQuery("Almacen.findByIdAlmacen");
                    query.setParameter("id", idAlmacen);
                    return query.getResultList();
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Error al buscar el almacen por id");

            }
        }
        return List.of();
    }

    //la query de este metodo esta en la entidad Kardex, no en Almacen por la relacion que se tiene
    public List<Almacen> findAlmacenWithMostProductos() {
            try {
                Query query = em.createNamedQuery("Kardex.findAlmacenWithMostProductos");
                query.setMaxResults(1);
                return query.getResultList();
            } catch (Exception e) {
                throw new IllegalArgumentException("Error al buscar el almacen con mas productos");

            }
    }

    public List<Almacen> findAlmacenWithTipoAlmacen(Integer idTipoAlmacen) {
        if (idTipoAlmacen != null) {
            try {
                Query query = em.createNamedQuery("Almacen.findAlmacenWithTipoAlmacen");
                query.setParameter("idTipoAlmacen", idTipoAlmacen);
                return query.getResultList();
            } catch (Exception e) {
                throw new IllegalArgumentException("Error al buscar el almacen con tipo de almacen");

            }
        }
        return List.of();
    }

    public List<Almacen> findByIdTipoAlmacen(Integer idTipoAlmacen, int firts, int max) {
        if (idTipoAlmacen != null) {
            try {
                if (em != null) {
                    Query query = em.createNamedQuery("Almacen.findAlmacenWithTipoAlmacen");
                    query.setParameter("idTipoAlmacen", idTipoAlmacen);
                    query.setFirstResult(firts);
                    query.setMaxResults(max);
                    return query.getResultList();
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Error al buscar el almacen con tipo de almacen");
            }
        }
        return List.of();
    }

    public List<Almacen> findAllAlmacen(int first, int max) {
            try {
                TypedQuery<Almacen> q = em.createNamedQuery("Almacen.findAll", Almacen.class);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception e) {
                throw new IllegalArgumentException("Error al buscar todos los almacenes");
            }
    }
}

