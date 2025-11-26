package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class VentaDAO extends InventarioDefaultDataAccess<Venta, Object> implements Serializable {

    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public VentaDAO() {
        super(Venta.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Venta> getEntityClass() {
        return Venta.class;
    }

    @Override
    public int count() throws IllegalStateException {
        return super.count();
    }

    public Cliente findClienteById(UUID idCliente) {
        return em.find(Cliente.class, idCliente);
    }

    public List<Venta> buscarVentasParaDespacho(int first, int max) {
        TypedQuery<Venta> q = em.createQuery(
                "SELECT v FROM Venta v WHERE v.estado = 'ACTIVA'",
                Venta.class
        );
        q.setFirstResult(first);
        q.setMaxResults(max);
        return q.getResultList();
    }

    public Long contarVentasParaDespacho() {
        return em.createQuery(
                "SELECT COUNT(v) FROM Venta v WHERE v.estado = 'ACTIVA'",
                Long.class
        ).getSingleResult();
    }

}