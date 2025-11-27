package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.*;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.DeleteManager;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoProductoDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoProducto;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TipoProductoFrmTest {

    @InjectMocks
    TipoProductoFrm frm;

    @Mock
    FacesContext facesContext;

    @Mock
    TipoProductoDAO tipoProductoDAO;

    @Mock
    TipoProductoCaracteristicaFrm tipoProductoCaracteristicaFrm;

    @Mock
    DeleteManager deleteManager;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        frm = new TipoProductoFrm();
        frm.facesContext = facesContext;
        frm.tipoProductoDAO = tipoProductoDAO;
        frm.tipoProductoCaracteristicaFrm = tipoProductoCaracteristicaFrm;
        frm.deleteManager = deleteManager;

        frm.modelo = new LazyDataModel<TipoProducto>() {

            @Override
            public List<TipoProducto> load(int first, int pageSize,
                                           Map<String, SortMeta> sortBy,
                                           Map<String, FilterMeta> filterBy) {
                return List.of();
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return 0;
            }
        };


    }

    @Test
    void testNuevoRegistro() {
        TipoProducto t = frm.nuevoRegistro();

        assertNotNull(t);
        assertTrue(t.getActivo());
        assertEquals("Comentario", t.getComentarios());
    }

    @Test
    void testGetIdAsText() {
        TipoProducto t = new TipoProducto();
        t.setId(10L);

        assertEquals("10", frm.getIdAsText(t));
    }

    @Test
    void testGetIdAsTextNull() {
        assertNull(frm.getIdAsText(null));
    }

    @Test
    void testGetIdByTextFound() {
        TipoProducto t = new TipoProducto();
        t.setId(5L);

        frm.modelo.setWrappedData(List.of(t));

        assertEquals(t, frm.getIdByText("5"));
    }

    @Test
    void testGetIdByTextNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText("100"));
    }

    @Test
    void testGetIdByTextInvalidFormat() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText("NO_NUMBER"));
    }

    @Test
    void testInicializar() {
        when(tipoProductoDAO.findRange(0, Integer.MAX_VALUE))
                .thenReturn(List.of(new TipoProducto()));

        when(tipoProductoDAO.findTiposPadre()).thenReturn(List.of());

        frm.inicializar();

        assertNotNull(frm.getListaTipoProducto());
        assertEquals(1, frm.getListaTipoProducto().size());
        assertNotNull(frm.getRoot());
    }


    @Test
    void testCargarArbol() {
        TipoProducto padre = new TipoProducto();
        padre.setId(1L);
        padre.setNombre("Padre");

        when(tipoProductoDAO.findTiposPadre())
                .thenReturn(List.of(padre));

        when(tipoProductoDAO.findHijosByPadre(1L))
                .thenReturn(List.of());

        frm.cargarArbol();

        assertNotNull(frm.getRoot());
        assertEquals(1, frm.getRoot().getChildren().size());
    }

    @Test
    void testOnNodeSelect() {
        TipoProducto t = new TipoProducto();
        t.setId(3L);

        TreeNode node = new DefaultTreeNode(t);

        NodeSelectEvent evt = mock(NodeSelectEvent.class);
        when(evt.getTreeNode()).thenReturn(node);

        doNothing().when(tipoProductoCaracteristicaFrm).setIdCaracteristica(anyLong());
        doNothing().when(tipoProductoCaracteristicaFrm).setIdTipoProducto(any());
        doNothing().when(tipoProductoCaracteristicaFrm).inicializar();

        frm.onNodeSelect(evt);

        assertEquals(t, frm.getRegistro());
        assertEquals(ESTADO_CRUD.MODIFICAR, frm.getEstado());
    }

    @Test
    void testBtnGuardarHandler() {
        frm.registro = new TipoProducto();
        frm.registro.setId(4L);

        when(tipoProductoDAO.findRange(0, Integer.MAX_VALUE))
                .thenReturn(List.of());

        frm.btnGuardarHandler(mock(ActionEvent.class));

        assertNotNull(frm.getRoot());
    }

    @Test
    void testBtnEliminarHandlerSinRegistro() {

        frm.registro = null;

        frm.btnEliminarHandler(mock(ActionEvent.class));

        verify(facesContext).addMessage(any(), any());
    }

    @Test
    void testBtnEliminarHandlerExitoso() {

        TipoProducto t = new TipoProducto();
        t.setId(10L);
        frm.registro = t;

        when(deleteManager.contarProductosDeTipo(10L)).thenReturn(0);
        when(deleteManager.contarCaracteristicasDeTipo(10L)).thenReturn(0);

        when(tipoProductoDAO.findRange(0, Integer.MAX_VALUE))
                .thenReturn(List.of());

        doNothing().when(deleteManager).eliminarTipoProductoEnCascada(10L);

        frm.btnEliminarHandler(mock(ActionEvent.class));

        verify(deleteManager).eliminarTipoProductoEnCascada(10L);
        assertNull(frm.getRegistro());
    }

    @Test
    void testTiposPadreDisponibles() {
        TipoProducto a = new TipoProducto();
        a.setId(1L);

        TipoProducto b = new TipoProducto();
        b.setId(2L);

        frm.setListaTipoProducto(List.of(a, b));

        frm.registro = a;

        List<TipoProducto> disponibles = frm.getTiposPadreDisponibles();

        assertEquals(1, disponibles.size());
        assertEquals(2L, disponibles.get(0).getId());
    }

    @Test
    void testNombreBean() {
        assertEquals("page.tipoProducto", frm.getNombreBean());

        frm.setNombreBean("nuevoNombre");

        assertEquals("nuevoNombre", frm.getNombreBean());
    }

    @Test
    void testGetTipoProductoCaracteristicaFrm() {

        TipoProducto t = new TipoProducto();
        t.setId(33L);

        frm.registro = t;

        doNothing().when(tipoProductoCaracteristicaFrm).setIdCaracteristica(33L);
        doNothing().when(tipoProductoCaracteristicaFrm).setIdTipoProducto(t);

        assertNotNull(frm.getTipoProductoCaracteristicaFrm());
    }
}
