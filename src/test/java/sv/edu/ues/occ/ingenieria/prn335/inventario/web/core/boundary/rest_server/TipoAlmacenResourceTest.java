package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoAlmacenDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoAlmacen;

import java.net.URI;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoAlmacenResourceTest {

    @Mock
    TipoAlmacenDAO tipoAlmacenDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    TipoAlmacenResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        TipoAlmacen t = new TipoAlmacen();
        // Simular que al persistir se asigna id
        doAnswer(invocation -> {
            TipoAlmacen arg = invocation.getArgument(0);
            arg.setId(123);
            return null;
        }).when(tipoAlmacenDAO).create(any(TipoAlmacen.class));

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/tipoAlmacen/123"));

        try (Response resp = resource.create(t, uriInfo)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertNotNull(resp.getLocation());
            assertTrue(resp.getLocation().toString().contains("123"));
            assertTrue(resp.hasEntity());
            TipoAlmacen returned = (TipoAlmacen) resp.getEntity();
            assertNotNull(returned.getId());
        }

        verify(tipoAlmacenDAO).create(any(TipoAlmacen.class));
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
        when(tipoAlmacenDAO.findById(id)).thenReturn(null);
        try (Response resp = resource.findById(id)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        }
    }

    @Test
    void findRange_success_returnsOkAndHeader() {
        when(tipoAlmacenDAO.count()).thenReturn(5);
        when(tipoAlmacenDAO.findRange(0, 10)).thenReturn(Collections.singletonList(new TipoAlmacen()));

        try (Response resp = resource.findRange(0, 10)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals("5", resp.getHeaderString("Total-records"));
        }

        verify(tipoAlmacenDAO).findRange(0, 10);
    }

    @Test
    void delete_existing_returnsNoContent() {
        TipoAlmacen t = new TipoAlmacen();
        t.setId(7);
        when(tipoAlmacenDAO.findById(7)).thenReturn(t);

        try (Response resp = resource.delete(7)) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        }

        verify(tipoAlmacenDAO).delete(t);
    }

    @Test
    void update_existing_returnsOk() {
        TipoAlmacen existing = new TipoAlmacen();
        existing.setId(8);
        TipoAlmacen toUpdate = new TipoAlmacen();
        when(tipoAlmacenDAO.findById(8)).thenReturn(existing);
        when(tipoAlmacenDAO.update(any(TipoAlmacen.class))).thenReturn(existing);

        try (Response resp = resource.update(8, toUpdate)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        }

        verify(tipoAlmacenDAO).update(any(TipoAlmacen.class));
    }
}
