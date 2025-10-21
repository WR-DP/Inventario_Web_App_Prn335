package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Caracteristica;

import java.io.Serializable;

@Stateless
@LocalBean
public class CaracteristicaDAO extends InventarioDefaultDataAccess<Caracteristica, Object> implements Serializable {
    @PersistenceContext(unitName = "InventarioPU")
    private EntityManager em;

    public CaracteristicaDAO() {
        super(Caracteristica.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<Caracteristica> getEntityClass() {
        return Caracteristica.class;
    }


}
