package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteDAOTest {

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<Cliente> query;

    @Mock
    CriteriaBuilder cb;

    @Mock
    CriteriaQuery<Long> cq;

    @Mock
    Root<Cliente> root;

    @Mock
    TypedQuery<Long> tq;

    @InjectMocks
    ClienteDAO dao;

    Cliente cliente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = new Cliente();
    }

    // ===============================================================
    // findById
    // ===============================================================
    @Test
    void testFindByIdOK() {
        when(em.find(Cliente.class, 1L)).thenReturn(cliente);
        assertEquals(cliente, dao.findById(1L));
    }

    @Test
    void testFindByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.findById(null));
    }

    // ===============================================================
    // create()
    // ===============================================================
    @Test
    void testCreateOK() {
        dao.create(cliente);
        verify(em).persist(cliente);
    }

    @Test
    void testCreateNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.create(null));
    }

    // ===============================================================
    // update()
    // ===============================================================
    @Test
    void testUpdateOK() {
        when(em.merge(cliente)).thenReturn(cliente);
        assertEquals(cliente, dao.update(cliente));
        verify(em).merge(cliente);
    }

    @Test
    void testUpdateNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.update(null));
    }

    // ===============================================================
    // delete()
    // ===============================================================
    @Test
    void testDeleteOK() {
        when(em.merge(cliente)).thenReturn(cliente);

        dao.delete(cliente);

        verify(em).merge(cliente);
        verify(em).remove(cliente);
    }

    @Test
    void testDeleteNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.delete(null));
    }

    // ===============================================================
    // count()
    // ===============================================================
    @Test
    void testCountOK() {
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Long.class)).thenReturn(cq);
        when(cq.from(Cliente.class)).thenReturn(root);
        when(em.createQuery(cq)).thenReturn(tq);
        when(tq.getSingleResult()).thenReturn(7L);

        int resultado = dao.count();

        assertEquals(7, resultado);
    }

    @Test
    void testCountException() {
        when(em.getCriteriaBuilder()).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> dao.count());
    }

    // ===============================================================
    // buscarClientePorNombre()
    // ===============================================================
    @Test
    void testBuscarClientePorNombreOK() {

        when(em.createNamedQuery("Cliente.buscarClientePorNombre", Cliente.class))
                .thenReturn(query);

        when(query.setParameter(eq("nombre"), any()))
                .thenReturn(query);
        when(query.setFirstResult(0)).thenReturn(query);
        when(query.setMaxResults(10)).thenReturn(query);

        when(query.getResultList())
                .thenReturn(List.of(cliente));

        List<Cliente> resultado =
                dao.buscarClientePorNombre("Juan", 0, 10);

        assertEquals(1, resultado.size());
    }

    @Test
    void testBuscarClientePorNombreNombreNull() {
        List<Cliente> resultado = dao.buscarClientePorNombre(null, 0, 10);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarClientePorNombreNombreVacio() {
        List<Cliente> resultado = dao.buscarClientePorNombre("   ", 0, 10);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarClientePorNombreRangoInvalido() {
        List<Cliente> resultado = dao.buscarClientePorNombre("Juan", -1, 10);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testBuscarClientePorNombreException() {

        when(em.createNamedQuery("Cliente.buscarClientePorNombre", Cliente.class))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(
                IllegalStateException.class,
                () -> dao.buscarClientePorNombre("Juan", 0, 10)
        );
    }
}
