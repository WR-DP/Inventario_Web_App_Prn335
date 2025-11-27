package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.CaracteristicaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.DeleteManager;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoUnidadMedidaDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Caracteristica;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoUnidadMedida;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaracteristicaFrmTest {

    @Mock
    FacesContext facesContext;

    @Mock
    CaracteristicaDAO caracteristicaDAO;

    @Mock
    TipoUnidadMedidaDAO tipoUnidadMedidaDAO;

    @Mock
    DeleteManager deleteManager;

    @InjectMocks
    CaracteristicaFrm frm;

    @BeforeEach
    void setUp() {
        frm.inicializarRegistros();
    }

    private LazyDataModel<Caracteristica> lazyConDatos(List<Caracteristica> datos) {
        LazyDataModel<Caracteristica> lazy = new LazyDataModel<>() {
            @Override
            public int count(Map<String, FilterMeta> map) {
                return datos.size();
            }

            @Override
            public List<Caracteristica> load(int first, int pageSize,
                                             Map<String, SortMeta> sort,
                                             Map<String, FilterMeta> filter) {
                return datos;
            }
        };

        lazy.setWrappedData(datos);
        return lazy;
    }


    @Test
    void testInicializarListasOK() {
        TipoUnidadMedida t = new TipoUnidadMedida();
        t.setId(1);

        when(tipoUnidadMedidaDAO.findRange(0, Integer.MAX_VALUE))
                .thenReturn(List.of(t));

        frm.inicializarListas();

        assertNotNull(frm.getTipoUnidadMedidasList());
        assertEquals(1, frm.getTipoUnidadMedidasList().size());
        assertEquals(t, frm.getTipoUnidadMedidasList().get(0));
    }

    @Test
    void testInicializarListasError() {
        when(tipoUnidadMedidaDAO.findRange(0, Integer.MAX_VALUE))
                .thenThrow(new RuntimeException("ERR"));

        frm.inicializarListas();

        assertNotNull(frm.getTipoUnidadMedidasList());
        assertTrue(frm.getTipoUnidadMedidasList().isEmpty());
    }

    @Test
    void testGetFacesContext() {
        assertSame(facesContext, frm.getFacesContext());
    }

    @Test
    void testGetDao() {
        assertSame(caracteristicaDAO, frm.getDao());
    }

    @Test
    void testGetIdAsTextOK() {
        Caracteristica c = new Caracteristica();
        c.setId(10);

        assertEquals("10", frm.getIdAsText(c));
    }

    @Test
    void testGetIdAsTextNullId() {
        Caracteristica c = new Caracteristica();
        c.setId(null);

        assertNull(frm.getIdAsText(c));
    }

    @Test
    void testGetIdAsTextNullObject() {
        assertNull(frm.getIdAsText(null));
    }

    @Test
    void testGetIdByTextFound() {
        Caracteristica c = new Caracteristica();
        c.setId(1);

        LazyDataModel<Caracteristica> lazy = lazyConDatos(List.of(c));
        frm.setModelo(lazy);

        Caracteristica result = frm.getIdByText("1");

        assertNotNull(result);
        assertSame(c, result);
    }

    @Test
    void testGetIdByTextIdNull() {
        Caracteristica result = frm.getIdByText(null);
        assertNull(result);
    }

    @Test
    void testGetIdByTextInvalidNumber() {
        Caracteristica c = new Caracteristica();
        c.setId(1);

        LazyDataModel<Caracteristica> lazy = lazyConDatos(List.of(c));
        frm.setModelo(lazy);

        Caracteristica result = frm.getIdByText("xyz");
        assertNull(result);  // al fallar el parseo, debe regresar null
    }

    @Test
    void testNuevoRegistro() {
        Caracteristica c = frm.nuevoRegistro();

        assertNotNull(c);
        assertEquals("", c.getNombre());
        assertEquals("", c.getDescripcion());
        assertTrue(c.getActivo());
    }

    @Test
    void testGetDataAccess() {
        assertSame(caracteristicaDAO, frm.getDataAccess());
    }

    @Test
    void testBuscarRegistroPorId() {
        Caracteristica c = new Caracteristica();
        c.setId(1);

        LazyDataModel<Caracteristica> lazy = lazyConDatos(List.of(c));
        frm.setModelo(lazy);

        Caracteristica result = frm.buscarRegistroPorId(1);

        assertNotNull(result);
        assertSame(c, result);
    }

    @Test
    void testBuscarRegistroPorIdIdNoInteger() {
        Caracteristica c = new Caracteristica();
        c.setId(1);

        LazyDataModel<Caracteristica> lazy = lazyConDatos(List.of(c));
        frm.setModelo(lazy);

        Caracteristica result = frm.buscarRegistroPorId("no-int");

        assertNull(result);
    }

    @Test
    void testBuscarRegistroPorIdModeloNull() {
        frm.setModelo(null);

        Caracteristica result = frm.buscarRegistroPorId(1);
        assertNull(result);
    }

    @Test
    void testGetNombreBeanDefault() {
        assertEquals("page.caracteristica", frm.getNombreBean());
    }

    @Test
    void testSetNombreBean() {
        frm.setNombreBean("otro.bean");
        assertEquals("otro.bean", frm.getNombreBean());
    }

    @Test
    void testGetModelo() {
        LazyDataModel<Caracteristica> lazy = lazyConDatos(Collections.emptyList());
        frm.setModelo(lazy);

        assertSame(lazy, frm.getModelo());
    }

    @Test
    void testGetIdTipoUnidadMedidaSeleccionado() {
        Caracteristica c = new Caracteristica();
        TipoUnidadMedida t = new TipoUnidadMedida();
        t.setId(5);
        c.setIdTipoUnidadMedida(t);

        frm.setRegistro(c);

        assertEquals(5, frm.getIdTipoUnidadMedidaSeleccionado());
    }

    @Test
    void testGetIdTipoUnidadMedidaSeleccionadoRegistroNull() {
        frm.setRegistro(null);
        assertNull(frm.getIdTipoUnidadMedidaSeleccionado());
    }

    @Test
    void testSetIdTipoUnidadMedidaSeleccionado() {
        TipoUnidadMedida t1 = new TipoUnidadMedida();
        t1.setId(1);
        TipoUnidadMedida t2 = new TipoUnidadMedida();
        t2.setId(2);

        frm.inicializarListas();
        frm.tipoUnidadMedidasList = List.of(t1, t2);

        Caracteristica c = new Caracteristica();
        frm.setRegistro(c);

        frm.setIdTipoUnidadMedidaSeleccionado(2);

        assertNotNull(frm.getRegistro().getIdTipoUnidadMedida());
        assertEquals(2, frm.getRegistro().getIdTipoUnidadMedida().getId());
    }

    @Test
    void testSetIdTipoUnidadMedidaSeleccionadoListaVacia() {
        frm.tipoUnidadMedidasList = Collections.emptyList();
        Caracteristica c = new Caracteristica();
        frm.setRegistro(c);

        frm.setIdTipoUnidadMedidaSeleccionado(1);

        assertNull(frm.getRegistro().getIdTipoUnidadMedida());
    }

    @Test
    void testBtnEliminarHandlerRegistroNull() {
        frm.setRegistro(null);

        frm.btnEliminarHandler((ActionEvent) null);

        verify(deleteManager, never()).contarTiposConCaracteristica(anyInt());
        verify(deleteManager, never()).eliminarCaracteristicaEnCascada(anyInt());
        verify(caracteristicaDAO, never()).delete(any());

        verify(facesContext, atLeastOnce())
                .addMessage(eq(null), any(FacesMessage.class));
    }

    @Test
    void testBtnEliminarHandlerConDependenciasYExito() {
        Caracteristica c = new Caracteristica();
        c.setId(10);
        frm.setRegistro(c);

        when(deleteManager.contarTiposConCaracteristica(10)).thenReturn(2);
        when(deleteManager.contarProductosConCaracteristica(10)).thenReturn(3);

        // eliminación en cascada OK
        doNothing().when(deleteManager).eliminarCaracteristicaEnCascada(10);
        doNothing().when(caracteristicaDAO).delete(c);

        frm.btnEliminarHandler((ActionEvent) null);

        verify(deleteManager).contarTiposConCaracteristica(10);
        verify(deleteManager).contarProductosConCaracteristica(10);
        verify(deleteManager).eliminarCaracteristicaEnCascada(10);
        verify(caracteristicaDAO).delete(c);

        verify(facesContext, atLeastOnce())
                .addMessage(eq(null), any(FacesMessage.class));

        assertNull(frm.getRegistro());
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    @Test
    void testBtnEliminarHandlerException() {
        Caracteristica c = new Caracteristica();
        c.setId(20);
        frm.setRegistro(c);

        when(deleteManager.contarTiposConCaracteristica(20)).thenReturn(0);
        when(deleteManager.contarProductosConCaracteristica(20)).thenReturn(0);

        doThrow(new RuntimeException("boom"))
                .when(deleteManager).eliminarCaracteristicaEnCascada(20);

        frm.btnEliminarHandler((ActionEvent) null);

        verify(deleteManager).eliminarCaracteristicaEnCascada(20);

        verify(caracteristicaDAO, never()).delete(any());

        verify(facesContext, atLeastOnce())
                .addMessage(eq(null), any(FacesMessage.class));
    }
}
