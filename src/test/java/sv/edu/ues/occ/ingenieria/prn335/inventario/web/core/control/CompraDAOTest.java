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
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CompraDAOTest {

    @Mock
    EntityManager em;

    @Mock
    CriteriaBuilder cb;

    @Mock
    CriteriaQuery<Long> cq;

    @Mock
    Root<Compra> root;

    @Mock
    TypedQuery<Long> tq;

    @InjectMocks
    CompraDAO dao;

    Compra compra;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        compra = new Compra();
    }

    // ===============================================================
    // findById()
    // ===============================================================
    @Test
    void testFindByIdOK() {
        when(em.find(Compra.class, 1L)).thenReturn(compra);

        Compra resultado = dao.findById(1L);

        assertEquals(compra, resultado);
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
        dao.create(compra);
        verify(em).persist(compra);
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
        when(em.merge(compra)).thenReturn(compra);

        Compra resultado = dao.update(compra);

        assertEquals(compra, resultado);
        verify(em).merge(compra);
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
        when(em.merge(compra)).thenReturn(compra);

        dao.delete(compra);

        verify(em).merge(compra);
        verify(em).remove(compra);
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
        when(cq.from(Compra.class)).thenReturn(root);
        when(em.createQuery(cq)).thenReturn(tq);
        when(tq.getSingleResult()).thenReturn(5L);

        int resultado = dao.count();

        assertEquals(5, resultado);
    }

    @Test
    void testCountException() {
        when(em.getCriteriaBuilder()).thenThrow(new RuntimeException("DB ERROR"));

        assertThrows(RuntimeException.class, () -> dao.count());
    }
}
