package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteManagerTest {

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<Long> longQuery;

    @Mock
    TypedQuery<UUID> uuidQuery;

    @Mock
    Query query;

    @InjectMocks
    DeleteManager deleteManager;

    // ===================== CLIENTE - VENTA =====================

    @Test
    void testContarVentasDeCliente() {
        UUID idCliente = UUID.randomUUID();

        when(em.createQuery(
                "SELECT COUNT(v) FROM Venta v WHERE v.idCliente.id = :id",
                Long.class)).thenReturn(longQuery);
        when(longQuery.setParameter("id", idCliente)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(5L);

        int result = deleteManager.contarVentasDeCliente(idCliente);

        assertEquals(5, result);
        verify(longQuery).setParameter("id", idCliente);
    }

    @Test
    void testContarDetallesDeVentaCliente() {
        UUID idCliente = UUID.randomUUID();

        when(em.createQuery(
                "SELECT COUNT(vd) FROM VentaDetalle vd WHERE vd.idVenta.id IN " +
                        "(SELECT v.id FROM Venta v WHERE v.idCliente.id = :id)",
                Long.class)).thenReturn(longQuery);
        when(longQuery.setParameter("id", idCliente)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(3L);

        int result = deleteManager.contarDetallesDeVentaCliente(idCliente);

        assertEquals(3, result);
    }

    @Test
    void testContarKardexDeCliente() {
        UUID idCliente = UUID.randomUUID();

        when(em.createQuery(
                "SELECT COUNT(k) FROM Kardex k WHERE k.idVentaDetalle.id IN " +
                        "(SELECT vd.id FROM VentaDetalle vd WHERE vd.idVenta.id IN " +
                        "(SELECT v.id FROM Venta v WHERE v.idCliente.id = :id))",
                Long.class)).thenReturn(longQuery);
        when(longQuery.setParameter("id", idCliente)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(2L);

        int result = deleteManager.contarKardexDeCliente(idCliente);

        assertEquals(2, result);
    }

    @Test
    void testEliminarVentasDeCliente() {
        UUID idCliente = UUID.randomUUID();
        UUID v1 = UUID.randomUUID();
        UUID v2 = UUID.randomUUID();

        when(em.createQuery(
                "SELECT v.id FROM Venta v WHERE v.idCliente.id = :id",
                UUID.class)).thenReturn(uuidQuery);
        when(uuidQuery.setParameter("id", idCliente)).thenReturn(uuidQuery);
        when(uuidQuery.getResultList()).thenReturn(List.of(v1, v2));

        when(em.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);

        deleteManager.eliminarVentasDeCliente(idCliente);

        // una selección + varios deletes
        verify(em).createQuery("SELECT v.id FROM Venta v WHERE v.idCliente.id = :id", UUID.class);
        verify(query, atLeast(1)).executeUpdate();
    }

    // ===================== PRODUCTO =====================

    @Test
    void testContarRelacionesProducto() {
        UUID idProducto = UUID.randomUUID();

        when(em.createQuery(
                "SELECT COUNT(ptp) FROM ProductoTipoProducto ptp WHERE ptp.idProducto.id = :id",
                Long.class)).thenReturn(longQuery);
        when(longQuery.setParameter("id", idProducto)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(4L);

        int result = deleteManager.contarRelacionesProducto(idProducto);

        assertEquals(4, result);
    }

    @Test
    void testContarCaracteristicasDeProducto() {
        UUID idProducto = UUID.randomUUID();

        when(em.createQuery(
                "SELECT COUNT(pc) FROM ProductoTipoProductoCaracteristica pc " +
                        "WHERE pc.idProductoTipoProducto.idProducto.id = :id",
                Long.class)).thenReturn(longQuery);
        when(longQuery.setParameter("id", idProducto)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(7L);

        int result = deleteManager.contarCaracteristicasDeProducto(idProducto);

        assertEquals(7, result);
    }

    @Test
    void testEliminarRelacionesProducto() {
        UUID idProducto = UUID.randomUUID();

        when(em.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);

        deleteManager.eliminarRelacionesProducto(idProducto);

        // 4 deletes en cascada
        verify(query, times(4)).executeUpdate();
    }

    // ===================== TIPO_PRODUCTO =====================

    @Test
    void testContarProductosDeTipo() {
        Long idTipo = 10L;

        when(em.createQuery(
                "SELECT COUNT(ptp) FROM ProductoTipoProducto ptp WHERE ptp.idTipoProducto.id = :id",
                Long.class)).thenReturn(longQuery);
        when(longQuery.setParameter("id", idTipo)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(9L);

        int result = deleteManager.contarProductosDeTipo(idTipo);

        assertEquals(9, result);
    }

    @Test
    void testContarCaracteristicasDeTipo() {
        Long idTipo = 11L;

        when(em.createQuery(
                "SELECT COUNT(pc) FROM ProductoTipoProductoCaracteristica pc " +
                        "WHERE pc.idProductoTipoProducto.idTipoProducto.id = :id",
                Long.class)).thenReturn(longQuery);
        when(longQuery.setParameter("id", idTipo)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(6L);

        int result = deleteManager.contarCaracteristicasDeTipo(idTipo);

        assertEquals(6, result);
    }

    @Test
    void testEliminarTipoProductoEnCascada() {
        Long idTipo = 12L;

        when(em.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);

        deleteManager.eliminarTipoProductoEnCascada(idTipo);

        // 3 deletes
        verify(query, times(3)).executeUpdate();
    }

    // ===================== VENTA =====================

    @Test
    void testContarDetallesDeVenta() {
        UUID idVenta = UUID.randomUUID();

        when(em.createQuery(
                "SELECT COUNT(vd) FROM VentaDetalle vd WHERE vd.idVenta.id = :id",
                Long.class)).thenReturn(longQuery);
        when(longQuery.setParameter("id", idVenta)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(8L);

        int result = deleteManager.contarDetallesDeVenta(idVenta);

        assertEquals(8, result);
    }

    @Test
    void testContarKardexDeVenta() {
        UUID idVenta = UUID.randomUUID();

        when(em.createQuery(
                "SELECT COUNT(k) FROM Kardex k WHERE k.idVentaDetalle.id IN " +
                        "(SELECT vd.id FROM VentaDetalle vd WHERE vd.idVenta.id = :id)",
                Long.class)).thenReturn(longQuery);
        when(longQuery.setParameter("id", idVenta)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(2L);

        int result = deleteManager.contarKardexDeVenta(idVenta);

        assertEquals(2, result);
    }

    @Test
    void testEliminarVentaEnCascada() {
        UUID idVenta = UUID.randomUUID();

        when(em.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);

        deleteManager.eliminarVentaEnCascada(idVenta);

        // 3 deletes (kardex, detalles, venta)
        verify(query, times(3)).executeUpdate();
    }

    // ===================== CARACTERISTICA =====================

    @Test
    void testContarTiposConCaracteristica() {
        Integer idCarac = 1;

        when(em.createQuery(
                "SELECT COUNT(tpc) FROM TipoProductoCaracteristica tpc WHERE tpc.idCaracteristica.id = :id",
                Long.class)).thenReturn(longQuery);
        when(longQuery.setParameter("id", idCarac)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(4L);

        int result = deleteManager.contarTiposConCaracteristica(idCarac);

        assertEquals(4, result);
    }

    @Test
    void testContarProductosConCaracteristica() {
        Integer idCarac = 2;

        when(em.createQuery(
                "SELECT COUNT(pc) FROM ProductoTipoProductoCaracteristica pc " +
                        "WHERE pc.idTipoProductoCaracteristica.idCaracteristica.id = :id",
                Long.class)).thenReturn(longQuery);
        when(longQuery.setParameter("id", idCarac)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(10L);

        int result = deleteManager.contarProductosConCaracteristica(idCarac);

        assertEquals(10, result);
    }

    @Test
    void testEliminarCaracteristicaEnCascada() {
        Integer idCarac = 3;

        when(em.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);

        deleteManager.eliminarCaracteristicaEnCascada(idCarac);

        // 2 deletes
        verify(query, times(2)).executeUpdate();
    }
}
