package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProveedorDAOTest {

    private EntityManager em;
    private TypedQuery<Proveedor> query;
    private ProveedorDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        em = Mockito.mock(EntityManager.class);
        query = Mockito.mock(TypedQuery.class);

        dao = new ProveedorDAO();

        // Inyectamos el EntityManager por reflexión
        var field = ProveedorDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(dao, em);
    }

    @Test
    void testBuscarProveedorPorNombre_valido() {

        Proveedor p = new Proveedor();
        List<Proveedor> lista = List.of(p);

        when(em.createNamedQuery("Proveedor.buscarProveedorPorNombre", Proveedor.class))
                .thenReturn(query);
        when(query.setParameter("nombre", "%LAPTOP%"))
                .thenReturn(query);
        when(query.setFirstResult(0))
                .thenReturn(query);
        when(query.setMaxResults(10))
                .thenReturn(query);
        when(query.getResultList())
                .thenReturn(lista);

        List<Proveedor> result = dao.buscarProveedorPorNombre("laptop", 0, 10);

        assertEquals(1, result.size());
        assertSame(p, result.get(0));
    }

    @Test
    void testBuscarProveedorPorNombre_nombreNull() {

        List<Proveedor> result = dao.buscarProveedorPorNombre(null, 0, 10);

        assertTrue(result.isEmpty());
        verify(em, never()).createNamedQuery(any(), eq(Proveedor.class));
    }

    @Test
    void testBuscarProveedorPorNombre_nombreVacio() {

        List<Proveedor> result = dao.buscarProveedorPorNombre("   ", 0, 10);

        assertTrue(result.isEmpty());
        verify(em, never()).createNamedQuery(any(), eq(Proveedor.class));
    }

    @Test
    void testBuscarProveedorPorNombre_parametrosInvalidos_firstNegativo() {

        List<Proveedor> result = dao.buscarProveedorPorNombre("algo", -1, 10);

        assertTrue(result.isEmpty());
        verify(em, never()).createNamedQuery(any(), eq(Proveedor.class));
    }

    @Test
    void testBuscarProveedorPorNombre_parametrosInvalidos_maxCero() {

        List<Proveedor> result = dao.buscarProveedorPorNombre("algo", 0, 0);

        assertTrue(result.isEmpty());
        verify(em, never()).createNamedQuery(any(), eq(Proveedor.class));
    }

    @Test
    void testBuscarProveedorPorNombre_excepcion() {

        when(em.createNamedQuery("Proveedor.buscarProveedorPorNombre", Proveedor.class))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(IllegalStateException.class,
                () -> dao.buscarProveedorPorNombre("pc", 0, 10));
    }
}
