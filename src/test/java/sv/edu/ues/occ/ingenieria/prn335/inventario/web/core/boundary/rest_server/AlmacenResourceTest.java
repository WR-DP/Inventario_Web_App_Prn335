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
import java.util.Collections;

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

    @Test
    void findRange_returnsOkAndHeader() {
        when(almacenDAO.count()).thenReturn(12);
        when(almacenDAO.findRange(0, 10)).thenReturn(Collections.singletonList(new Almacen()));

        try (Response resp = resource.findRange(0, 10)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals("12", resp.getHeaderString("Total-records"));
        }

        verify(almacenDAO).findRange(0, 10);
    }

    @Test
    void delete_existing_returnsNoContent() {
        Almacen a = new Almacen();
        a.setId(21);
        when(almacenDAO.findById(21)).thenReturn(a);

        try (Response resp = resource.delete(21)) {
            assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        }

        verify(almacenDAO).delete(a);
    }

    @Test
    void update_existing_returnsOk() {
        Almacen existing = new Almacen();
        existing.setId(22);
        when(almacenDAO.findById(22)).thenReturn(existing);

        Almacen toUpdate = new Almacen();
        TipoAlmacen tipoRef = new TipoAlmacen();
        tipoRef.setId(2);
        toUpdate.setIdTipoAlmacen(tipoRef);

        TipoAlmacen found = new TipoAlmacen();
        found.setId(2);
        when(almacenDAO.findTipoAlmacenById(2)).thenReturn(found);
        when(almacenDAO.update(any(Almacen.class))).thenReturn(existing);

        try (Response resp = resource.update(22, toUpdate)) {
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        }

        verify(almacenDAO).update(any(Almacen.class));
    }

    @Test
    void update_missingTipo_returns422() {
        Almacen toUpdate = new Almacen();
        // stub existing so resource reaches validation of tipo
        when(almacenDAO.findById(23)).thenReturn(new Almacen());
        try (Response resp = resource.update(23, toUpdate)) {
            assertEquals(422, resp.getStatus());
        }
    }

    @Test
    void update_tipoNotFound_returns404() {
        Almacen toUpdate = new Almacen();
        TipoAlmacen tipoRef = new TipoAlmacen();
        tipoRef.setId(9999);
        toUpdate.setIdTipoAlmacen(tipoRef);

        when(almacenDAO.findById(24)).thenReturn(new Almacen());
        when(almacenDAO.findTipoAlmacenById(9999)).thenReturn(null);

        try (Response resp = resource.update(24, toUpdate)) {
            assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
        }
    }
}
