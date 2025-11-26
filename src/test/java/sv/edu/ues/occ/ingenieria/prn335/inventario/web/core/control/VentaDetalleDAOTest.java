package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.VentaDetalle;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaDetalleDAOTest {

    private EntityManager emMock;
    private TypedQuery<VentaDetalle> queryMockVD;
    private TypedQuery<Long> queryMockLong;
    private VentaDetalleDAO dao;

    @BeforeEach
    void setUp() throws Exception {

        emMock = Mockito.mock(EntityManager.class);
        queryMockVD = Mockito.mock(TypedQuery.class);
        queryMockLong = Mockito.mock(TypedQuery.class);

        dao = new VentaDetalleDAO();

        // Inyectar EntityManager
        var field = VentaDetalleDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(dao, emMock);
    }

    // ========================================================================
    // findByIdVenta()
    // ========================================================================
    @Test
    void testFindByIdVenta_OK() {
        UUID id = UUID.randomUUID();
        VentaDetalle vd = new VentaDetalle();
        List<VentaDetalle> lista = List.of(vd);

        when(emMock.createNamedQuery("VentaDetalle.findByIdVenta", VentaDetalle.class))
                .thenReturn(queryMockVD);
        when(queryMockVD.setParameter("idVenta", id)).thenReturn(queryMockVD);
        when(queryMockVD.setFirstResult(0)).thenReturn(queryMockVD);
        when(queryMockVD.setMaxResults(10)).thenReturn(queryMockVD);
        when(queryMockVD.getResultList()).thenReturn(lista);

        List<VentaDetalle> result = dao.findByIdVenta(id, 0, 10);

        assertEquals(1, result.size());
        assertSame(vd, result.get(0));
    }

    @Test
    void testFindByIdVenta_idNull() {
        List<VentaDetalle> result = dao.findByIdVenta(null, 0, 10);

        assertTrue(result.isEmpty());
        verify(emMock, never()).createNamedQuery(any(), eq(VentaDetalle.class));
    }

    @Test
    void testFindByIdVenta_Exception() {
        UUID id = UUID.randomUUID();

        when(emMock.createNamedQuery("VentaDetalle.findByIdVenta", VentaDetalle.class))
                .thenThrow(new RuntimeException("ERR"));

        assertThrows(IllegalStateException.class,
                () -> dao.findByIdVenta(id, 0, 10));
    }

    // ========================================================================
    // countByIdVenta()
    // ========================================================================
    @Test
    void testCountByIdVenta_OK() {
        UUID id = UUID.randomUUID();

        when(emMock.createNamedQuery("VentaDetalle.countByIdVenta", Long.class))
                .thenReturn(queryMockLong);
        when(queryMockLong.setParameter("idVenta", id))
                .thenReturn(queryMockLong);
        when(queryMockLong.getSingleResult())
                .thenReturn(5L);

        int count = dao.countByIdVenta(id);

        assertEquals(5, count);
    }

    @Test
    void testCountByIdVenta_idNull() {
        int result = dao.countByIdVenta(null);

        assertEquals(0, result);
        verify(emMock, never()).createNamedQuery(any(), eq(Long.class));
    }

    @Test
    void testCountByIdVenta_Exception() {
        UUID id = UUID.randomUUID();
        when(emMock.createNamedQuery("VentaDetalle.countByIdVenta", Long.class))
                .thenThrow(new RuntimeException("ERR"));

        assertThrows(IllegalStateException.class,
                () -> dao.countByIdVenta(id));
    }

    // ========================================================================
    // calcularMontoTotal()
    // ========================================================================
    @Test
    void testCalcularMontoTotal_valoresCorrectos() {

        VentaDetalle d1 = new VentaDetalle();
        d1.setCantidad(new BigDecimal("2"));
        d1.setPrecio(new BigDecimal("10.50"));

        VentaDetalle d2 = new VentaDetalle();
        d2.setCantidad(new BigDecimal("3"));
        d2.setPrecio(new BigDecimal("5.00"));

        List<VentaDetalle> detalles = List.of(d1, d2);

        BigDecimal esperado = new BigDecimal("21.00")
                .add(new BigDecimal("15.00")); // 36.00

        BigDecimal result = dao.calcularMontoTotal(detalles);

        assertEquals(0, esperado.compareTo(result));   // CORRECTO
    }


    @Test
    void testCalcularMontoTotal_listaNull() {
        assertEquals(BigDecimal.ZERO, dao.calcularMontoTotal(null));
    }

    @Test
    void testCalcularMontoTotal_listaVacia() {
        assertEquals(BigDecimal.ZERO, dao.calcularMontoTotal(List.of()));
    }

    @Test
    void testCalcularMontoTotal_cantidadInvalida() {
        VentaDetalle d = new VentaDetalle();
        d.setCantidad(null);
        d.setPrecio(new BigDecimal("10"));

        BigDecimal result = dao.calcularMontoTotal(List.of(d));

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void testCalcularMontoTotal_precioInvalido() {
        VentaDetalle d = new VentaDetalle();
        d.setCantidad(new BigDecimal("2"));
        d.setPrecio(null);

        BigDecimal result = dao.calcularMontoTotal(List.of(d));

        assertEquals(0, BigDecimal.ZERO.compareTo(result));   // CORRECTO
    }


    @Test
    void testCalcularMontoTotal_excepcionEnIteracion() {
        List<VentaDetalle> mockList = Mockito.mock(List.class);
        when(mockList.isEmpty()).thenReturn(false);
        when(mockList.iterator()).thenThrow(new RuntimeException("ERR"));

        BigDecimal result = dao.calcularMontoTotal(mockList);

        assertEquals(BigDecimal.ZERO, result);
    }
}
