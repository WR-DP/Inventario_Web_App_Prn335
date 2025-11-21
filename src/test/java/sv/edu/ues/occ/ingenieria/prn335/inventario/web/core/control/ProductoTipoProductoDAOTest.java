package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.ProductoTipoProducto;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProductoTipoProductoDAOTest {

    static {
        java.util.logging.Logger root = java.util.logging.Logger.getLogger("");
        root.setLevel(java.util.logging.Level.OFF);
    }

    private EntityManager emMock;
    private TypedQuery<ProductoTipoProducto> queryMock;
    private TypedQuery<Long> queryLongMock;

    private ProductoTipoProductoDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        emMock = Mockito.mock(EntityManager.class);
        queryMock = Mockito.mock(TypedQuery.class);
        queryLongMock = Mockito.mock(TypedQuery.class);

        dao = new ProductoTipoProductoDAO();

        // Inyección vía reflexión
        var field = ProductoTipoProductoDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(dao, emMock);
    }

    // -------------------------------------------------------------------------
    // TEST findByidProducto() válido
    // -------------------------------------------------------------------------
    @Test
    void testFindByidProductoValido() {

        UUID id = UUID.randomUUID();
        ProductoTipoProducto p = new ProductoTipoProducto();
        List<ProductoTipoProducto> resultado = List.of(p);

        when(emMock.createNamedQuery("ProductoTipoProducto.findByIdProducto", ProductoTipoProducto.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter("idProducto", id)).thenReturn(queryMock);
        when(queryMock.setFirstResult(0)).thenReturn(queryMock);
        when(queryMock.setMaxResults(10)).thenReturn(queryMock);
        when(queryMock.getResultList()).thenReturn(resultado);

        List<ProductoTipoProducto> lista = dao.findByidProducto(id, 0, 10);

        assertEquals(1, lista.size());
        assertSame(p, lista.get(0));
    }

    // -------------------------------------------------------------------------
    // TEST findByidProducto() id null → lista vacía
    // -------------------------------------------------------------------------
    @Test
    void testFindByidProductoNull() {

        List<ProductoTipoProducto> lista = dao.findByidProducto(null, 0, 10);

        assertTrue(lista.isEmpty());
        verify(emMock, never()).createNamedQuery(any(), any());
    }

    // -------------------------------------------------------------------------
    // TEST findByidProducto() excepción → lista vacía
    // -------------------------------------------------------------------------
    @Test
    void testFindByidProductoException() {

        UUID id = UUID.randomUUID();

        when(emMock.createNamedQuery("ProductoTipoProducto.findByIdProducto", ProductoTipoProducto.class))
                .thenThrow(new RuntimeException("DB error"));

        List<ProductoTipoProducto> lista = dao.findByidProducto(id, 0, 10);

        assertTrue(lista.isEmpty());
    }

    // -------------------------------------------------------------------------
    // TEST countByidProducto() válido
    // -------------------------------------------------------------------------
    @Test
    void testCountByidProductoValido() {

        UUID id = UUID.randomUUID();

        when(emMock.createNamedQuery("ProductoTipoProducto.countByIdProducto", Long.class))
                .thenReturn(queryLongMock);
        when(queryLongMock.setParameter("idProducto", id)).thenReturn(queryLongMock);
        when(queryLongMock.getSingleResult()).thenReturn(5L);

        Long count = dao.countByidProducto(id);

        assertEquals(5L, count);
    }

    // -------------------------------------------------------------------------
    // TEST countByidProducto() excepción → retorna 0
    // -------------------------------------------------------------------------
    @Test
    void testCountByidProductoException() {

        UUID id = UUID.randomUUID();

        when(emMock.createNamedQuery("ProductoTipoProducto.countByIdProducto", Long.class))
                .thenThrow(new RuntimeException("DB error"));

        Long count = dao.countByidProducto(id);

        assertEquals(0L, count);
    }

    // -------------------------------------------------------------------------
    // TEST countByidProducto() null → retorna 0
    // -------------------------------------------------------------------------
    @Test
    void testCountByidProductoNull() {

        Long count = dao.countByidProducto(null);

        assertEquals(0L, count);
        verify(emMock, never()).createNamedQuery(any(), any());
    }
}
