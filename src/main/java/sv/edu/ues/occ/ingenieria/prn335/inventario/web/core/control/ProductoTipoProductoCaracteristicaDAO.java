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


    public List<ProductoTipoProductoCaracteristica> findByIdProducto(Integer idProducto) {
        if(idProducto != null){
            try {
                TypedQuery <ProductoTipoProductoCaracteristica> q = em.createNamedQuery("ProductoTipoProductoCaracteristica.findByIdProducto", ProductoTipoProductoCaracteristica.class);
                q.setParameter("idProducto", idProducto);
                return q.getResultList();
            } catch (Exception e) {
                throw new IllegalArgumentException("Error al buscar las caracteristicas del producto");
            }
        }
        return List.of();
    }

    public List<TipoProductoCaracteristica> findByProductoTipoProductoId(UUID id, int i, int maxValue) {
        if(id != null){
            try {
                TypedQuery <TipoProductoCaracteristica> q = em.createNamedQuery("ProductoTipoProductoCaracteristica.findByProductoTipoProductoId", TipoProductoCaracteristica.class);
                q.setParameter("idProductoTipoProducto", id);
                q.setFirstResult(i);
                q.setMaxResults(maxValue);
                return q.getResultList();
            } catch (Exception e) {
                throw new IllegalArgumentException("Error al buscar las caracteristicas del producto por tipo producto");
            }
        }
        return List.of();
    }

    //metodo para buscar por el id del padre


}
