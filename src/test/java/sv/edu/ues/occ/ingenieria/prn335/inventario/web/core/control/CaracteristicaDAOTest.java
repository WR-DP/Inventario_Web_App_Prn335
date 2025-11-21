package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Caracteristica;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CaracteristicaDAOTest {

    private EntityManager emMock;
    private TypedQuery<Caracteristica> queryMock;
    private CaracteristicaDAO dao;

    @BeforeEach
    void setUp() {
        emMock = Mockito.mock(EntityManager.class);
        queryMock = Mockito.mock(TypedQuery.class);

        dao = new CaracteristicaDAO();

        // Inyectamos el EntityManager via reflexión
        try {
            var field = CaracteristicaDAO.class.getDeclaredField("em");
            field.setAccessible(true);
            field.set(dao, emMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ------------------------------------------------------------
    // TEST: findByIdCaracteristica() con valores válidos
    // ------------------------------------------------------------
    @Test
    void testFindByIdCaracteristicaValido() {

        Caracteristica c = new Caracteristica();
        List<Caracteristica> resultado = List.of(c);

        when(emMock.createNamedQuery("Caracteristica.findByIdCaracteristica", Caracteristica.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("id"), any()))
                .thenReturn(queryMock);
        when(queryMock.setFirstResult(anyInt()))
                .thenReturn(queryMock);
        when(queryMock.setMaxResults(anyInt()))
                .thenReturn(queryMock);
        when(queryMock.getResultList())
                .thenReturn(resultado);

        List<Caracteristica> lista = dao.findByIdCaracteristica(5, 0, 10);

        assertEquals(1, lista.size());
        assertSame(c, lista.get(0));
    }

    // ------------------------------------------------------------
    // TEST: findByIdCaracteristica() con id NULL → retorna lista vacía
    // ------------------------------------------------------------
    @Test
    void testFindByIdCaracteristicaIdNull() {
        List<Caracteristica> lista = dao.findByIdCaracteristica(null, 0, 10);
        assertTrue(lista.isEmpty());
        verify(emMock, never()).createNamedQuery(any(), eq(Caracteristica.class));
    }

    // ------------------------------------------------------------
    // TEST: findByNombreLike() válido
    // ------------------------------------------------------------
    @Test
    void testFindByNombreLikeValido() {

        Caracteristica c = new Caracteristica();
        List<Caracteristica> resultado = List.of(c);

        when(emMock.createNamedQuery("Caracteristica.findByNombreLike", Caracteristica.class))
                .thenReturn(queryMock);
        when(queryMock.setParameter(eq("nombre"), anyString()))
                .thenReturn(queryMock);
        when(queryMock.setFirstResult(anyInt()))
                .thenReturn(queryMock);
        when(queryMock.setMaxResults(anyInt()))
                .thenReturn(queryMock);
        when(queryMock.getResultList())
                .thenReturn(resultado);

        List<Caracteristica> lista = dao.findByNombreLike("cpu", 0, 10);

        assertEquals(1, lista.size());
        assertSame(c, lista.get(0));
    }

    // ------------------------------------------------------------
    // TEST: findByNombreLike() nombre inválido → retorna vacío
    // ------------------------------------------------------------
    @Test
    void testFindByNombreLikeNombreInvalido() {
        List<Caracteristica> lista = dao.findByNombreLike("   ", 0, 10);
        assertTrue(lista.isEmpty());
    }

}
