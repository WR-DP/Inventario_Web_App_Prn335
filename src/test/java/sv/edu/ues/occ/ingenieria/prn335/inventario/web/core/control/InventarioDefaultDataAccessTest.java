package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventarioDefaultDataAccessTest {

    private EntityManager emMock;
    private CriteriaBuilder cbMock;
    private CriteriaQuery<Object> cqMock;
    private Root<Object> rootMock;
    private TypedQuery<Object> typedQueryMock;
    private TypedQuery<Long> typedQueryLongMock;

    // clase dummy para probar la clase abstracta
    private InventarioDefaultDataAccess<Object, Object> dao;

    @BeforeEach
    void setUp() throws Exception {

        emMock = Mockito.mock(EntityManager.class);
        cbMock = Mockito.mock(CriteriaBuilder.class);
        cqMock = Mockito.mock(CriteriaQuery.class);
        rootMock = Mockito.mock(Root.class);
        typedQueryMock = Mockito.mock(TypedQuery.class);
        typedQueryLongMock = Mockito.mock(TypedQuery.class);

        // Implementación dummy
        dao = new InventarioDefaultDataAccess<>(Object.class) {
            @Override
            public EntityManager getEntityManager() {
                return emMock;
            }

            @Override
            protected Class<Object> getEntityClass() {
                return Object.class;
            }
        };

        when(emMock.getCriteriaBuilder()).thenReturn(cbMock);
        when(cbMock.createQuery(Object.class)).thenReturn(cqMock);
        when(cbMock.createQuery(Long.class)).thenReturn(Mockito.mock(CriteriaQuery.class));
        when(cqMock.from(Object.class)).thenReturn(rootMock);
        when(emMock.createQuery(any(CriteriaQuery.class))).thenReturn(typedQueryMock);
    }

    // --------------------------------------------------------------------
    // TEST create()
    // --------------------------------------------------------------------
    @Test
    void testCreateValido() {
        Object entity = new Object();
        dao.create(entity);

        verify(emMock, times(1)).persist(entity);
    }

    @Test
    void testCreateNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.create(null));
    }

    // --------------------------------------------------------------------
    // TEST findById()
    // --------------------------------------------------------------------
    @Test
    void testFindByIdValido() {
        Object entity = new Object();
        when(emMock.find(Object.class, 1L)).thenReturn(entity);

        Object result = dao.findById(1L);

        assertSame(entity, result);
    }

    @Test
    void testFindByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.findById(null));
    }

    // --------------------------------------------------------------------
    // TEST update()
    // --------------------------------------------------------------------
    @Test
    void testUpdateValido() {
        Object entity = new Object();

        when(emMock.merge(entity)).thenReturn(entity);

        Object result = dao.update(entity);

        assertSame(entity, result);
    }

    @Test
    void testUpdateNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.update(null));
    }

    // --------------------------------------------------------------------
    // TEST delete()
    // --------------------------------------------------------------------
    @Test
    void testDeleteValido() {
        Object entity = new Object();
        Object managed = new Object();

        when(emMock.merge(entity)).thenReturn(managed);

        dao.delete(entity);

        verify(emMock, times(1)).remove(managed);
    }

    @Test
    void testDeleteNull() {
        assertThrows(IllegalArgumentException.class, () -> dao.delete(null));
    }

    // --------------------------------------------------------------------
    // TEST findRange()
    // --------------------------------------------------------------------
    @Test
    void testFindRangeValido() {

        when(typedQueryMock.setFirstResult(0)).thenReturn(typedQueryMock);
        when(typedQueryMock.setMaxResults(10)).thenReturn(typedQueryMock);
        when(typedQueryMock.getResultList()).thenReturn(List.of("A", "B"));

        List<Object> result = dao.findRange(0, 10);

        assertEquals(2, result.size());
        assertEquals("A", result.get(0));
    }

    @Test
    void testFindRangeParametrosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> dao.findRange(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> dao.findRange(0, 0));
    }

    // --------------------------------------------------------------------
    // TEST count()
    // --------------------------------------------------------------------
    @Test
    void testCountValido() {

        CriteriaQuery<Long> cqLong = Mockito.mock(CriteriaQuery.class);
        Root<Object> rootLong = Mockito.mock(Root.class);

        when(cbMock.createQuery(Long.class)).thenReturn(cqLong);
        when(cqLong.from(Object.class)).thenReturn(rootLong);
        when(emMock.createQuery(cqLong)).thenReturn(typedQueryLongMock);
        when(typedQueryLongMock.getSingleResult()).thenReturn(7L);

        int count = dao.count();

        assertEquals(7, count);
    }
}
