package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CompraDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import java.net.URI;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraResourceTest {

    @Mock
    CompraDAO compraDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    CompraResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        Compra input = new Compra();
        Proveedor p = new Proveedor();
        p.setId(55);
        input.setIdProveedor(p);

        Proveedor found = new Proveedor();
        found.setId(55);
        when(compraDAO.findProveedorById(55)).thenReturn(found);

        doAnswer(invocation -> {
            Compra c = invocation.getArgument(0);
            c.setId(101L);
            return null;
        }).when(compraDAO).create(any(Compra.class));

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/compra/101"));

        try (Response resp = resource.create(input, uriInfo)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertNotNull(resp.getLocation());
            assertTrue(resp.hasEntity());
            Compra returned = (Compra) resp.getEntity();
            assertEquals(101L, returned.getId());
        }

        verify(compraDAO).findProveedorById(55);
        verify(compraDAO).create(any(Compra.class));
    }

    @Test
    void create_null_returns422() {
        try (Response resp = resource.create(null, uriInfo)) {
            assertEquals(422, resp.getStatus());
        }
    }

    @Test
    void findRange_returnsOkAndHeader() {
        when(compraDAO.count()).thenReturn(4);
        when(compraDAO.findRange(0, 10)).thenReturn(Collections.singletonList(new Compra()));

        try (Response resp = resource.findRange(0, 10)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals("4", resp.getHeaderString("Total-records"));
        }

        verify(compraDAO).findRange(0, 10);
    }

    @Test
    void findById_notFound_returns404() {
        when(compraDAO.findById(500L)).thenReturn(null);
        try (Response resp = resource.findById(500L)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        }
    }

    @Test
    void delete_existing_returnsNoContent() {
        Compra c = new Compra();
        c.setId(200L);
        when(compraDAO.findById(200L)).thenReturn(c);

        try (Response resp = resource.delete(200L)) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        }

        verify(compraDAO).delete(c);
    }

    @Test
    void update_existing_returnsOk() {
        Compra existing = new Compra();
        existing.setId(300L);
        when(compraDAO.findById(300L)).thenReturn(existing);

        Compra toUpdate = new Compra();
        Proveedor ref = new Proveedor();
        ref.setId(77);
        toUpdate.setIdProveedor(ref);

        Proveedor found = new Proveedor();
        found.setId(77);
        when(compraDAO.findProveedorById(77)).thenReturn(found);
        when(compraDAO.update(any(Compra.class))).thenReturn(existing);

        try (Response resp = resource.update(300L, toUpdate)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        }

        verify(compraDAO).update(any(Compra.class));
    }

    @Test
    void update_missingProveedor_returns422() {
        Compra toUpdate = new Compra();
        // stub existing so validation of proveedor is reached
        when(compraDAO.findById(301L)).thenReturn(new Compra());
        try (Response resp = resource.update(301L, toUpdate)) {
            assertEquals(422, resp.getStatus());
        }
    }

    @Test
    void update_proveedorNotFound_returns404() {
        Compra toUpdate = new Compra();
        Proveedor ref = new Proveedor();
        ref.setId(9999);
        toUpdate.setIdProveedor(ref);

        when(compraDAO.findById(302L)).thenReturn(new Compra());
        when(compraDAO.findProveedorById(9999)).thenReturn(null);

        try (Response resp = resource.update(302L, toUpdate)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        }
    }
}
