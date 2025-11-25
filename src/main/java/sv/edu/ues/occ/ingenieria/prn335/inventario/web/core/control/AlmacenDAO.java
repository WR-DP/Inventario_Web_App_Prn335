package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Almacen;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoAlmacen;

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
    public TipoAlmacen findTipoAlmacenById(Integer id){
        return em.find(TipoAlmacen.class, id);
    }


}

