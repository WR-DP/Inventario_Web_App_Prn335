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
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Almacen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlmacenDAOTest {

    @Mock
    EntityManager em;

    @Mock
    CriteriaBuilder cb;

    @Mock
    CriteriaQuery<Long> cq;

    @Mock
    Root<Almacen> root;

    @Mock
    TypedQuery<Long> tq;

    @InjectMocks
    AlmacenDAO dao;

    Almacen almacen;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        almacen = new Almacen();
    }

    @Test
    void testFindByIdOK() {
        when(em.find(Almacen.class, 1L)).thenReturn(almacen);
        assertEquals(almacen, dao.findById(1L));
    }

    @Test
    void testFindByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.findById(null));
    }

    @Test
    void testCreateOK() {
        dao.create(almacen);
        verify(em).persist(almacen);
    }

    @Test
    void testCreateNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.create(null));
    }

    @Test
    void testUpdateOK() {
        when(em.merge(almacen)).thenReturn(almacen);
        assertEquals(almacen, dao.update(almacen));
        verify(em).merge(almacen);
    }

    @Test
    void testUpdateNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.update(null));
    }

    @Test
    void testDeleteOK() {
        when(em.merge(almacen)).thenReturn(almacen);
        dao.delete(almacen);
        verify(em).merge(almacen);
        verify(em).remove(almacen);
    }

    @Test
    void testDeleteNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.delete(null));
    }

    @Test
    void testCountOK() {

        // Mock completo del árbol Criteria
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Long.class)).thenReturn(cq);
        when(cq.from(Almacen.class)).thenReturn(root);
        when(em.createQuery(cq)).thenReturn(tq);
        when(tq.getSingleResult()).thenReturn(5L);

        int resultado = dao.count();

        assertEquals(5, resultado);
        verify(tq).getSingleResult();
    }
}
