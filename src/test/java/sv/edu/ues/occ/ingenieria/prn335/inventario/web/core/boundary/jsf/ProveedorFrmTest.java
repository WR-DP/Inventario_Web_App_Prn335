package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ProveedorDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProveedorFrmTest {

    @InjectMocks
    ProveedorFrm frm;

    @Mock
    FacesContext facesContext;

    @Mock
    ProveedorDAO proveedorDAO;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        frm = new ProveedorFrm();
        frm.facesContext = facesContext;
        frm.proveedorDAO = proveedorDAO;

        frm.modelo = new LazyDataModel<Proveedor>() {
            @Override
            public List<Proveedor> load(int first, int pageSize,
                                        Map<String, SortMeta> sortBy,
                                        Map<String, FilterMeta> filterBy) {
                return List.of();
            }

            @Override
            public int count(Map<String, FilterMeta> map) {
                return 0;
            }
        };

    }

    @Test
    void testGetIdAsText() {
        Proveedor p = new Proveedor();
        p.setId(10);
        assertEquals("10", frm.getIdAsText(p));
    }

    @Test
    void testGetIdAsTextNull() {
        assertNull(frm.getIdAsText(null));
    }

    @Test
    void testGetIdByTextFound() {
        Proveedor p = new Proveedor();
        p.setId(5);

        frm.modelo.setWrappedData(List.of(p));

        Proveedor result = frm.getIdByText("5");
        assertEquals(p, result);
    }

    @Test
    void testGetIdByTextNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText("999"));
    }

    @Test
    void testGetIdByTextInvalid() {
        assertNull(frm.getIdByText("NO-NUM"));
    }


    @Test
    void testInicializar() {
        when(proveedorDAO.findRange(0, Integer.MAX_VALUE))
                .thenReturn(List.of(new Proveedor()));

        frm.inicializar();

        assertEquals(1, frm.getListaProveedores().size());
    }

    @Test
    void testNuevoRegistro() {
        Proveedor p = frm.nuevoRegistro();

        assertEquals("", p.getNombre());
        assertEquals("", p.getRazonSocial());
        assertEquals("", p.getNit());
        assertTrue(p.getActivo());
        assertEquals("", p.getObservaciones());
    }

    @Test
    void testBuscarRegistroPorId() {
        Proveedor p = new Proveedor();
        p.setId(20);

        frm.modelo.setWrappedData(List.of(p));

        assertEquals(p, frm.buscarRegistroPorId(20));
    }

    @Test
    void testBuscarRegistroPorIdNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.buscarRegistroPorId(1));
    }

    @Test
    void testBtnGuardarNitInvalido() {
        frm.registro = new Proveedor();
        frm.registro.setNit("123"); // No tiene 14 dígitos

        frm.btnGuardarHandler(mock(ActionEvent.class));

        verify(facesContext).addMessage(any(), any());
        verify(proveedorDAO, never()).create(any());
    }

    @Test
    void testBtnGuardarExitoso() {
        frm.registro = new Proveedor();
        frm.registro.setNit("12345678901234");

        Proveedor copia = frm.registro; // ← guardar referencia antes de limpiar

        frm.btnGuardarHandler(mock(ActionEvent.class));

        verify(proveedorDAO).create(eq(copia));
        verify(facesContext).addMessage(any(), any());
        assertNull(frm.registro);
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }


    @Test
    void testBtnGuardarException() {
        frm.registro = new Proveedor();
        frm.registro.setNit("12345678901234");

        doThrow(new RuntimeException("err")).when(proveedorDAO).create(any());

        frm.btnGuardarHandler(mock(ActionEvent.class));

        verify(facesContext).addMessage(any(), any());
    }

    @Test
    void testBtnGuardarRegistroNulo() {
        frm.registro = null;

        frm.btnGuardarHandler(mock(ActionEvent.class));

        verify(facesContext).addMessage(any(), any());
    }

    @Test
    void testBtnModificarSinSeleccion() {
        frm.registro = null;

        frm.btnModificarHandler(mock(ActionEvent.class));

        verify(facesContext).addMessage(any(), any());
    }

    @Test
    void testBtnModificarNitInvalido() {
        frm.registro = new Proveedor();
        frm.registro.setNit("55");

        frm.btnModificarHandler(mock(ActionEvent.class));

        verify(facesContext).addMessage(any(), any());
        verify(proveedorDAO, never()).update(any());
    }

    @Test
    void testBtnModificarExitoso() {
        frm.registro = new Proveedor();
        frm.registro.setNit("12345678901234");

        Proveedor copia = frm.registro;

        frm.btnModificarHandler(mock(ActionEvent.class));

        verify(proveedorDAO).update(eq(copia));
        verify(facesContext).addMessage(any(), any());
    }


    @Test
    void testBtnModificarException() {
        frm.registro = new Proveedor();
        frm.registro.setNit("12345678901234");

        doThrow(new RuntimeException()).when(proveedorDAO).update(any());

        frm.btnModificarHandler(mock(ActionEvent.class));

        verify(facesContext).addMessage(any(), any());
    }

    @Test
    void testSetGetNombreBean() {
        frm.setNombreBean("otro");
        assertEquals("otro", frm.getNombreBean());
    }

    @Test
    void testSetGetListaProveedores() {
        List<Proveedor> list = List.of(new Proveedor());
        frm.setListaProveedores(list);
        assertEquals(1, frm.getListaProveedores().size());
    }
}
