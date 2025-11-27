package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Caracteristica;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoUnidadMedida;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CaracteristicaDAOTest {

    private EntityManager em;
    private TypedQuery<Caracteristica> query;
    private CaracteristicaDAO dao;

    @BeforeEach
    void setup() throws Exception {
        em = Mockito.mock(EntityManager.class);
        query = Mockito.mock(TypedQuery.class);

        dao = new CaracteristicaDAO();

        // inyectar EntityManager via reflexión
        var field = CaracteristicaDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(dao, em);
    }

    @Test
    void testFindTipoUnidadMedidaById_OK() {
        TipoUnidadMedida tu = new TipoUnidadMedida();
        when(em.find(TipoUnidadMedida.class, 5)).thenReturn(tu);

        TipoUnidadMedida result = dao.findTipoUnidadMedidaById(5);

        assertSame(tu, result);
    }

    @Test
    void testFindTipoUnidadMedidaById_null() {
        when(em.find(TipoUnidadMedida.class, null)).thenReturn(null);

        TipoUnidadMedida result = dao.findTipoUnidadMedidaById(null);

        assertNull(result);
    }

    @Test
    void testFindByIdCaracteristica_valido() {

        Caracteristica c = new Caracteristica();
        List<Caracteristica> lista = List.of(c);

        when(em.createNamedQuery("Caracteristica.findByIdCaracteristica", Caracteristica.class))
                .thenReturn(query);
        when(query.setParameter("id", 5))
                .thenReturn(query);
        when(query.setFirstResult(0))
                .thenReturn(query);
        when(query.setMaxResults(10))
                .thenReturn(query);
        when(query.getResultList())
                .thenReturn(lista);

        List<Caracteristica> result = dao.findByIdCaracteristica(5, 0, 10);

        assertEquals(1, result.size());
        assertSame(c, result.get(0));
    }

    @Test
    void testFindByIdCaracteristica_nullId() {
        List<Caracteristica> result = dao.findByIdCaracteristica(null, 0, 10);

        assertTrue(result.isEmpty());
        verify(em, never()).createNamedQuery(any(), eq(Caracteristica.class));
    }

    @Test
    void testFindByIdCaracteristica_excepcion() {

        when(em.createNamedQuery("Caracteristica.findByIdCaracteristica", Caracteristica.class))
                .thenThrow(new RuntimeException("DB error"));

        List<Caracteristica> result = dao.findByIdCaracteristica(5, 0, 10);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByNombreLike_valido() {

        Caracteristica c = new Caracteristica();
        List<Caracteristica> lista = List.of(c);

        when(em.createNamedQuery("Caracteristica.findByNombreLike", Caracteristica.class))
                .thenReturn(query);
        when(query.setParameter("nombre", "%CPU%"))
                .thenReturn(query);
        when(query.setFirstResult(0))
                .thenReturn(query);
        when(query.setMaxResults(10))
                .thenReturn(query);
        when(query.getResultList())
                .thenReturn(lista);

        List<Caracteristica> result = dao.findByNombreLike("cpu", 0, 10);

        assertEquals(1, result.size());
        assertSame(c, result.get(0));
    }

    @Test
    void testFindByNombreLike_nombreInvalido() {

        List<Caracteristica> result = dao.findByNombreLike("   ", 0, 10);

        assertTrue(result.isEmpty());
        verify(em, never()).createNamedQuery(any(), eq(Caracteristica.class));
    }

    @Test
    void testFindByNombreLike_parametrosInvalidos() {

        List<Caracteristica> result = dao.findByNombreLike("cpu", -1, 10);

        assertTrue(result.isEmpty());
        verify(em, never()).createNamedQuery(any(), eq(Caracteristica.class));
    }

    @Test
    void testFindByNombreLike_excepcion() {

        when(em.createNamedQuery("Caracteristica.findByNombreLike", Caracteristica.class))
                .thenThrow(new RuntimeException("DB error"));

        List<Caracteristica> result = dao.findByNombreLike("cpu", 0, 10);

        assertTrue(result.isEmpty()); // método atrapa error y devuelve lista vacía
    }
}
