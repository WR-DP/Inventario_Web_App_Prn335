package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.UnidadMedidaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoUnidadMedida;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.UnidadMedida;

import java.net.URI;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnidadMedidaResourceTest {

    @Mock
    UnidadMedidaDAO unidadMedidaDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    UnidadMedidaResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        UnidadMedida input = new UnidadMedida();
        TipoUnidadMedida tipoRef = new TipoUnidadMedida();
        tipoRef.setId(2);
        input.setIdTipoUnidadMedida(tipoRef);

        TipoUnidadMedida found = new TipoUnidadMedida();
        found.setId(2);
        when(unidadMedidaDAO.findTipoUnidadMedidaById(2)).thenReturn(found);

        doAnswer(invocation -> {
            UnidadMedida u = invocation.getArgument(0);
            u.setId(5);
            return null;
        }).when(unidadMedidaDAO).create(any(UnidadMedida.class));

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/unidadMedida/5"));

        try (Response resp = resource.create(input, uriInfo)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertNotNull(resp.getLocation());
            assertTrue(resp.hasEntity());
            UnidadMedida returned = (UnidadMedida) resp.getEntity();
            assertEquals(5, returned.getId());
        }

        verify(unidadMedidaDAO).findTipoUnidadMedidaById(2);
        verify(unidadMedidaDAO).create(any(UnidadMedida.class));
    }

    @Test
    void create_null_returns422() {
        try (Response resp = resource.create(null, uriInfo)) {
            assertEquals(422, resp.getStatus());
        }
    }

    @Test
    void findById_notFound_returns404() {
        Integer id = 123;
        when(unidadMedidaDAO.findById(id)).thenReturn(null);
        try (Response resp = resource.findById(id)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        }
    }

    @Test
    void findRange_returnsOkAndHeader() {
        when(unidadMedidaDAO.count()).thenReturn(7);
        when(unidadMedidaDAO.findRange(0, 10)).thenReturn(Collections.singletonList(new UnidadMedida()));

        try (Response resp = resource.findRange(0, 10)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals("7", resp.getHeaderString("Total-records"));
        }

        verify(unidadMedidaDAO).findRange(0, 10);
    }

    @Test
    void delete_existing_returnsNoContent() {
        UnidadMedida u = new UnidadMedida();
        u.setId(9);
        when(unidadMedidaDAO.findById(9)).thenReturn(u);

        try (Response resp = resource.delete(9)) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        }

        verify(unidadMedidaDAO).delete(u);
    }

    @Test
    void update_existing_returnsOk() {
        UnidadMedida existing = new UnidadMedida();
        existing.setId(11);
        when(unidadMedidaDAO.findById(11)).thenReturn(existing);

        UnidadMedida toUpdate = new UnidadMedida();
        TipoUnidadMedida tipoRef = new TipoUnidadMedida();
        tipoRef.setId(3);
        toUpdate.setIdTipoUnidadMedida(tipoRef);

        TipoUnidadMedida found = new TipoUnidadMedida();
        found.setId(3);
        when(unidadMedidaDAO.findTipoUnidadMedidaById(3)).thenReturn(found);
        when(unidadMedidaDAO.update(any(UnidadMedida.class))).thenReturn(existing);

        try (Response resp = resource.update(11, toUpdate)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        }

        verify(unidadMedidaDAO).update(any(UnidadMedida.class));
    }

    @Test
    void update_missingTipo_returns422() {
        UnidadMedida toUpdate = new UnidadMedida();
        // Stub existing so resource reaches validation of tipo
        when(unidadMedidaDAO.findById(12)).thenReturn(new UnidadMedida());
        try (Response resp = resource.update(12, toUpdate)) {
            assertEquals(422, resp.getStatus());
        }
    }

    @Test
    void update_tipoNotFound_returns404() {
        UnidadMedida toUpdate = new UnidadMedida();
        TipoUnidadMedida tipoRef = new TipoUnidadMedida();
        tipoRef.setId(99);
        toUpdate.setIdTipoUnidadMedida(tipoRef);

        when(unidadMedidaDAO.findById(13)).thenReturn(new UnidadMedida());
        when(unidadMedidaDAO.findTipoUnidadMedidaById(99)).thenReturn(null);

        try (Response resp = resource.update(13, toUpdate)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        }
    }
}
