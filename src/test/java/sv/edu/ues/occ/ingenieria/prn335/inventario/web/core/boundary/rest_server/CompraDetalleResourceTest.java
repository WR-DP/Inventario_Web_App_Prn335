package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.rest_server;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CompraDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CompraDetalleDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraDetalleResourceTest {

    @Mock
    CompraDetalleDAO compraDetalleDAO;

    @Mock
    CompraDAO compraDAO;

    @Mock
    ProductoDAO productoDAO;

    @Mock
    UriInfo uriInfo;

    @Mock
    UriBuilder uriBuilder;

    @InjectMocks
    CompraDetalleResource resource;

    @Test
    void create_success_returnsCreated() throws Exception {
        Long compraId = 1L;
        CompraDetalle entity = new CompraDetalle();
        Producto prodRef = new Producto();
        prodRef.setId(UUID.randomUUID());
        entity.setIdProducto(prodRef);

        Compra compra = new Compra(); compra.setId(compraId);
        Producto producto = new Producto(); producto.setId(prodRef.getId());

        when(compraDAO.findById(compraId)).thenReturn(compra);
        when(productoDAO.findById(prodRef.getId())).thenReturn(producto);

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(new URI("http://localhost/compra/1/compraDetalle/abcd"));

        // create does not need to set id via DAO since resource assigns UUID; mock create to do nothing
        doNothing().when(compraDetalleDAO).create(any(CompraDetalle.class));

        Response resp = resource.create(entity, compraId, uriInfo);
        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertTrue(resp.hasEntity());
        assertNotNull(((CompraDetalle)resp.getEntity()).getId());
        verify(compraDAO).findById(compraId);
        verify(productoDAO).findById(prodRef.getId());
        verify(compraDetalleDAO).create(any(CompraDetalle.class));
    }

    @Test
    void create_missingBody_returns422() {
        Response resp = resource.create(null, 1L, uriInfo);
        assertEquals(422, resp.getStatus());
    }

    @Test
    void create_compraNotFound_returns404() {
        Long compraId = 2L;
        CompraDetalle entity = new CompraDetalle();
        entity.setIdProducto(null);
        when(compraDAO.findById(compraId)).thenReturn(null);
        Response resp = resource.create(entity, compraId, uriInfo);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }
}

