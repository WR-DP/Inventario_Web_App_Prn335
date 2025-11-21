package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VentaDAOTest {

    @Mock
    EntityManager em;

    @InjectMocks
    VentaDAO dao;

    Venta venta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        venta = new Venta();
        venta.setId(UUID.randomUUID());
    }

    // -----------------------------------------------------------
    // findById
    // -----------------------------------------------------------

    @Test
    void testFindByIdOK() {
        when(em.find(Venta.class, venta.getId())).thenReturn(venta);

        Venta resultado = dao.findById(venta.getId());

        assertNotNull(resultado);
        assertEquals(venta.getId(), resultado.getId());
    }

    @Test
    void testFindByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.findById(null));
    }


    @Test
    void testFindByIdException() {
        when(em.find(Venta.class, venta.getId())).thenThrow(new RuntimeException("ERR"));

        assertThrows(RuntimeException.class, () -> dao.findById(venta.getId()));
    }

    // -----------------------------------------------------------
    // create
    // -----------------------------------------------------------

    @Test
    void testCreateOK() {
        dao.create(venta);
        verify(em, times(1)).persist(venta);
    }

    @Test
    void testCreateNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.create(null));
    }

    @Test
    void testCreateException() {
        doThrow(new RuntimeException("ERR")).when(em).persist(venta);

        assertThrows(RuntimeException.class, () -> dao.create(venta));
    }

    // -----------------------------------------------------------
    // update
    // -----------------------------------------------------------

    @Test
    void testUpdateOK() {
        when(em.merge(venta)).thenReturn(venta);

        Venta resultado = dao.update(venta);

        assertNotNull(resultado);
        assertEquals(venta.getId(), resultado.getId());
    }

    @Test
    void testUpdateNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.update(null));
    }

    @Test
    void testUpdateException() {
        when(em.merge(venta)).thenThrow(new RuntimeException("ERR"));

        assertThrows(RuntimeException.class, () -> dao.update(venta));
    }

    // -----------------------------------------------------------
    // delete
    // -----------------------------------------------------------

    @Test
    void testDeleteOK() {
        Venta venta = new Venta();
        UUID id = UUID.randomUUID();
        venta.setId(id);

        when(em.merge(venta)).thenReturn(venta);

        dao.delete(venta);

        verify(em).merge(venta);
        verify(em).remove(venta);
    }



    @Test
    void testDeleteNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.delete(null));
    }

    @Test
    void testDeleteException() {
        Venta venta = new Venta();
        venta.setId(UUID.randomUUID());

        when(em.merge(venta)).thenThrow(new RuntimeException("ERR"));

        assertThrows(RuntimeException.class, () -> dao.delete(venta));
    }



    // -----------------------------------------------------------
    // count (delegado a super)
    // -----------------------------------------------------------

    @Test
    void testCountException() {
        when(em.getCriteriaBuilder()).thenThrow(new RuntimeException("ERR"));

        assertThrows(RuntimeException.class, () -> dao.count());
    }
}
