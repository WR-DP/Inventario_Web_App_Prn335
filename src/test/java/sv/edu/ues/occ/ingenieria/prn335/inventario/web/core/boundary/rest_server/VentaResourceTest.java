package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.VentaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaResourceTest {

    @Mock
    VentaDAO ventaDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    VentaResource resource;

    @Test
    void create_success_assignsIdAndReturnsCreated() throws Exception {
        Venta input = new Venta();
        Cliente c = new Cliente();
        UUID clientId = UUID.randomUUID();
        c.setId(clientId);
        input.setIdCliente(c);

        Cliente found = new Cliente();
        found.setId(clientId);
        when(ventaDAO.findClienteById(clientId)).thenReturn(found);

        doAnswer(invocation -> {
            Venta v = invocation.getArgument(0);
            v.setId(UUID.fromString("00000000-0000-0000-0000-0000000000aa"));
            return null;
        }).when(ventaDAO).create(any(Venta.class));

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/venta/00000000-0000-0000-0000-0000000000aa"));

        Response resp = resource.create(input, uriInfo);

        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertNotNull(resp.getLocation());
        assertTrue(resp.hasEntity());
        Venta returned = (Venta) resp.getEntity();
        assertNotNull(returned.getId());

        verify(ventaDAO).findClienteById(clientId);
        verify(ventaDAO).create(any(Venta.class));
    }

    @Test
    void create_missingCliente_returns422() {
        Venta input = new Venta();

        Response resp = resource.create(input, uriInfo);

        assertEquals(422, resp.getStatus());
    }

    @Test
    void findRange_invalidParameters_returns422() {
        Response resp = resource.findRange(-1, 200);
        assertEquals(422, resp.getStatus());
    }
}

