package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoUnidadMedidaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoUnidadMedida;

import java.net.URI;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoUnidadMedidaResourceTest {

    @Mock
    TipoUnidadMedidaDAO tipoUnidadMedidaDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    TipoUnidadMedidaResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        TipoUnidadMedida t = new TipoUnidadMedida();
        doAnswer(invocation -> {
            TipoUnidadMedida arg = invocation.getArgument(0);
            arg.setId(77);
            return null;
        }).when(tipoUnidadMedidaDAO).create(any(TipoUnidadMedida.class));

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/tipoUnidadMedida/77"));

        try (Response resp = resource.create(t, uriInfo)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertNotNull(resp.getLocation());
            assertTrue(resp.getLocation().toString().contains("77"));
            assertTrue(resp.hasEntity());
            TipoUnidadMedida returned = (TipoUnidadMedida) resp.getEntity();
            assertNotNull(returned.getId());
        }

        verify(tipoUnidadMedidaDAO).create(any(TipoUnidadMedida.class));
    }

    @Test
    void create_null_returns422() {
        try (Response resp = resource.create(null, uriInfo)) {
            assertEquals(422, resp.getStatus());
        }
    }

    @Test
    void findById_notFound_returns404() {
        Integer id = 9999;
        when(tipoUnidadMedidaDAO.findById(id)).thenReturn(null);
        try (Response resp = resource.findById(id)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        }
    }

    @Test
    void findRange_success_returnsOkAndHeader() {
        when(tipoUnidadMedidaDAO.count()).thenReturn(2);
        when(tipoUnidadMedidaDAO.findRange(0, 10)).thenReturn(Collections.singletonList(new TipoUnidadMedida()));

        try (Response resp = resource.findRange(0, 10)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals("2", resp.getHeaderString("Total-records"));
        }

        verify(tipoUnidadMedidaDAO).findRange(0, 10);
    }

    @Test
    void delete_existing_returnsNoContent() {
        TipoUnidadMedida t = new TipoUnidadMedida();
        t.setId(5);
        when(tipoUnidadMedidaDAO.findById(5)).thenReturn(t);

        try (Response resp = resource.delete(5)) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        }

        verify(tipoUnidadMedidaDAO).delete(t);
    }

    @Test
    void update_existing_returnsOk() {
        TipoUnidadMedida existing = new TipoUnidadMedida();
        existing.setId(6);
        TipoUnidadMedida toUpdate = new TipoUnidadMedida();
        when(tipoUnidadMedidaDAO.findById(6)).thenReturn(existing);
        when(tipoUnidadMedidaDAO.update(any(TipoUnidadMedida.class))).thenReturn(existing);

        try (Response resp = resource.update(6, toUpdate)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        }

        verify(tipoUnidadMedidaDAO).update(any(TipoUnidadMedida.class));
    }
}
