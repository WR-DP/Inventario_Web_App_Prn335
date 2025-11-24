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
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProveedorDAOTest {

    @Mock
    EntityManager em;

    @Mock
    CriteriaBuilder cb;

    @Mock
    CriteriaQuery<Long> cq;

    @Mock
    Root<Proveedor> root;

    @Mock
    TypedQuery<Long> tq;

    @InjectMocks
    ProveedorDAO dao;

    Proveedor proveedor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        proveedor = new Proveedor();
    }

    // ============================================================
    // findById()
    // ============================================================
    @Test
    void testFindByIdOK() {
        when(em.find(Proveedor.class, 1L)).thenReturn(proveedor);

        Proveedor resultado = dao.findById(1L);

        assertEquals(proveedor, resultado);
    }

    @Test
    void testFindByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.findById(null));
    }

    // ============================================================
    // create()
    // ============================================================
    @Test
    void testCreateOK() {
        dao.create(proveedor);
        verify(em).persist(proveedor);
    }

    @Test
    void testCreateNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.create(null));
    }

    // ============================================================
    // update()
    // ============================================================
    @Test
    void testUpdateOK() {
        when(em.merge(proveedor)).thenReturn(proveedor);

        Proveedor resultado = dao.update(proveedor);

        assertEquals(proveedor, resultado);
        verify(em).merge(proveedor);
    }

    @Test
    void testUpdateNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.update(null));
    }

    // ============================================================
    // delete()
    // ============================================================
    @Test
    void testDeleteOK() {
        when(em.merge(proveedor)).thenReturn(proveedor);

        dao.delete(proveedor);

        verify(em).merge(proveedor);
        verify(em).remove(proveedor);
    }

    @Test
    void testDeleteNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.delete(null));
    }

    // ============================================================
    // count()
    // ============================================================
    @Test
    void testCountOK() {
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Long.class)).thenReturn(cq);
        when(cq.from(Proveedor.class)).thenReturn(root);
        when(em.createQuery(cq)).thenReturn(tq);
        when(tq.getSingleResult()).thenReturn(10L);

        int resultado = dao.count();

        assertEquals(10, resultado);
    }

    @Test
    void testCountException() {
        when(em.getCriteriaBuilder()).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> dao.count());
    }
}
