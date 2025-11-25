package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.AlmacenDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Almacen;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoAlmacen;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlmacenResourceTest {

    @Mock
    AlmacenDAO almacenDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    AlmacenResource resource;

    @BeforeEach
    void setup() {
    }

    @Test
    void create_success_returnsCreatedAndLocation() throws Exception {
        // Preparar entidad entrante
        Almacen input = new Almacen();
        TipoAlmacen tipoRef = new TipoAlmacen();
        tipoRef.setId(1);
        input.setIdTipoAlmacen(tipoRef);

        // Preparar mocks
        TipoAlmacen foundTipo = new TipoAlmacen();
        foundTipo.setId(1);
        when(almacenDAO.findTipoAlmacenById(1)).thenReturn(foundTipo);

        // Simular que el DAO asigna id al persistir
        doAnswer(invocation -> {
            Almacen a = invocation.getArgument(0);
            a.setId(10);
            return null;
        }).when(almacenDAO).create(any(Almacen.class));

        // Mockear UriBuilder (evitar RuntimeDelegate)
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path("10")).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/almacen/10"));

        Response resp = resource.create(input, uriInfo);

        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertNotNull(resp.getLocation());
        assertTrue(resp.getLocation().toString().contains("/10"));
        // El entity retornado debe ser la misma instancia con id seteado
        assertTrue(resp.hasEntity());
        Almacen returned = (Almacen) resp.getEntity();
        assertEquals(10, returned.getId());

        verify(almacenDAO).findTipoAlmacenById(1);
        verify(almacenDAO).create(any(Almacen.class));
    }

    @Test
    void create_missingTipo_returns422() {
        Almacen input = new Almacen();
        // No set tipo

        // No necesitamos stubear UriInfo aquí
        Response resp = resource.create(input, uriInfo);

        assertEquals(422, resp.getStatus());
    }

    @Test
    void findById_notFound_returns404() {
        Integer id = 5;
        when(almacenDAO.findById(id)).thenReturn(null);

        Response resp = resource.findById(id);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }
}
