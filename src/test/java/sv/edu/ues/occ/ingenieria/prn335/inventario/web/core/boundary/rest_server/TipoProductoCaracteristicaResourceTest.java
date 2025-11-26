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
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoCaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Caracteristica;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProductoCaracteristica;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoProductoCaracteristicaResourceTest {

    @Mock
    TipoProductoCaracteristicaDAO tipoProductoCaracteristicaDAO;

    @Mock
    TipoProductoDAO tipoProductoDAO;

    @Mock
    CaracteristicaDAO caracteristicaDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    TipoProductoCaracteristicaResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        Long tipoId = 1L;
        TipoProductoCaracteristica entity = new TipoProductoCaracteristica();
        Caracteristica cRef = new Caracteristica(); cRef.setId(2);
        entity.setIdCaracteristica(cRef);

        TipoProducto tp = new TipoProducto(); tp.setId(tipoId);
        when(tipoProductoDAO.findById(tipoId)).thenReturn(tp);
        when(caracteristicaDAO.findById(2)).thenReturn(cRef);

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/tipo/1/car/abcd"));

        // Mock create to simulate JPA assigning an ID during persist
        doAnswer(invocation -> {
            TipoProductoCaracteristica arg = invocation.getArgument(0);
            arg.setId(100L);
            return null;
        }).when(tipoProductoCaracteristicaDAO).create(any(TipoProductoCaracteristica.class));

        Response resp = resource.create(entity, tipoId, uriInfo);
        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
        verify(tipoProductoDAO).findById(tipoId);
        verify(caracteristicaDAO).findById(2);
        verify(tipoProductoCaracteristicaDAO).create(any(TipoProductoCaracteristica.class));
    }

    @Test
    void findById_success_returns200() {
        Long id = 1L;
        TipoProductoCaracteristica t = new TipoProductoCaracteristica(); t.setId(id);
        when(tipoProductoCaracteristicaDAO.findById(id)).thenReturn(t);
        Response resp = resource.findById(id);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
    }

    @Test
    void delete_success_returns204() {
        Long id = 1L;
        TipoProductoCaracteristica t = new TipoProductoCaracteristica(); t.setId(id);
        when(tipoProductoCaracteristicaDAO.findById(id)).thenReturn(t);
        doNothing().when(tipoProductoCaracteristicaDAO).delete(t);
        Response resp = resource.delete(id);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        verify(tipoProductoCaracteristicaDAO).delete(t);
    }

    @Test
    void update_success_returns200() {
        Long id = 1L;
        TipoProductoCaracteristica existing = new TipoProductoCaracteristica(); existing.setId(id);
        TipoProducto tp = new TipoProducto(); tp.setId(5L);
        existing.setIdTipoProducto(tp);
        when(tipoProductoCaracteristicaDAO.findById(id)).thenReturn(existing);

        TipoProductoCaracteristica toUpdate = new TipoProductoCaracteristica();
        Caracteristica cRef = new Caracteristica(); cRef.setId(7);
        toUpdate.setIdCaracteristica(cRef);

        when(caracteristicaDAO.findById(7)).thenReturn(cRef);
        TipoProductoCaracteristica updated = new TipoProductoCaracteristica(); updated.setId(id);
        when(tipoProductoCaracteristicaDAO.update(any(TipoProductoCaracteristica.class))).thenReturn(updated);

        Response resp = resource.update(id, toUpdate);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
        assertEquals(id, ((TipoProductoCaracteristica)resp.getEntity()).getId());
    }

    @Test
    void findRange_returnsList_withHeader() {
        when(tipoProductoCaracteristicaDAO.count()).thenReturn(1);
        java.util.List<TipoProductoCaracteristica> list = java.util.List.of(new TipoProductoCaracteristica());
        when(tipoProductoCaracteristicaDAO.findRange(0, 100)).thenReturn(list);
        Response resp = resource.findRange(0,100, 1L);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertEquals("1", resp.getHeaderString("Total-records"));
    }

}
