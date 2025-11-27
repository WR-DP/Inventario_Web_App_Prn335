package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaDAOTest {

    private EntityManager em;
    private TypedQuery<Venta> queryVenta;
    private TypedQuery<Long> queryCount;
    private VentaDAO dao;

    private Venta venta;

    @BeforeEach
    void setUp() throws Exception {

        em = Mockito.mock(EntityManager.class);
        queryVenta = Mockito.mock(TypedQuery.class);
        queryCount = Mockito.mock(TypedQuery.class);

        dao = new VentaDAO();

        // Inyectamos EntityManager al DAO por reflexión
        var field = VentaDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(dao, em);

        venta = new Venta();
        venta.setId(UUID.randomUUID());
    }

    @Test
    void testFindClienteByIdOK() {
        UUID id = UUID.randomUUID();
        Cliente c = new Cliente();

        when(em.find(Cliente.class, id)).thenReturn(c);

        Cliente resultado = dao.findClienteById(id);

        assertSame(c, resultado);
    }

    @Test
    void testFindClienteByIdNull() {
        when(em.find(Cliente.class, null)).thenReturn(null);
        Cliente resultado = dao.findClienteById(null);
        assertNull(resultado);
    }

    @Test
    void testBuscarVentasParaDespachoOK() {

        Venta v = new Venta();
        List<Venta> lista = List.of(v);

        when(em.createQuery("SELECT v FROM Venta v WHERE v.estado = 'ACTIVA'", Venta.class))
                .thenReturn(queryVenta);
        when(queryVenta.setFirstResult(0)).thenReturn(queryVenta);
        when(queryVenta.setMaxResults(10)).thenReturn(queryVenta);
        when(queryVenta.getResultList()).thenReturn(lista);

        List<Venta> resultado = dao.buscarVentasParaDespacho(0, 10);

        assertEquals(1, resultado.size());
        assertSame(v, resultado.get(0));
    }

    @Test
    void testBuscarVentasParaDespachoException() {

        when(em.createQuery(anyString(), eq(Venta.class)))
                .thenThrow(new RuntimeException("ERR"));

        assertThrows(RuntimeException.class,
                () -> dao.buscarVentasParaDespacho(0, 10));
    }

    @Test
    void testContarVentasParaDespachoOK() {

        when(em.createQuery("SELECT COUNT(v) FROM Venta v WHERE v.estado = 'ACTIVA'", Long.class))
                .thenReturn(queryCount);
        when(queryCount.getSingleResult()).thenReturn(5L);

        Long result = dao.contarVentasParaDespacho();

        assertEquals(5L, result);
    }

    @Test
    void testContarVentasParaDespachoException() {

        when(em.createQuery(anyString(), eq(Long.class)))
                .thenThrow(new RuntimeException("ERR"));

        assertThrows(RuntimeException.class,
                () -> dao.contarVentasParaDespacho());
    }
}
