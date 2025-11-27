package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.primefaces.model.LazyDataModel;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.*;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Producto;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductoFrmTest {

    @InjectMocks
    ProductoFrm frm;

    @Mock
    FacesContext facesContext;

    @Mock
    ProductoDAO productoDAO;

    @Mock
    ProductoTipoProductoFrm productoTipoProductoFrm;

    @Mock
    DeleteManager deleteManager;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        frm = new ProductoFrm();
        frm.facesContext = facesContext;
        frm.productoDAO = productoDAO;
        frm.productoTipoProductoFrm = productoTipoProductoFrm;
        frm.deleteManager = deleteManager;

        frm.modelo = new LazyDataModel<Producto>() {
            @Override
            public List<Producto> load(int first, int pageSize, Map sort, Map filter) {
                return List.of();
            }

            @Override
            public int count(Map filter) {
                return 0;
            }
        };
    }

    @Test
    void testGetIdAsText() {
        Producto p = new Producto();
        UUID id = UUID.randomUUID();
        p.setId(id);

        assertEquals(id.toString(), frm.getIdAsText(p));
    }

    @Test
    void testGetIdAsTextNull() {
        assertNull(frm.getIdAsText(null));
    }

    @Test
    void testGetIdByTextFound() {
        UUID id = UUID.randomUUID();
        Producto p = new Producto();
        p.setId(id);

        frm.modelo.setWrappedData(List.of(p));

        assertEquals(p, frm.getIdByText(id.toString()));
    }

    @Test
    void testGetIdByTextNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText(UUID.randomUUID().toString()));
    }

    @Test
    void testGetIdByTextInvalidUUID() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText("NO_VALIDO"));
    }

    @Test
    void testInicializar() {
        when(productoDAO.findRange(0, Integer.MAX_VALUE))
                .thenReturn(List.of(new Producto()));

        frm.inicializar();

        assertNotNull(frm.getListaProductos());
        assertEquals(1, frm.getListaProductos().size());
    }

    @Test
    void testNuevoRegistro() {
        Producto p = frm.nuevoRegistro();

        assertNotNull(p.getId());
        assertEquals("", p.getNombreProducto());
        assertTrue(p.getActivo());
        assertEquals("", p.getComentarios());
    }

    @Test
    void testBuscarRegistroPorId() {
        UUID id = UUID.randomUUID();
        Producto p = new Producto();
        p.setId(id);

        frm.modelo.setWrappedData(List.of(p));

        assertEquals(p, frm.buscarRegistroPorId(id));
    }

    @Test
    void testBuscarRegistroPorIdNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.buscarRegistroPorId(UUID.randomUUID()));
    }

    @Test
    void testGetProductoTipoProductoFrm() {
        UUID id = UUID.randomUUID();

        Producto p = new Producto();
        p.setId(id);
        frm.registro = p;

        doNothing().when(productoTipoProductoFrm).setIdProducto(id);

        assertNotNull(frm.getProductoTipoProductoFrm());
        verify(productoTipoProductoFrm).setIdProducto(id);
    }


    @Test
    void testBtnEliminarHandlerSinRegistro() {
        frm.registro = null;

        frm.btnEliminarHandler(mock(ActionEvent.class));

        verify(facesContext).addMessage(any(), any(FacesMessage.class));
        verifyNoInteractions(deleteManager);
        verifyNoInteractions(productoDAO);
    }

    @Test
    void testBtnEliminarHandlerConRelaciones() {

        UUID id = UUID.randomUUID();
        Producto p = new Producto();
        p.setId(id);
        frm.registro = p;

        when(deleteManager.contarRelacionesProducto(id)).thenReturn(3);
        when(deleteManager.contarCaracteristicasDeProducto(id)).thenReturn(2);

        frm.btnEliminarHandler(mock(ActionEvent.class));

        verify(deleteManager).eliminarRelacionesProducto(id);
        verify(productoDAO).delete(p);

        verify(facesContext, atLeastOnce()).addMessage(any(), any());
    }


    @Test
    void testBtnEliminarHandlerExitoso() throws Exception {
        UUID id = UUID.randomUUID();

        Producto p = new Producto();
        p.setId(id);
        frm.registro = p;

        when(deleteManager.contarRelacionesProducto(id)).thenReturn(0);
        when(deleteManager.contarCaracteristicasDeProducto(id)).thenReturn(0);

        frm.btnEliminarHandler(mock(ActionEvent.class));

        verify(deleteManager).eliminarRelacionesProducto(id);
        verify(productoDAO).delete(p);
        verify(facesContext, atLeastOnce()).addMessage(any(), any());
    }
}
