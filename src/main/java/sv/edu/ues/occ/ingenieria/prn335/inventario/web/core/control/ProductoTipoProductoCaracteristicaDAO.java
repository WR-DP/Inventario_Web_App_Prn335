package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.ProductoTipoProductoCaracteristica;

import java.io.Serializable;
import java.util.List;

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

    // Buscar las caracteristicas de un producto por su idProducto, pero en realidad no le siento utilidad real a este metodo, pero lo vamos a dejar de base por si acaso
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
}
