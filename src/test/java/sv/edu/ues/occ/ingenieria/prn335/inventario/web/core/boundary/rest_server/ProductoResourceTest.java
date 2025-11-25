package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoResourceTest {

    @Mock
    ProductoDAO productoDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    ProductoResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        Producto p = new Producto();
        // Simular que al persistir se asigna id
        doAnswer(invocation -> {
            Producto arg = invocation.getArgument(0);
            arg.setId(UUID.fromString("00000000-0000-0000-0000-0000000000aa"));
            return null;
        }).when(productoDAO).create(any(Producto.class));

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/producto/00000000-0000-0000-0000-0000000000aa"));

        Response resp = resource.create(p, uriInfo);
        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertNotNull(resp.getLocation());
        assertTrue(resp.getLocation().toString().contains("00000000-0000-0000-0000-0000000000aa"));
        assertTrue(resp.hasEntity());
        Producto returned = (Producto) resp.getEntity();
        assertNotNull(returned.getId());

        verify(productoDAO).create(any(Producto.class));
    }

    @Test
    void create_null_returns422() {
        Response resp = resource.create(null, uriInfo);
        assertEquals(422, resp.getStatus());
    }

    @Test
    void findById_notFound_returns404() {
        UUID id = UUID.randomUUID();
        when(productoDAO.findById(id)).thenReturn(null);
        Response resp = resource.findById(id);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }
}

