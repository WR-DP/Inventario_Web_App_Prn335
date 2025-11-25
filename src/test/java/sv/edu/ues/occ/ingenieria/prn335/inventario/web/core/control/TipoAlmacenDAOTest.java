package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoAlmacen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TipoAlmacenDAOTest {

    private EntityManager emMock;
    private TipoAlmacenDAO dao;

    @BeforeEach
    void setUp() throws Exception {

        emMock = Mockito.mock(EntityManager.class);
        dao = new TipoAlmacenDAO();

        // Inyectar EM con reflexión
        var field = TipoAlmacenDAO.class.getDeclaredField("em");
        field.setAccessible(true);
        field.set(dao, emMock);
    }

    // -------------------------------------------------------------
    // TEST findById() delega correctamente a la clase padre
    // -------------------------------------------------------------
    @Test
    void testFindById() {

        TipoAlmacen ta = new TipoAlmacen();

        when(emMock.find(TipoAlmacen.class, 1L)).thenReturn(ta);

        TipoAlmacen res = dao.findById(1L);

        assertSame(ta, res);
    }

    // -------------------------------------------------------------
    // TEST count() funciona y delega en EntityManager + Criteria API
    // -------------------------------------------------------------
    @Test
    void testCount() {

        // Mocks necesarios para el count()
        var cbMock = Mockito.mock(jakarta.persistence.criteria.CriteriaBuilder.class);
        var cqMock = Mockito.mock(jakarta.persistence.criteria.CriteriaQuery.class);
        var rootMock = Mockito.mock(jakarta.persistence.criteria.Root.class);
        var typedQueryMock = Mockito.mock(jakarta.persistence.TypedQuery.class);

        when(emMock.getCriteriaBuilder()).thenReturn(cbMock);
        when(cbMock.createQuery(Long.class)).thenReturn(cqMock);
        when(cqMock.from(TipoAlmacen.class)).thenReturn(rootMock);
        when(emMock.createQuery(cqMock)).thenReturn(typedQueryMock);
        when(typedQueryMock.getSingleResult()).thenReturn(7L);

        int resultado = dao.count();

        assertEquals(7, resultado);
    }

    // -------------------------------------------------------------
    // TEST create() de clase padre
    // -------------------------------------------------------------
    @Test
    void testCreate() {

        TipoAlmacen t = new TipoAlmacen();

        dao.create(t);

        verify(emMock, times(1)).persist(t);
    }

    // -------------------------------------------------------------
    // TEST update() de clase padre
    // -------------------------------------------------------------
    @Test
    void testUpdate() {

        TipoAlmacen t = new TipoAlmacen();

        when(emMock.merge(t)).thenReturn(t);

        TipoAlmacen resultado = dao.update(t);

        assertSame(t, resultado);
    }

    // -------------------------------------------------------------
    // TEST delete() de clase padre
    // -------------------------------------------------------------
    @Test
    void testDelete() {

        TipoAlmacen t = new TipoAlmacen();
        TipoAlmacen managed = new TipoAlmacen();

        when(emMock.merge(t)).thenReturn(managed);

        dao.delete(t);

        verify(emMock, times(1)).remove(managed);
    }
}
