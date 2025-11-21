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
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.KardexDetalle;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class KardexDetalleDAOTest {

    @Mock
    EntityManager em;

    @Mock
    CriteriaBuilder cb;

    @Mock
    CriteriaQuery<Long> cq;

    @Mock
    Root<KardexDetalle> root;

    @Mock
    TypedQuery<Long> tq;

    @InjectMocks
    KardexDetalleDAO dao;

    KardexDetalle detalle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        detalle = new KardexDetalle();
    }

    // ============================================================
    // findById()
    // ============================================================
    @Test
    void testFindByIdOK() {
        when(em.find(KardexDetalle.class, 1L)).thenReturn(detalle);

        KardexDetalle resultado = dao.findById(1L);

        assertEquals(detalle, resultado);
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
        dao.create(detalle);
        verify(em).persist(detalle);
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
        when(em.merge(detalle)).thenReturn(detalle);

        KardexDetalle resultado = dao.update(detalle);

        assertEquals(detalle, resultado);
        verify(em).merge(detalle);
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
        when(em.merge(detalle)).thenReturn(detalle);

        dao.delete(detalle);

        verify(em).merge(detalle);
        verify(em).remove(detalle);
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
        when(cq.from(KardexDetalle.class)).thenReturn(root);
        when(em.createQuery(cq)).thenReturn(tq);
        when(tq.getSingleResult()).thenReturn(5L);

        int resultado = dao.count();

        assertEquals(5, resultado);
    }

    @Test
    void testCountException() {
        when(em.getCriteriaBuilder()).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> dao.count());
    }
}
