package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.ProductoTipoProductoCaracteristica;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProductoCaracteristica;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class ProductoTipoProductoCaracteristicaDAO extends InventarioDefaultDataAccess<ProductoTipoProductoCaracteristica, Object> implements Serializable {
    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public ProductoTipoProductoCaracteristicaDAO() {
        super(ProductoTipoProductoCaracteristica.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    @Override
    protected Class<ProductoTipoProductoCaracteristica> getEntityClass() {
        return ProductoTipoProductoCaracteristica.class;
    }

    public List<ProductoTipoProductoCaracteristica> findByIdProducto(UUID idProducto) {
        if (idProducto == null) return new java.util.ArrayList<>();
        try {
            TypedQuery<ProductoTipoProductoCaracteristica> q =
                    em.createNamedQuery("ProductoTipoProductoCaracteristica.findByIdProducto", ProductoTipoProductoCaracteristica.class);
            q.setParameter("idProducto", idProducto);
            return q.getResultList();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al buscar las caracteristicas del producto", e);
        }
    }
    public List<ProductoTipoProductoCaracteristica> findByProductoTipoProductoId(UUID id, int first, int maxValue) {
        if (id == null) return new java.util.ArrayList<>();
        try {
            TypedQuery<ProductoTipoProductoCaracteristica> q =
                    em.createNamedQuery("ProductoTipoProductoCaracteristica.findByProductoTipoProductoId", ProductoTipoProductoCaracteristica.class);
            q.setParameter("idProductoTipoProducto", id);
            q.setFirstResult(first);
            q.setMaxResults(maxValue);
            return q.getResultList();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al buscar las caracteristicas del producto por tipo producto", e);
        }
    }

    public int deleteByProductoTipoProductoAndCaracteristica(UUID idPtpp, Long idTipoProductoCaracteristica) {
        try {
            javax.persistence.Query q =
                    (javax.persistence.Query) em.createNamedQuery("ProductoTipoProductoCaracteristica.removeByProductoTipoProductoAndCaracteristica");
            q.setParameter("idPtpp", idPtpp);
            q.setParameter("idCar", idTipoProductoCaracteristica);
            return q.executeUpdate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al eliminar asignacion", e);
        }
    }
    public boolean exists(UUID id) {
        if (id == null) return false;
        return em.find(ProductoTipoProductoCaracteristica.class, id) != null;
    }
}
