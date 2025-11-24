package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoProductoDAOTest {

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<TipoProducto> typedQuery;

    @InjectMocks
    TipoProductoDAO dao;

    private TipoProducto tipo;

    @BeforeEach
    void setUp() {
        tipo = new TipoProducto();
        tipo.setId(1L);
    }

    // ------------------------------------------------------------
    // findTiposPadre()
    // ------------------------------------------------------------
    @Test
    void testFindTiposPadreOK() {
        when(em.createNamedQuery("TipoProducto.findTiposPadre", TipoProducto.class))
                .thenReturn(typedQuery);

        when(typedQuery.getResultList())
                .thenReturn(List.of(tipo));

        List<TipoProducto> resultado = dao.findTiposPadre();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
    }

    @Test
    void testFindTiposPadreException() {
        when(em.createNamedQuery("TipoProducto.findTiposPadre", TipoProducto.class))
                .thenThrow(new RuntimeException("DB ERROR"));

        List<TipoProducto> resultado = dao.findTiposPadre();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ------------------------------------------------------------
    // findHijosByPadre()
    // ------------------------------------------------------------
    @Test
    void testFindHijosByPadreOK() {

        when(em.createNamedQuery("TipoProducto.findHijosByPadre", TipoProducto.class))
                .thenReturn(typedQuery);

        when(typedQuery.setParameter(eq("idPadre"), eq(1L))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(tipo));

        List<TipoProducto> resultado = dao.findHijosByPadre(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testFindHijosByPadreException() {
        when(em.createNamedQuery("TipoProducto.findHijosByPadre", TipoProducto.class))
                .thenThrow(new RuntimeException("ERR"));

        List<TipoProducto> resultado = dao.findHijosByPadre(1L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ------------------------------------------------------------
    // findByNameLike()
    // ------------------------------------------------------------
    @Test
    void testFindByNameLikeOK() {

        when(em.createNamedQuery("TipoProducto.findByNombreLike", TipoProducto.class))
                .thenReturn(typedQuery);

        when(typedQuery.setParameter(eq("nombre"), anyString())).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(0)).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(10)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(List.of(tipo));

        List<TipoProducto> resultado = dao.findByNameLike("TEST", 0, 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void testFindByNameLikeParametrosInvalidos() {
        List<TipoProducto> resultado = dao.findByNameLike("", 0, 10);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testFindByNameLikeException() {
        when(em.createNamedQuery("TipoProducto.findByNombreLike", TipoProducto.class))
                .thenThrow(new RuntimeException("ERR"));

        List<TipoProducto> resultado = dao.findByNameLike("A", 0, 10);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
