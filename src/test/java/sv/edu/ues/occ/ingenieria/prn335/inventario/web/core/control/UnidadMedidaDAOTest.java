package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.UnidadMedida;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnidadMedidaDAOTest {

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<UnidadMedida> typedQueryUnidad;

    @Mock
    TypedQuery<Long> typedQueryLong;

    @InjectMocks
    UnidadMedidaDAO dao;

    UnidadMedida unidad;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        unidad = new UnidadMedida();
        unidad.setId(10); // entero
    }

    // -----------------------------------------------------------
    // findByIdTipoUnidadMedida
    // -----------------------------------------------------------

    @Test
    void testFindByIdTipoUnidadMedidaOK() {
        when(em.createNamedQuery("UnidadMedida.findByIdTipoUnidadMedida", UnidadMedida.class))
                .thenReturn(typedQueryUnidad);

        when(typedQueryUnidad.setParameter("idTipoUnidadMedida", 10)).thenReturn(typedQueryUnidad);
        when(typedQueryUnidad.setFirstResult(0)).thenReturn(typedQueryUnidad);
        when(typedQueryUnidad.setMaxResults(10)).thenReturn(typedQueryUnidad);

        when(typedQueryUnidad.getResultList()).thenReturn(List.of(unidad));

        List<UnidadMedida> resultado = dao.findByIdTipoUnidadMedida(10, 0, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(unidad.getId(), resultado.get(0).getId());
    }

    @Test
    void testFindByIdTipoUnidadMedidaNullParam() {
        List<UnidadMedida> resultado = dao.findByIdTipoUnidadMedida(null, 0, 10);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testFindByIdTipoUnidadMedidaException() {
        when(em.createNamedQuery("UnidadMedida.findByIdTipoUnidadMedida", UnidadMedida.class))
                .thenThrow(new RuntimeException("ERR"));

        List<UnidadMedida> resultado = dao.findByIdTipoUnidadMedida(10, 0, 10);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // -----------------------------------------------------------
    // countByIdTipoUnidadMedida
    // -----------------------------------------------------------

    @Test
    void testCountByIdTipoUnidadMedidaOK() {
        when(em.createNamedQuery("UnidadMedida.countByIdTipoUnidadMedida", Long.class))
                .thenReturn(typedQueryLong);

        when(typedQueryLong.setParameter("idTipoUnidadMedida", 10)).thenReturn(typedQueryLong);
        when(typedQueryLong.getSingleResult()).thenReturn(5L);

        Long resultado = dao.countByIdTipoUnidadMedida(10);

        assertEquals(5L, resultado);
    }

    @Test
    void testCountByIdTipoUnidadMedidaNullParam() {
        Long resultado = dao.countByIdTipoUnidadMedida(null);

        assertEquals(0L, resultado);
    }

    @Test
    void testCountByIdTipoUnidadMedidaException() {
        when(em.createNamedQuery("UnidadMedida.countByIdTipoUnidadMedida", Long.class))
                .thenThrow(new RuntimeException("ERROR"));

        Long resultado = dao.countByIdTipoUnidadMedida(10);

        assertEquals(0L, resultado);
    }
}
