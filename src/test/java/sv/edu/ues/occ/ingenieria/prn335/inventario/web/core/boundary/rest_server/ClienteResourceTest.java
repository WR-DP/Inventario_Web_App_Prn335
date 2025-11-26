package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ClienteDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;

import java.net.URI;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteResourceTest {

    @Mock
    ClienteDAO clienteDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    ClienteResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        Cliente c = new Cliente();
        doAnswer(invocation -> {
            Cliente arg = invocation.getArgument(0);
            arg.setId(UUID.fromString("00000000-0000-0000-0000-0000000000bb"));
            return null;
        }).when(clienteDAO).create(any(Cliente.class));

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/cliente/00000000-0000-0000-0000-0000000000bb"));

        try (Response resp = resource.create(c, uriInfo)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertNotNull(resp.getLocation());
            assertTrue(resp.getLocation().toString().contains("00000000-0000-0000-0000-0000000000bb"));
            assertTrue(resp.hasEntity());
            Cliente returned = (Cliente) resp.getEntity();
            assertNotNull(returned.getId());
        }

        verify(clienteDAO).create(any(Cliente.class));
    }

    @Test
    void create_null_returns422() {
        try (Response resp = resource.create(null, uriInfo)) {
            assertEquals(422, resp.getStatus());
        }
    }

    @Test
    void findById_notFound_returns404() {
        UUID id = UUID.randomUUID();
        when(clienteDAO.findById(id)).thenReturn(null);
        try (Response resp = resource.findById(id)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        }
    }

    @Test
    void findRange_success_returnsOkAndHeader() {
        when(clienteDAO.count()).thenReturn(3);
        when(clienteDAO.findRange(0, 10)).thenReturn(Collections.singletonList(new Cliente()));

        try (Response resp = resource.findRange(0, 10)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals("3", resp.getHeaderString("Total-records"));
        }

        verify(clienteDAO).findRange(0, 10);
    }

    @Test
    void delete_existing_returnsNoContent() {
        Cliente c = new Cliente();
        UUID id = UUID.randomUUID();
        c.setId(id);
        when(clienteDAO.findById(id)).thenReturn(c);

        try (Response resp = resource.delete(id)) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        }

        verify(clienteDAO).delete(c);
    }

    @Test
    void update_existing_returnsOk() {
        Cliente existing = new Cliente();
        UUID id = UUID.randomUUID();
        existing.setId(id);
        Cliente toUpdate = new Cliente();
        when(clienteDAO.findById(id)).thenReturn(existing);
        when(clienteDAO.update(any(Cliente.class))).thenReturn(existing);

        try (Response resp = resource.update(id, toUpdate)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        }

        verify(clienteDAO).update(any(Cliente.class));
    }
}
