package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Caracteristica;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoUnidadMedida;

import java.net.URI;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaracteristicaResourceTest {

    @Mock
    CaracteristicaDAO caracteristicaDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    CaracteristicaResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        Caracteristica input = new Caracteristica();
        TipoUnidadMedida tipoRef = new TipoUnidadMedida();
        tipoRef.setId(4);
        input.setIdTipoUnidadMedida(tipoRef);

        TipoUnidadMedida found = new TipoUnidadMedida();
        found.setId(4);
        when(caracteristicaDAO.findTipoUnidadMedidaById(4)).thenReturn(found);

        doAnswer(invocation -> {
            Caracteristica c = invocation.getArgument(0);
            c.setId(20);
            return null;
        }).when(caracteristicaDAO).create(any(Caracteristica.class));

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/caracteristica/20"));

        try (Response resp = resource.create(input, uriInfo)) {
            assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
            assertNotNull(resp.getLocation());
            assertTrue(resp.hasEntity());
            Caracteristica returned = (Caracteristica) resp.getEntity();
            assertEquals(20, returned.getId());
        }

        verify(caracteristicaDAO).findTipoUnidadMedidaById(4);
        verify(caracteristicaDAO).create(any(Caracteristica.class));
    }

    @Test
    void create_null_returns422() {
        try (Response resp = resource.create(null, uriInfo)) {
            assertEquals(422, resp.getStatus());
        }
    }

    @Test
    void findById_notFound_returns404() {
        Integer id = 222;
        when(caracteristicaDAO.findById(id)).thenReturn(null);
        try (Response resp = resource.findById(id)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        }
    }

    @Test
    void findRange_returnsOkAndHeader() {
        when(caracteristicaDAO.count()).thenReturn(2);
        when(caracteristicaDAO.findRange(0, 10)).thenReturn(Collections.singletonList(new Caracteristica()));

        try (Response resp = resource.findRange(0, 10)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals("2", resp.getHeaderString("Total-records"));
        }

        verify(caracteristicaDAO).findRange(0, 10);
    }

    @Test
    void delete_existing_returnsNoContent() {
        Caracteristica c = new Caracteristica();
        c.setId(30);
        when(caracteristicaDAO.findById(30)).thenReturn(c);

        try (Response resp = resource.delete(30)) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        }

        verify(caracteristicaDAO).delete(c);
    }

    @Test
    void update_existing_returnsOk() {
        Caracteristica existing = new Caracteristica();
        existing.setId(40);
        when(caracteristicaDAO.findById(40)).thenReturn(existing);

        Caracteristica toUpdate = new Caracteristica();
        TipoUnidadMedida tipoRef = new TipoUnidadMedida();
        tipoRef.setId(7);
        toUpdate.setIdTipoUnidadMedida(tipoRef);

        TipoUnidadMedida found = new TipoUnidadMedida();
        found.setId(7);
        when(caracteristicaDAO.findTipoUnidadMedidaById(7)).thenReturn(found);
        when(caracteristicaDAO.update(any(Caracteristica.class))).thenReturn(existing);

        try (Response resp = resource.update(40, toUpdate)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        }

        verify(caracteristicaDAO).update(any(Caracteristica.class));
    }

    @Test
    void update_missingTipo_returns422() {
        Caracteristica toUpdate = new Caracteristica();
        // stub existing so validation of tipo is reached
        when(caracteristicaDAO.findById(41)).thenReturn(new Caracteristica());
        try (Response resp = resource.update(41, toUpdate)) {
            assertEquals(422, resp.getStatus());
        }
    }

    @Test
    void update_tipoNotFound_returns404() {
        Caracteristica toUpdate = new Caracteristica();
        TipoUnidadMedida tipoRef = new TipoUnidadMedida();
        tipoRef.setId(999);
        toUpdate.setIdTipoUnidadMedida(tipoRef);

        when(caracteristicaDAO.findById(42)).thenReturn(new Caracteristica());
        when(caracteristicaDAO.findTipoUnidadMedidaById(999)).thenReturn(null);

        try (Response resp = resource.update(42, toUpdate)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        }
    }
}
