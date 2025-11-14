package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Stateless
public class DeleteManager {

    @PersistenceContext(unitName="InventarioPU")
    private EntityManager em;

    // ======================================================================
    // ===== CLIENTE → VENTA → VENTA_DETALLE → KARDEX =======================
    // ======================================================================

    public int contarVentasDeCliente(UUID idCliente) {
        return em.createQuery(
                        "SELECT COUNT(v) FROM Venta v WHERE v.idCliente.id = :id",
                        Long.class
                )
                .setParameter("id", idCliente)
                .getSingleResult()
                .intValue();
    }

    public int contarDetallesDeVentaCliente(UUID idCliente) {
        return em.createQuery(
                        "SELECT COUNT(vd) FROM VentaDetalle vd WHERE vd.idVenta.id IN " +
                                "(SELECT v.id FROM Venta v WHERE v.idCliente.id = :id)",
                        Long.class
                )
                .setParameter("id", idCliente)
                .getSingleResult()
                .intValue();
    }

    public int contarKardexDeCliente(UUID idCliente) {
        return em.createQuery(
                        "SELECT COUNT(k) FROM Kardex k WHERE k.idVentaDetalle.id IN " +
                                "(SELECT vd.id FROM VentaDetalle vd WHERE vd.idVenta.id IN " +
                                "(SELECT v.id FROM Venta v WHERE v.idCliente.id = :id))",
                        Long.class
                )
                .setParameter("id", idCliente)
                .getSingleResult()
                .intValue();
    }


    public void eliminarVentasDeCliente(UUID idCliente) {

        // 1. obtener ventas
        List<UUID> ventas = em.createQuery(
                        "SELECT v.id FROM Venta v WHERE v.idCliente.id = :id",
                        UUID.class
                )
                .setParameter("id", idCliente)
                .getResultList();

        for (UUID idVenta : ventas) {

            // 2. eliminar kardex
            em.createQuery(
                            "DELETE FROM Kardex k WHERE k.idVentaDetalle.id IN " +
                                    "(SELECT vd.id FROM VentaDetalle vd WHERE vd.idVenta.id = :id)"
                    )
                    .setParameter("id", idVenta)
                    .executeUpdate();

            // 3. eliminar detalles
            em.createQuery(
                            "DELETE FROM VentaDetalle vd WHERE vd.idVenta.id = :id"
                    )
                    .setParameter("id", idVenta)
                    .executeUpdate();
        }

        // 4. eliminar ventas
        em.createQuery(
                        "DELETE FROM Venta v WHERE v.idCliente.id = :id"
                )
                .setParameter("id", idCliente)
                .executeUpdate();
    }



    // ======================================================================
    // ===== PRODUCTO → PRODUCTO_TIPO_PRODUCTO → PRO_TIPO_PROD_CARAC → VENTA_DETALLE → KARDEX
    // ======================================================================

    public int contarRelacionesProducto(UUID idProducto) {
        return em.createQuery(
                        "SELECT COUNT(ptp) FROM ProductoTipoProducto ptp WHERE ptp.idProducto.id = :id",
                        Long.class
                )
                .setParameter("id", idProducto)
                .getSingleResult()
                .intValue();
    }

    public int contarCaracteristicasDeProducto(UUID idProducto) {
        return em.createQuery(
                        "SELECT COUNT(pc) FROM ProductoTipoProductoCaracteristica pc " +
                                "WHERE pc.idProductoTipoProducto.idProducto.id = :id",
                        Long.class
                )
                .setParameter("id", idProducto)
                .getSingleResult()
                .intValue();
    }

    public void eliminarRelacionesProducto(UUID idProducto) {

        // 1. características del producto (nietos)
        em.createQuery(
                        "DELETE FROM ProductoTipoProductoCaracteristica pc " +
                                "WHERE pc.idProductoTipoProducto.idProducto.id = :id"
                )
                .setParameter("id", idProducto)
                .executeUpdate();

        // 2. tipos del producto (hijos)
        em.createQuery(
                        "DELETE FROM ProductoTipoProducto ptp WHERE ptp.idProducto.id = :id"
                )
                .setParameter("id", idProducto)
                .executeUpdate();

        // 3. detalles de venta del producto
        em.createQuery(
                        "DELETE FROM VentaDetalle vd WHERE vd.idProducto.id = :id"
                )
                .setParameter("id", idProducto)
                .executeUpdate();

        // 4. kardex del producto
        em.createQuery(
                        "DELETE FROM Kardex k WHERE k.idProducto.id = :id"
                )
                .setParameter("id", idProducto)
                .executeUpdate();
    }



    // ======================================================================
    // ===== TIPO_PRODUCTO → PRODUCTO_TIPO_PRODUCTO → PRO_TIPO_PROD_CARAC → TIPO_PRODUCTO_CARACTERISTICA
    // ======================================================================

    public int contarProductosDeTipo(Long idTipo) {
        return em.createQuery(
                        "SELECT COUNT(ptp) FROM ProductoTipoProducto ptp WHERE ptp.idTipoProducto.id = :id",
                        Long.class
                )
                .setParameter("id", idTipo)
                .getSingleResult()
                .intValue();
    }

    public int contarCaracteristicasDeTipo(Long idTipo) {
        return em.createQuery(
                        "SELECT COUNT(pc) FROM ProductoTipoProductoCaracteristica pc " +
                                "WHERE pc.idProductoTipoProducto.idTipoProducto.id = :id",
                        Long.class
                )
                .setParameter("id", idTipo)
                .getSingleResult()
                .intValue();
    }

    public void eliminarTipoProductoEnCascada(Long idTipo) {

        // 1. eliminar características en productos
        em.createQuery(
                        "DELETE FROM ProductoTipoProductoCaracteristica pc " +
                                "WHERE pc.idProductoTipoProducto.idTipoProducto.id = :id"
                )
                .setParameter("id", idTipo)
                .executeUpdate();

        // 2. eliminar relaciones producto-tipo
        em.createQuery(
                        "DELETE FROM ProductoTipoProducto ptp WHERE ptp.idTipoProducto.id = :id"
                )
                .setParameter("id", idTipo)
                .executeUpdate();

        // 3. eliminar configuraciones del tipo
        em.createQuery(
                        "DELETE FROM TipoProductoCaracteristica tpc WHERE tpc.idTipoProducto.id = :id"
                )
                .setParameter("id", idTipo)
                .executeUpdate();
    }



    // ======================================================================
    // ===== VENTA → VENTA_DETALLE → KARDEX =================================
    // ======================================================================

    public int contarDetallesDeVenta(UUID idVenta) {
        return em.createQuery(
                        "SELECT COUNT(vd) FROM VentaDetalle vd WHERE vd.idVenta.id = :id",
                        Long.class
                )
                .setParameter("id", idVenta)
                .getSingleResult()
                .intValue();
    }

    public int contarKardexDeVenta(UUID idVenta) {
        return em.createQuery(
                        "SELECT COUNT(k) FROM Kardex k WHERE k.idVentaDetalle.id IN " +
                                "(SELECT vd.id FROM VentaDetalle vd WHERE vd.idVenta.id = :id)",
                        Long.class
                )
                .setParameter("id", idVenta)
                .getSingleResult()
                .intValue();
    }

    public void eliminarVentaEnCascada(UUID idVenta) {

        // 1. kardex
        em.createQuery(
                        "DELETE FROM Kardex k WHERE k.idVentaDetalle.id IN " +
                                "(SELECT vd.id FROM VentaDetalle vd WHERE vd.idVenta.id = :id)"
                )
                .setParameter("id", idVenta)
                .executeUpdate();

        // 2. detalles
        em.createQuery(
                        "DELETE FROM VentaDetalle vd WHERE vd.idVenta.id = :id"
                )
                .setParameter("id", idVenta)
                .executeUpdate();

        // 3. venta
        em.createQuery(
                        "DELETE FROM Venta v WHERE v.id = :id"
                )
                .setParameter("id", idVenta)
                .executeUpdate();
    }



    // ======================================================================
    // ===== CARACTERISTICA → TIPO_PRODUCTO_CARACTERISTICA → PRO_TIPO_PROD_CARAC
    // ======================================================================

    public int contarTiposConCaracteristica(Integer idCaracteristica) {
        return em.createQuery(
                        "SELECT COUNT(tpc) FROM TipoProductoCaracteristica tpc WHERE tpc.idCaracteristica.id = :id",
                        Long.class
                )
                .setParameter("id", idCaracteristica)
                .getSingleResult()
                .intValue();
    }

    public int contarProductosConCaracteristica(Integer idCaracteristica) {
        return em.createQuery(
                        "SELECT COUNT(pc) FROM ProductoTipoProductoCaracteristica pc " +
                                "WHERE pc.idTipoProductoCaracteristica.idCaracteristica.id = :id",
                        Long.class
                )
                .setParameter("id", idCaracteristica)
                .getSingleResult()
                .intValue();
    }

    public void eliminarCaracteristicaEnCascada(Integer idCaracteristica) {

        // 1. eliminar en productos
        em.createQuery(
                        "DELETE FROM ProductoTipoProductoCaracteristica pc " +
                                "WHERE pc.idTipoProductoCaracteristica.idCaracteristica.id = :id"
                )
                .setParameter("id", idCaracteristica)
                .executeUpdate();

        // 2. eliminar en tipos de producto
        em.createQuery(
                        "DELETE FROM TipoProductoCaracteristica tpc " +
                                "WHERE tpc.idCaracteristica.id = :id"
                )
                .setParameter("id", idCaracteristica)
                .executeUpdate();
    }

}
