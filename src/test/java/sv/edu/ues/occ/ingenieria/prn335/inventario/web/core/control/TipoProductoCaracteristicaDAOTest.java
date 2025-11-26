package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.ProductoTipoProductoCaracteristica;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProductoCaracteristica;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TipoProductoCaracteristicaDAOTest {

    private EntityManager emMock;
    private TipoProductoCaracteristicaDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        emMock = Mockito.mock(EntityManager.class);
        dao = new TipoProductoCaracteristicaDAO();

        // Inyección manual de EM
        var field = TipoProductoCaracteristicaDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(dao, emMock);
    }

    // -------------------------------------------------------------
    // findByIdTipoProducto
    // -------------------------------------------------------------
    @Test
    void testFindByIdTipoProducto() {
        TypedQuery<TipoProductoCaracteristica> qMock = Mockito.mock(TypedQuery.class);

        when(emMock.createNamedQuery("TipoProductoCaracteristica.findByIdTipoProducto",
                TipoProductoCaracteristica.class)).thenReturn(qMock);

        when(qMock.setParameter("idTipoProducto", 10L)).thenReturn(qMock);
        when(qMock.setFirstResult(0)).thenReturn(qMock);
        when(qMock.setMaxResults(5)).thenReturn(qMock);

        TipoProductoCaracteristica obj = new TipoProductoCaracteristica();
        when(qMock.getResultList()).thenReturn(List.of(obj));

        List<TipoProductoCaracteristica> resultado = dao.findByIdTipoProducto(10L, 0, 5);

        assertEquals(1, resultado.size());
        assertSame(obj, resultado.get(0));
    }

    @Test
    void testFindByIdTipoProductoNull() {
        List<TipoProductoCaracteristica> resultado = dao.findByIdTipoProducto(null, 0, 5);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testFindByIdTipoProductoException() {
        when(emMock.createNamedQuery(anyString(), eq(TipoProductoCaracteristica.class)))
                .thenThrow(new RuntimeException("DB Error"));

        List<TipoProductoCaracteristica> resultado = dao.findByIdTipoProducto(5L, 0, 5);

        assertTrue(resultado.isEmpty());
    }

    // -------------------------------------------------------------
    // countByIdTipoProducto
    // -------------------------------------------------------------
    @Test
    void testCountByIdTipoProducto() {
        TypedQuery<Long> qMock = Mockito.mock(TypedQuery.class);

        when(emMock.createNamedQuery("TipoProductoCaracteristica.countByIdTipoProducto", Long.class))
                .thenReturn(qMock);

        when(qMock.setParameter("idTipoProducto", 22L)).thenReturn(qMock);
        when(qMock.getSingleResult()).thenReturn(7L);

        Long resultado = dao.countByIdTipoProducto(22L);

        assertEquals(7L, resultado);
    }

    @Test
    void testCountByIdTipoProductoNull() {
        Long resultado = dao.countByIdTipoProducto(null);
        assertEquals(0L, resultado);
    }

    @Test
    void testCountByIdTipoProductoException() {
        when(emMock.createNamedQuery(anyString(), eq(Long.class)))
                .thenThrow(new RuntimeException("DB Error"));

        Long resultado = dao.countByIdTipoProducto(99L);

        assertEquals(0L, resultado);
    }

    // -------------------------------------------------------------
    // save()
    // -------------------------------------------------------------
    @Test
    void testSavePersist() {
        ProductoTipoProductoCaracteristica e = new ProductoTipoProductoCaracteristica();
        // ID null ⇒ debe persistir
        e.setId(null);

        ProductoTipoProductoCaracteristica resultado = dao.save(e);

        verify(emMock, times(1)).persist(e);
        assertSame(e, resultado);
    }

    @Test
    void testSaveMerge() {
        ProductoTipoProductoCaracteristica e = new ProductoTipoProductoCaracteristica();
        e.setId(java.util.UUID.randomUUID());

        when(emMock.merge(e)).thenReturn(e);

        ProductoTipoProductoCaracteristica resultado = dao.save(e);

        verify(emMock, times(1)).merge(e);
        assertSame(e, resultado);
    }

    @Test
    void testSaveException() {
        ProductoTipoProductoCaracteristica e = new ProductoTipoProductoCaracteristica();

        doThrow(new RuntimeException("Error")).when(emMock).persist(any());

        ProductoTipoProductoCaracteristica resultado = dao.save(e);

        assertNull(resultado);
    }

    // -------------------------------------------------------------
    // findById()
    // -------------------------------------------------------------
    @Test
    void testFindById() {
        TipoProductoCaracteristica obj = new TipoProductoCaracteristica();

        when(emMock.find(TipoProductoCaracteristica.class, 5L)).thenReturn(obj);

        TipoProductoCaracteristica result = dao.findById(5L);

        assertSame(obj, result);
    }

    @Test
    void testFindByIdNull() {
        assertNull(dao.findById(null));
    }

    @Test
    void testFindByIdException() {
        when(emMock.find(TipoProductoCaracteristica.class, 7L))
                .thenThrow(new RuntimeException("DB Error"));

        assertNull(dao.findById(7L));
    }

    // -------------------------------------------------------------
    // findObligatoriasByTipo
    // -------------------------------------------------------------
    @Test
    void testFindObligatoriasByTipo() {
        TypedQuery<TipoProductoCaracteristica> qMock = mock(TypedQuery.class);

        when(emMock.createNamedQuery("TipoProductoCaracteristica.findObligatoriasByTipo",
                TipoProductoCaracteristica.class)).thenReturn(qMock);

        TipoProductoCaracteristica t = new TipoProductoCaracteristica();
        when(qMock.setParameter("idTipo", 4L)).thenReturn(qMock);
        when(qMock.getResultList()).thenReturn(List.of(t));

        List<TipoProductoCaracteristica> resultado = dao.findObligatoriasByTipo(4L);

        assertEquals(1, resultado.size());
        assertSame(t, resultado.get(0));
    }

    @Test
    void testFindObligatoriasByTipoNull() {
        List<TipoProductoCaracteristica> r = dao.findObligatoriasByTipo(null);
        assertTrue(r.isEmpty());
    }

    @Test
    void testFindObligatoriasByTipoException() {
        when(emMock.createNamedQuery(anyString(), eq(TipoProductoCaracteristica.class)))
                .thenThrow(new RuntimeException("Error"));

        List<TipoProductoCaracteristica> r = dao.findObligatoriasByTipo(5L);

        assertTrue(r.isEmpty());
    }
}
