package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoTipoProductoCaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoTipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoCaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.ProductoTipoProducto;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.ProductoTipoProductoCaracteristica;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProductoCaracteristica;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoTipoProductoCaracteristicaResourceTest {

    @Mock
    ProductoTipoProductoCaracteristicaDAO ptpcDAO;

    @Mock
    ProductoTipoProductoDAO productoTipoProductoDAO;

    @Mock
    TipoProductoCaracteristicaDAO tipoProductoCaracteristicaDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    ProductoTipoProductoCaracteristicaResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        Long parentId = 1L;
        ProductoTipoProductoCaracteristica entity = new ProductoTipoProductoCaracteristica();
        TipoProductoCaracteristica tpcRef = new TipoProductoCaracteristica(); tpcRef.setId(2L);
        entity.setIdTipoProductoCaracteristica(tpcRef);

        ProductoTipoProducto ptp = new ProductoTipoProducto(); ptp.setId(UUID.randomUUID());
        when(productoTipoProductoDAO.findById(parentId)).thenReturn(ptp);
        when(tipoProductoCaracteristicaDAO.findById(2L)).thenReturn(tpcRef);

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/ptp/1/tpc/abcd"));

        doNothing().when(ptpcDAO).create(any(ProductoTipoProductoCaracteristica.class));

        Response resp = resource.create(entity, parentId, uriInfo);
        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
        assertNotNull(((ProductoTipoProductoCaracteristica)resp.getEntity()).getId());
        verify(productoTipoProductoDAO).findById(parentId);
        verify(tipoProductoCaracteristicaDAO).findById(2L);
        verify(ptpcDAO).create(any(ProductoTipoProductoCaracteristica.class));
    }

    @Test
    void findById_success_returns200() {
        UUID id = UUID.randomUUID();
        ProductoTipoProductoCaracteristica p = new ProductoTipoProductoCaracteristica(); p.setId(id);
        when(ptpcDAO.findById(id)).thenReturn(p);
        Response resp = resource.findById(id);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
    }

    @Test
    void delete_success_returns204() {
        UUID id = UUID.randomUUID();
        ProductoTipoProductoCaracteristica p = new ProductoTipoProductoCaracteristica(); p.setId(id);
        when(ptpcDAO.findById(id)).thenReturn(p);
        doNothing().when(ptpcDAO).delete(p);
        Response resp = resource.delete(id);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        verify(ptpcDAO).delete(p);
    }

    @Test
    void update_success_returns200() {
        UUID id = UUID.randomUUID();
        ProductoTipoProductoCaracteristica existing = new ProductoTipoProductoCaracteristica(); existing.setId(id);
        ProductoTipoProducto ptp = new ProductoTipoProducto(); ptp.setId(UUID.randomUUID());
        existing.setIdProductoTipoProducto(ptp);
        when(ptpcDAO.findById(id)).thenReturn(existing);

        ProductoTipoProductoCaracteristica toUpdate = new ProductoTipoProductoCaracteristica();
        TipoProductoCaracteristica tpcRef = new TipoProductoCaracteristica(); tpcRef.setId(3L);
        toUpdate.setIdTipoProductoCaracteristica(tpcRef);

        when(tipoProductoCaracteristicaDAO.findById(3L)).thenReturn(tpcRef);
        ProductoTipoProductoCaracteristica updated = new ProductoTipoProductoCaracteristica(); updated.setId(id);
        when(ptpcDAO.update(any(ProductoTipoProductoCaracteristica.class))).thenReturn(updated);

        Response resp = resource.update(id, toUpdate);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
        assertEquals(id, ((ProductoTipoProductoCaracteristica)resp.getEntity()).getId());
    }

    @Test
    void findRange_returnsList_withHeader() {
        when(ptpcDAO.count()).thenReturn(1);
        java.util.List<ProductoTipoProductoCaracteristica> list = java.util.List.of(new ProductoTipoProductoCaracteristica());
        when(ptpcDAO.findRange(0, 100)).thenReturn(list);
        Response resp = resource.findRange(0, 100, 1L);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertEquals("1", resp.getHeaderString("Total-records"));
    }

}

