package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoDAOTest {

    private EntityManager emMock;
    private TypedQuery<Producto> queryMock;
    private ProductoDAO dao;

    @BeforeEach
    void setUp() throws Exception {

        emMock = Mockito.mock(EntityManager.class);
        queryMock = Mockito.mock(TypedQuery.class);

        dao = new ProductoDAO();

        // Inyectar EntityManager via reflección (porque @PersistenceContext no lo pone Mockito)
        var field = ProductoDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(dao, emMock);
    }

    // =========================================================================
    // TESTS PARA buscarProductosPorNombre
    // =========================================================================

    @Test
    void testBuscarProductosPorNombreValido() {

        Producto p = new Producto();
        List<Producto> resultado = List.of(p);

        when(emMock.createNamedQuery("Producto.buscarProductosPorNombre", Producto.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter("nombreProducto", "%laptop%"))
                .thenReturn(queryMock);
        when(queryMock.setFirstResult(0))
                .thenReturn(queryMock);
        when(queryMock.setMaxResults(10))
                .thenReturn(queryMock);
        when(queryMock.getResultList())
                .thenReturn(resultado);

        List<Producto> lista = dao.buscarProductosPorNombre("laptop", 0, 10);

        assertEquals(1, lista.size());
        assertSame(p, lista.get(0));
    }

    @Test
    void testBuscarProductosPorNombreNull() {

        List<Producto> lista = dao.buscarProductosPorNombre(null, 0, 10);

        assertTrue(lista.isEmpty());
        verify(emMock, never()).createNamedQuery(any(), eq(Producto.class));
    }

    @Test
    void testBuscarProductosPorNombreVacio() {

        List<Producto> lista = dao.buscarProductosPorNombre("   ", 0, 10);

        assertTrue(lista.isEmpty());
        verify(emMock, never()).createNamedQuery(any(), eq(Producto.class));
    }

    @Test
    void testBuscarProductosPorNombreParametrosInvalidos() {

        List<Producto> lista = dao.buscarProductosPorNombre("laptop", -1, 10);

        assertTrue(lista.isEmpty());
        verify(emMock, never()).createNamedQuery(any(), eq(Producto.class));
    }

    @Test
    void testBuscarProductosPorNombreExcepcion() {

        when(emMock.createNamedQuery("Producto.buscarProductosPorNombre", Producto.class))
                .thenThrow(new RuntimeException("Error DB"));

        assertThrows(IllegalStateException.class,
                () -> dao.buscarProductosPorNombre("pc", 0, 10));
    }

    // =========================================================================
    // TESTS PARA buscarProductoPorNombre (EL SEGUNDO MÉTODO)
    // =========================================================================

    @Test
    void testBuscarProductoPorNombreValido() {

        Producto p = new Producto();
        List<Producto> resultado = List.of(p);

        when(emMock.createNamedQuery("Producto.buscarProductosPorNombre", Producto.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter("nombreProducto", "%mouse%"))
                .thenReturn(queryMock);
        when(queryMock.setFirstResult(0))
                .thenReturn(queryMock);
        when(queryMock.setMaxResults(10))
                .thenReturn(queryMock);
        when(queryMock.getResultList())
                .thenReturn(resultado);

        List<Producto> lista = dao.buscarProductoPorNombre("mouse", 0, 10);

        assertEquals(1, lista.size());
        assertSame(p, lista.get(0));
    }

    @Test
    void testBuscarProductoPorNombreNull() {

        List<Producto> lista = dao.buscarProductoPorNombre(null, 0, 10);

        assertTrue(lista.isEmpty());
        verify(emMock, never()).createNamedQuery(any(), eq(Producto.class));
    }

    @Test
    void testBuscarProductoPorNombreVacio() {

        List<Producto> lista = dao.buscarProductoPorNombre("  ", 0, 10);

        assertTrue(lista.isEmpty());
        verify(emMock, never()).createNamedQuery(any(), eq(Producto.class));
    }

    @Test
    void testBuscarProductoPorNombreParametrosInvalidos() {

        List<Producto> lista = dao.buscarProductoPorNombre("mouse", -1, 10);

        assertTrue(lista.isEmpty());
        verify(emMock, never()).createNamedQuery(any(), eq(Producto.class));
    }

    @Test
    void testBuscarProductoPorNombreExcepcion() {

        when(emMock.createNamedQuery("Producto.buscarProductosPorNombre", Producto.class))
                .thenThrow(new RuntimeException("Error DB"));

        assertThrows(IllegalStateException.class,
                () -> dao.buscarProductoPorNombre("teclado", 0, 10));
    }
}
