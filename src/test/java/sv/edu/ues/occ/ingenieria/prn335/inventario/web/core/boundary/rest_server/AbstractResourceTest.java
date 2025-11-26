package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractResourceTest {

    static class TestEntity {
        private Integer id;
        private String name;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestEntity that = (TestEntity) o;
            if (id != null) return id.equals(that.id);
            return super.equals(o);
        }
    }

    static class TestResource extends AbstractResource<TestEntity, Integer> {
        private InventarioDefaultDataAccess<TestEntity, Integer> beanMock;

        public TestResource(InventarioDefaultDataAccess<TestEntity, Integer> bean) {
            this.beanMock = bean;
        }

        @Override
        protected InventarioDefaultDataAccess<TestEntity, Integer> getBean() {
            return beanMock;
        }

        @Override
        protected Integer getIdEntity(TestEntity entity) {
            return entity == null ? null : entity.getId();
        }
    }

    @Mock
    InventarioDefaultDataAccess<TestEntity, Integer> bean;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    TestResource resource;

    @BeforeEach
    void setUp() {
        resource = new TestResource(bean);
    }

    @Test
    void create_nullEntity_returns422() {
        Response r = resource.create(null, uriInfo);
        assertEquals(422, r.getStatus());
    }

    @Test
    void create_entityWithId_returns422() {
        TestEntity e = new TestEntity();
        e.setId(5);
        Response r = resource.create(e, uriInfo);
        assertEquals(422, r.getStatus());
    }

    @Test
    void create_success_returnsCreated() throws Exception {
        TestEntity e = new TestEntity();
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/test/1"));

        // Simulate bean.create assigning id
        doAnswer(invocation -> {
            TestEntity arg = invocation.getArgument(0);
            arg.setId(1);
            return null;
        }).when(bean).create(any(TestEntity.class));

        Response r = resource.create(e, uriInfo);
        assertEquals(Response.Status.CREATED.getStatusCode(), r.getStatus());
        assertNotNull(r.getLocation());
    }

    @Test
    void findRange_invalidParams_returns422() {
        Response r = resource.findRange(-1, 10);
        assertEquals(422, r.getStatus());
    }

    @Test
    void findRange_empty_returns404() {
        when(bean.findRange(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(bean.count()).thenReturn(0);
        Response r = resource.findRange(0, 10);
        assertEquals(404, r.getStatus());
    }

    @Test
    void findRange_success_returns200() {
        List<TestEntity> list = new ArrayList<>();
        TestEntity e = new TestEntity(); e.setId(2); list.add(e);
        when(bean.findRange(0, 10)).thenReturn(list);
        when(bean.count()).thenReturn(1);
        Response r = resource.findRange(0, 10);
        assertEquals(200, r.getStatus());
    }

    @Test
    void findById_null_returns422() {
        Response r = resource.findById(null);
        assertEquals(422, r.getStatus());
    }

    @Test
    void findById_notFound_returns404() {
        when(bean.findById(3)).thenReturn(null);
        Response r = resource.findById(3);
        assertEquals(404, r.getStatus());
    }

    @Test
    void findById_found_returns200() {
        TestEntity e = new TestEntity(); e.setId(4);
        when(bean.findById(4)).thenReturn(e);
        Response r = resource.findById(4);
        assertEquals(200, r.getStatus());
    }

    @Test
    void update_invalid_returns422() {
        Response r1 = resource.update(null);
        assertEquals(422, r1.getStatus());
        TestEntity e = new TestEntity();
        Response r2 = resource.update(e);
        assertEquals(422, r2.getStatus());
    }

    @Test
    void update_success_returns200() {
        TestEntity e = new TestEntity(); e.setId(7);
        when(bean.update(e)).thenReturn(e);
        when(bean.findById(7)).thenReturn(e);
        Response r = resource.update(e);
        assertEquals(200, r.getStatus());
    }

    @Test
    void delete_invalid_returns422() {
        Response r = resource.delete(null);
        assertEquals(422, r.getStatus());
    }

    @Test
    void delete_notFound_returns404() {
        when(bean.findById(10)).thenReturn(null);
        Response r = resource.delete(10);
        assertEquals(404, r.getStatus());
    }

    @Test
    void delete_success_returns200() {
        TestEntity e = new TestEntity(); e.setId(11);
        when(bean.findById(11)).thenReturn(e);
        doNothing().when(bean).delete(e);
        Response r = resource.delete(11);
        assertEquals(200, r.getStatus());
    }
}

