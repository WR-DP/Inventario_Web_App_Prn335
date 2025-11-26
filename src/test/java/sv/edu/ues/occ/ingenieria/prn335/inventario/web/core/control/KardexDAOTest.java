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
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Kardex;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class KardexDAOTest {

    @Mock
    EntityManager em;

    @Mock
    CriteriaBuilder cb;

    @Mock
    CriteriaQuery<Long> cq;

    @Mock
    Root<Kardex> root;

    @Mock
    TypedQuery<Long> tq;

    @InjectMocks
    KardexDAO dao;

    Kardex kardex;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kardex = new Kardex();
    }

    // ===============================================================
    // findById()
    // ===============================================================
    @Test
    void testFindByIdOK() {
        when(em.find(Kardex.class, 1L)).thenReturn(kardex);

        Kardex resultado = dao.findById(1L);

        assertEquals(kardex, resultado);
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
        dao.create(kardex);
        verify(em).persist(kardex);
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
        when(em.merge(kardex)).thenReturn(kardex);

        Kardex resultado = dao.update(kardex);

        assertEquals(kardex, resultado);
        verify(em).merge(kardex);
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
        when(em.merge(kardex)).thenReturn(kardex);

        dao.delete(kardex);

        verify(em).merge(kardex);
        verify(em).remove(kardex);
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
        when(cq.from(Kardex.class)).thenReturn(root);
        when(em.createQuery(cq)).thenReturn(tq);
        when(tq.getSingleResult()).thenReturn(7L);

        int resultado = dao.count();

        assertEquals(7, resultado);
    }

    @Test
    void testCountException() {
        when(em.getCriteriaBuilder()).thenThrow(new RuntimeException("DB ERROR"));

        assertThrows(RuntimeException.class, () -> dao.count());
    }
}
