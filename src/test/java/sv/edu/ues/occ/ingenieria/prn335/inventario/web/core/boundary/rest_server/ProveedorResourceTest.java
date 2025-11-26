package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProveedorDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import java.net.URI;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorResourceTest {

    @Mock
    ProveedorDAO proveedorDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    ProveedorResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        Proveedor p = new Proveedor();
        doAnswer(invocation -> {
            Proveedor arg = invocation.getArgument(0);
            arg.setId(42);
            return null;
        }).when(proveedorDAO).create(any(Proveedor.class));

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/proveedor/42"));

        try (Response resp = resource.create(p, uriInfo)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertNotNull(resp.getLocation());
            assertTrue(resp.getLocation().toString().contains("42"));
            assertTrue(resp.hasEntity());
            Proveedor returned = (Proveedor) resp.getEntity();
            assertNotNull(returned.getId());
        }

        verify(proveedorDAO).create(any(Proveedor.class));
    }

    @Test
    void create_null_returns422() {
        try (Response resp = resource.create(null, uriInfo)) {
            assertEquals(422, resp.getStatus());
        }
    }

    @Test
    void findById_notFound_returns404() {
        Integer id = 999;
        when(proveedorDAO.findById(id)).thenReturn(null);
        try (Response resp = resource.findById(id)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        }
    }

    @Test
    void findRange_success_returnsOkAndHeader() {
        when(proveedorDAO.count()).thenReturn(6);
        when(proveedorDAO.findRange(0, 10)).thenReturn(Collections.singletonList(new Proveedor()));

        try (Response resp = resource.findRange(0, 10)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals("6", resp.getHeaderString("Total-records"));
        }

        verify(proveedorDAO).findRange(0, 10);
    }

    @Test
    void delete_existing_returnsNoContent() {
        Proveedor p = new Proveedor();
        p.setId(99);
        when(proveedorDAO.findById(99)).thenReturn(p);

        try (Response resp = resource.delete(99)) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        }

        verify(proveedorDAO).delete(p);
    }

    @Test
    void update_existing_returnsOk() {
        Proveedor existing = new Proveedor();
        existing.setId(100);
        Proveedor toUpdate = new Proveedor();
        when(proveedorDAO.findById(100)).thenReturn(existing);
        when(proveedorDAO.update(any(Proveedor.class))).thenReturn(existing);

        try (Response resp = resource.update(100, toUpdate)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        }

        verify(proveedorDAO).update(any(Proveedor.class));
    }
}

