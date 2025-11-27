package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraDetalleDAOTest {

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<CompraDetalle> queryCompraDetalle;

    @Mock
    TypedQuery<Long> queryLong;

    @InjectMocks
    CompraDetalleDAO dao;

    @Test
    void testGetEntityManager() {
        assertSame(em, dao.getEntityManager());
    }

    @Test
    void testGetEntityClass() {
        assertEquals(CompraDetalle.class, dao.getEntityClass());
    }

    @Test
    void testFindByIdCompra_ok() {
        Long idCompra = 1L;
        List<CompraDetalle> lista = List.of(new CompraDetalle());

        when(em.createNamedQuery("CompraDetalle.findByIdCompra", CompraDetalle.class))
                .thenReturn(queryCompraDetalle);
        when(queryCompraDetalle.setParameter(eq("idCompra"), any()))
                .thenReturn(queryCompraDetalle);
        when(queryCompraDetalle.setFirstResult(0)).thenReturn(queryCompraDetalle);
        when(queryCompraDetalle.setMaxResults(10)).thenReturn(queryCompraDetalle);
        when(queryCompraDetalle.getResultList()).thenReturn(lista);

        List<CompraDetalle> result = dao.findByIdCompra(idCompra, 0, 10);

        assertEquals(lista, result);
        verify(queryCompraDetalle).setParameter("idCompra", idCompra);
        verify(queryCompraDetalle).setFirstResult(0);
        verify(queryCompraDetalle).setMaxResults(10);
    }

    @Test
    void testFindByIdCompra_idNullDevuelveListaVacia() {
        List<CompraDetalle> result = dao.findByIdCompra(null, 0, 10);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(em);
    }

    @Test
    void testFindByIdCompra_excepcionEnQueryLanzaIllegalState() {
        when(em.createNamedQuery("CompraDetalle.findByIdCompra", CompraDetalle.class))
                .thenThrow(new RuntimeException("boom"));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> dao.findByIdCompra(1L, 0, 10)
        );
        assertEquals("Parametro no valido", ex.getMessage());
    }

    @Test
    void testCountByIdCompra_ok() {
        Long idCompra = 1L;

        when(em.createNamedQuery("CompraDetalle.countByIdCompra", Long.class))
                .thenReturn(queryLong);
        when(queryLong.setParameter(eq("idCompra"), any()))
                .thenReturn(queryLong);
        when(queryLong.getSingleResult()).thenReturn(5L);

        int result = dao.countByIdCompra(idCompra);

        assertEquals(5, result);
        verify(queryLong).setParameter("idCompra", idCompra);
    }

    @Test
    void testCountByIdCompra_idNullDevuelveCero() {
        int result = dao.countByIdCompra(null);
        assertEquals(0, result);
        verifyNoInteractions(em);
    }

    @Test
    void testCountByIdCompra_excepcionEnQueryLanzaIllegalState() {
        when(em.createNamedQuery("CompraDetalle.countByIdCompra", Long.class))
                .thenThrow(new RuntimeException("boom"));

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> dao.countByIdCompra(1L)
        );
        assertEquals("Parametro no valido", ex.getMessage());
    }

    @Test
    void testCalcularMontoTotal_listaNullOCiaVaciaDevuelveCero() {
        assertEquals(BigDecimal.ZERO, dao.calcularMontoTotal(null));
        assertEquals(BigDecimal.ZERO, dao.calcularMontoTotal(List.of()));
    }


    @Test
    void testCalcularMontoTotal_valoresCorrectos() {
        CompraDetalle d1 = mock(CompraDetalle.class);
        when(d1.getCantidad()).thenReturn(new BigDecimal("2"));
        when(d1.getPrecio()).thenReturn(new BigDecimal("10.50"));

        BigDecimal total = dao.calcularMontoTotal(List.of(d1));

        BigDecimal esperado = new BigDecimal("21.00");
        assertEquals(0, esperado.compareTo(total));
    }


    @Test
    void testCalcularMontoTotal_cantidadNullUsaCero() {
        CompraDetalle d1 = mock(CompraDetalle.class);
        when(d1.getCantidad()).thenReturn(null); // inválido
        when(d1.getPrecio()).thenReturn(new BigDecimal("10"));

        BigDecimal total = dao.calcularMontoTotal(List.of(d1));

        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void testCalcularMontoTotal_precioNullUsaCero() {
        CompraDetalle d1 = mock(CompraDetalle.class);
        when(d1.getCantidad()).thenReturn(new BigDecimal("5"));
        when(d1.getPrecio()).thenReturn(null);

        BigDecimal total = dao.calcularMontoTotal(List.of(d1));

        assertEquals(0, total.compareTo(BigDecimal.ZERO));
    }


    @Test
    void testCalcularMontoTotal_cantidadLanzaExcepcionUsaCero() {
        CompraDetalle d1 = mock(CompraDetalle.class);

        when(d1.getCantidad()).thenThrow(new RuntimeException("error"));
        when(d1.getPrecio()).thenReturn(new BigDecimal("10"));

        BigDecimal total = dao.calcularMontoTotal(List.of(d1));

        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    void testCalcularMontoTotal_precioLanzaExcepcionUsaCero() {
        CompraDetalle d1 = mock(CompraDetalle.class);
        when(d1.getCantidad()).thenReturn(new BigDecimal("5"));
        when(d1.getPrecio()).thenThrow(new RuntimeException("error"));

        BigDecimal total = dao.calcularMontoTotal(List.of(d1));

        assertEquals(0, total.compareTo(BigDecimal.ZERO));
    }


    @Test
    void testCalcularMontoTotal_excepcionEnIteracionCapturadaPorTryExterno() {
        @SuppressWarnings("unchecked")
        List<CompraDetalle> detalles = mock(List.class);

        when(detalles.isEmpty()).thenReturn(false);
        when(detalles.iterator()).thenThrow(new RuntimeException("fallo iterador"));

        BigDecimal total = dao.calcularMontoTotal(detalles);

        assertEquals(BigDecimal.ZERO, total);
    }
}
