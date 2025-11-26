package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioDefaultDataAccessTest {

    static class TestEntity {
        private Integer id;
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
    }

    static class TestDAO extends InventarioDefaultDataAccess<TestEntity, Integer> {
        private EntityManager em;
        public TestDAO(Class<TestEntity> cls) { super(cls); }
        public void setEntityManager(EntityManager em) { this.em = em; }
        @Override
        public EntityManager getEntityManager() { return em; }
        @Override
        protected Class<TestEntity> getEntityClass() { return TestEntity.class; }
    }

    @Mock
    EntityManager em;

    @Mock
    CriteriaBuilder cb;

    @Mock
    CriteriaQuery criteriaQuery;

    @Mock
    CriteriaQuery longCriteriaQuery;

    @Mock
    Root root;

    @Mock
    TypedQuery<TestEntity> typedQuery;

    @Mock
    TypedQuery<Long> longQuery;

    TestDAO dao;

    @BeforeEach
    void setUp() {
        dao = new TestDAO(TestEntity.class);
        dao.setEntityManager(em);
    }

    @Test
    void create_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> dao.create(null));
    }

    @Test
    void create_success_persists() {
        TestEntity e = new TestEntity();
        dao.create(e);
        verify(em).persist(e);
    }

    @Test
    void findById_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> dao.findById(null));
    }

    @Test
    void findById_success_returnsEntity() {
        TestEntity e = new TestEntity(); e.setId(5);
        when(em.find(TestEntity.class, 5)).thenReturn(e);
        TestEntity res = dao.findById(5);
        assertSame(e, res);
    }

    @Test
    void delete_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> dao.delete(null));
    }

    @Test
    void delete_success_mergesAndRemoves() {
        TestEntity e = new TestEntity(); e.setId(3);
        TestEntity managed = new TestEntity(); managed.setId(3);
        when(em.merge(e)).thenReturn(managed);
        doNothing().when(em).remove(managed);
        dao.delete(e);
        verify(em).merge(e);
        verify(em).remove(managed);
    }

    @Test
    void update_null_throws() {
        assertThrows(IllegalArgumentException.class, () -> dao.update(null));
    }

    @Test
    void update_success_returnsMerged() {
        TestEntity e = new TestEntity(); e.setId(8);
        when(em.merge(e)).thenReturn(e);
        TestEntity r = dao.update(e);
        assertSame(e, r);
    }

    @Test
    void findRange_invalid_throws() {
        assertThrows(IllegalArgumentException.class, () -> dao.findRange(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> dao.findRange(1, 0));
    }

    @Test
    void findRange_success_returnsList() {
        List<TestEntity> list = new ArrayList<>();
        TestEntity e = new TestEntity(); e.setId(2); list.add(e);
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(TestEntity.class)).thenReturn((CriteriaQuery<TestEntity>) criteriaQuery);
        when(criteriaQuery.from(TestEntity.class)).thenReturn(root);
        when(em.createQuery((CriteriaQuery<TestEntity>) any())).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(list);

        List<TestEntity> res = dao.findRange(0, 10);
        assertEquals(1, res.size());
    }

    @Test
    void count_success_returnsInt() {
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Long.class)).thenReturn((CriteriaQuery<Long>) longCriteriaQuery);
        when(longCriteriaQuery.from(TestEntity.class)).thenReturn(root);
        when(em.createQuery((CriteriaQuery<Long>) any())).thenReturn((TypedQuery<Long>) longQuery);
        when(longQuery.getSingleResult()).thenReturn(5L);
        int c = dao.count();
        assertEquals(5, c);
    }
}
