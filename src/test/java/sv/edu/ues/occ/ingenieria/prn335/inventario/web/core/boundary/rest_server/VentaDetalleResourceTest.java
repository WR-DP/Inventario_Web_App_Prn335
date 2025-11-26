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
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.VentaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.VentaDetalleDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.VentaDetalle;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaDetalleResourceTest {

    @Mock
    VentaDetalleDAO ventaDetalleDAO;

    @Mock
    VentaDAO ventaDAO;

    @Mock
    ProductoDAO productoDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    VentaDetalleResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        UUID ventaId = UUID.randomUUID();
        VentaDetalle entity = new VentaDetalle();
        Producto prodRef = new Producto(); prodRef.setId(UUID.randomUUID());
        entity.setIdProducto(prodRef);

        Venta venta = new Venta(); venta.setId(ventaId);
        Producto producto = new Producto(); producto.setId(prodRef.getId());

        when(ventaDAO.findById(ventaId)).thenReturn(venta);
        when(productoDAO.findById(prodRef.getId())).thenReturn(producto);

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/venta/1/detalle/abcd"));

        doNothing().when(ventaDetalleDAO).create(any(VentaDetalle.class));

        Response resp = resource.create(entity, ventaId, uriInfo);
        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
        assertNotNull(((VentaDetalle)resp.getEntity()).getId());
        verify(ventaDAO).findById(ventaId);
        verify(productoDAO).findById(prodRef.getId());
        verify(ventaDetalleDAO).create(any(VentaDetalle.class));
    }

    @Test
    void create_missingBody_returns422() {
        Response resp = resource.create(null, UUID.randomUUID(), uriInfo);
        assertEquals(422, resp.getStatus());
    }

    @Test
    void findById_success_returns200() {
        UUID id = UUID.randomUUID();
        VentaDetalle vd = new VentaDetalle(); vd.setId(id);
        when(ventaDetalleDAO.findById(id)).thenReturn(vd);
        Response resp = resource.findById(id);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
        assertEquals(id, ((VentaDetalle)resp.getEntity()).getId());
    }

    @Test
    void delete_success_returns204() {
        UUID id = UUID.randomUUID();
        VentaDetalle vd = new VentaDetalle(); vd.setId(id);
        when(ventaDetalleDAO.findById(id)).thenReturn(vd);
        doNothing().when(ventaDetalleDAO).delete(vd);
        Response resp = resource.delete(id);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
        verify(ventaDetalleDAO).delete(vd);
    }

    @Test
    void update_success_returns200() {
        UUID id = UUID.randomUUID();
        VentaDetalle existing = new VentaDetalle(); existing.setId(id);
        Venta venta = new Venta(); venta.setId(UUID.randomUUID());
        existing.setIdVenta(venta);
        when(ventaDetalleDAO.findById(id)).thenReturn(existing);

        VentaDetalle toUpdate = new VentaDetalle();
        Producto prodRef = new Producto(); prodRef.setId(UUID.randomUUID());
        toUpdate.setIdProducto(prodRef);

        Producto producto = new Producto(); producto.setId(prodRef.getId());
        when(productoDAO.findById(prodRef.getId())).thenReturn(producto);

        VentaDetalle updated = new VentaDetalle(); updated.setId(id);
        when(ventaDetalleDAO.update(any(VentaDetalle.class))).thenReturn(updated);

        Response resp = resource.update(id, toUpdate);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
        assertEquals(id, ((VentaDetalle)resp.getEntity()).getId());
        verify(ventaDetalleDAO).update(any(VentaDetalle.class));
    }

    @Test
    void findRange_returnsList_withHeader() {
        when(ventaDetalleDAO.count()).thenReturn(1);
        java.util.List<VentaDetalle> list = java.util.List.of(new VentaDetalle());
        when(ventaDetalleDAO.findRange(0, 100)).thenReturn(list);
        Response resp = resource.findRange(0, 100, UUID.randomUUID());
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
        assertEquals("1", resp.getHeaderString("Total-records"));
    }

}

