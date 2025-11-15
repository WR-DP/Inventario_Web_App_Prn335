package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf.CompraFrm;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Stateless
@LocalBean
public class CompraDetalleDAO extends InventarioDefaultDataAccess<CompraDetalle, Object> implements Serializable {
    @PersistenceContext(unitName="InventarioPU")
    private EntityManager em;

    public CompraDetalleDAO() {
        super(CompraDetalle.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Class<CompraDetalle> getEntityClass() {
        return CompraDetalle.class;
    }

    //buscar por idCompra
    public List<CompraDetalle> findByIdCompra(Long idCompra, int first, int max) {
        if (idCompra != null) {
            try {
                TypedQuery<CompraDetalle> q = em.createNamedQuery("CompraDetalle.findByIdCompra", CompraDetalle.class);
                q.setParameter("idCompra", idCompra);
                q.setFirstResult(first);
                q.setMaxResults(max);
                return q.getResultList();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return List.of();
    }

    //contar por idCompra
    public int countByIdCompra(Long idCompra) {
        if (idCompra!= null) {
            try {
                TypedQuery<Long> q = em.createNamedQuery("CompraDetalle.countByIdCompra", Long.class);
                q.setParameter("idCompra", idCompra);
                return ((Long) q.getSingleResult()).intValue();
            } catch (Exception ex) {
                throw new IllegalStateException("Parametro no valido", ex);
            }
        }
        return 0;
    }

    public java.math.BigDecimal calcularMontoTotal(java.util.List<sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle> detalles) {
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        if (detalles == null || detalles.isEmpty()) return total;
        try {
            for (sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle d : detalles) {
                java.math.BigDecimal cantidadBd = java.math.BigDecimal.ZERO;
                try {
                    Object c = d.getCantidad();
                    if (c instanceof Number) {
                        cantidadBd = java.math.BigDecimal.valueOf(((Number) c).doubleValue());
                    } else {
                        cantidadBd = new java.math.BigDecimal(c == null ? "0" : c.toString());
                    }
                } catch (Exception ex) {
                    cantidadBd = java.math.BigDecimal.ZERO;
                }

                java.math.BigDecimal precioBd = java.math.BigDecimal.ZERO;
                try {
                    Object p = d.getPrecio();
                    if (p != null) {
                        precioBd = (java.math.BigDecimal) p;
                    } else if (p instanceof Number) {
                        precioBd = java.math.BigDecimal.valueOf(((Number) p).doubleValue());
                    } else {
                        precioBd = new java.math.BigDecimal(p == null ? "0" : p.toString());
                    }
                } catch (Exception ex) {
                    precioBd = java.math.BigDecimal.ZERO;
                }

                total = total.add(precioBd.multiply(cantidadBd));
            }
        } catch (Exception ex) {
            Logger.getLogger(CompraFrm.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return total;
    }

}