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
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoTipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.ProductoTipoProducto;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoTipoProductoResourceTest {

    @Mock
    ProductoTipoProductoDAO productoTipoProductoDAO;

    @Mock
    ProductoDAO productoDAO;

    @Mock
    TipoProductoDAO tipoProductoDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    ProductoTipoProductoResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        UUID productoId = UUID.randomUUID();
        ProductoTipoProducto entity = new ProductoTipoProducto();

        TipoProducto tpRef = new TipoProducto(); tpRef.setId(1L);
        entity.setIdTipoProducto(tpRef);

        Producto producto = new Producto(); producto.setId(productoId);
        when(productoDAO.findById(productoId)).thenReturn(producto);
        when(tipoProductoDAO.findById(1L)).thenReturn(tpRef);

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/producto/1/tipoProducto/abcd"));

        doNothing().when(productoTipoProductoDAO).create(any(ProductoTipoProducto.class));

        Response resp = resource.create(entity, productoId, uriInfo);
        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
        assertNotNull(((ProductoTipoProducto)resp.getEntity()).getId());
        verify(productoDAO).findById(productoId);
        verify(tipoProductoDAO).findById(1L);
        verify(productoTipoProductoDAO).create(any(ProductoTipoProducto.class));
    }

    @Test
    void findById_success_returns200() {
        UUID id = UUID.randomUUID();
        ProductoTipoProducto p = new ProductoTipoProducto(); p.setId(id);
        when(productoTipoProductoDAO.findById(id)).thenReturn(p);
        Response resp = resource.findById(id);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
    }

    @Test
    void delete_success_returns204() {
        UUID id = UUID.randomUUID();
        ProductoTipoProducto p = new ProductoTipoProducto(); p.setId(id);
        when(productoTipoProductoDAO.findById(id)).thenReturn(p);
        doNothing().when(productoTipoProductoDAO).delete(p);
        Response resp = resource.delete(id);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        verify(productoTipoProductoDAO).delete(p);
    }

    @Test
    void update_success_returns200() {
        UUID id = UUID.randomUUID();
        ProductoTipoProducto existing = new ProductoTipoProducto(); existing.setId(id);
        Producto producto = new Producto(); producto.setId(UUID.randomUUID());
        existing.setIdProducto(producto);
        when(productoTipoProductoDAO.findById(id)).thenReturn(existing);

        ProductoTipoProducto toUpdate = new ProductoTipoProducto();
        TipoProducto tpRef = new TipoProducto(); tpRef.setId(2L);
        toUpdate.setIdTipoProducto(tpRef);

        when(tipoProductoDAO.findById(2L)).thenReturn(tpRef);
        ProductoTipoProducto updated = new ProductoTipoProducto(); updated.setId(id);
        when(productoTipoProductoDAO.update(any(ProductoTipoProducto.class))).thenReturn(updated);

        Response resp = resource.update(id, toUpdate);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
        assertEquals(id, ((ProductoTipoProducto)resp.getEntity()).getId());
    }

    @Test
    void findRange_returnsList_withHeader() {
        when(productoTipoProductoDAO.count()).thenReturn(1);
        java.util.List<ProductoTipoProducto> list = java.util.List.of(new ProductoTipoProducto());
        when(productoTipoProductoDAO.findRange(0, 100)).thenReturn(list);
        Response resp = resource.findRange(0,100, UUID.randomUUID());
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertEquals("1", resp.getHeaderString("Total-records"));
    }

}

