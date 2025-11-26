package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoProductoResourceTest {

    @Mock
    TipoProductoDAO tipoProductoDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    TipoProductoResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        TipoProducto t = new TipoProducto();
        // id must be null before create
        t.setId(null);
        doAnswer(invocation -> {
            TipoProducto arg = invocation.getArgument(0);
            arg.setId(555L);
            return null;
        }).when(tipoProductoDAO).create(any(TipoProducto.class));

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/tipoProducto/555"));

        try (Response resp = resource.create(t, uriInfo)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertNotNull(resp.getLocation());
            assertTrue(resp.getLocation().toString().contains("555"));
        }

        verify(tipoProductoDAO).create(any(TipoProducto.class));
    }

    @Test
    void create_withParentNotFound_returns422() {
        TipoProducto t = new TipoProducto();
        TipoProducto padreRef = new TipoProducto();
        padreRef.setId(999L);
        t.setIdTipoProductoPadre(padreRef);

        when(tipoProductoDAO.findById(999L)).thenReturn(null);

        try (Response resp = resource.create(t, uriInfo)) {
            assertEquals(422, resp.getStatus());
        }
    }

    @Test
    void create_withNonNullId_returns422() {
        TipoProducto t = new TipoProducto();
        t.setId(1L);
        try (Response resp = resource.create(t, uriInfo)) {
            assertEquals(422, resp.getStatus());
        }
    }
}
