package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.AlmacenDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.TipoAlmacenDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Almacen;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.TipoAlmacen;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlmacenFrmTest {

    @Mock
    FacesContext facesContext;

    @Mock
    AlmacenDAO almacenDAO;

    @Mock
    TipoAlmacenDAO tipoAlmacenDAO;

    @InjectMocks
    AlmacenFrm frm;

    @Test
    void testInicializarListasOK() {
        TipoAlmacen t = mock(TipoAlmacen.class);
        List<TipoAlmacen> lista = List.of(t);

        when(tipoAlmacenDAO.findRange(0, Integer.MAX_VALUE)).thenReturn(lista);

        frm.inicializarListas();

        assertNotNull(frm.getTipoAlmacenList());
        assertEquals(1, frm.getTipoAlmacenList().size());
        assertSame(t, frm.getTipoAlmacenList().getFirst());
    }

    @Test
    void testInicializarListasError() {
        when(tipoAlmacenDAO.findRange(0, Integer.MAX_VALUE))
                .thenThrow(new RuntimeException("ERR"));

        frm.inicializarListas();

        assertNotNull(frm.getTipoAlmacenList());
        assertTrue(frm.getTipoAlmacenList().isEmpty());
    }

    @Test
    void testGetFacesContext() {
        assertSame(facesContext, frm.getFacesContext());
    }

    @Test
    void testGetDao() {
        assertSame(almacenDAO, frm.getDao());
    }

    @Test
    void testGetDataAccess() {
        InventarioDefaultDataAccess da = frm.getDataAccess();
        assertSame(almacenDAO, da);
    }


    @Test
    void testGetIdAsTextOK() {
        Almacen a = new Almacen();
        a.setId(10);

        assertEquals("10", frm.getIdAsText(a));
    }

    @Test
    void testGetIdAsTextNull() {
        assertNull(frm.getIdAsText(null));

        Almacen a = new Almacen();
        assertNull(frm.getIdAsText(a));
    }

    private LazyDataModel<Almacen> lazyConAlmacenes(List<Almacen> almacenes) {
        LazyDataModel<Almacen> lazy = new LazyDataModel<>() {
            @Override
            public List<Almacen> load(int first, int pageSize,
                                      Map<String, SortMeta> sort,
                                      Map<String, FilterMeta> filter) {
                return Collections.emptyList();
            }

            @Override
            public int count(Map<String, FilterMeta> filter) {
                return 0;
            }
        };
        lazy.setWrappedData(almacenes);
        return lazy;
    }

    @Test
    void testGetIdByTextEncontrado() {
        Almacen a1 = new Almacen();
        a1.setId(1);
        Almacen a2 = new Almacen();
        a2.setId(2);

        LazyDataModel<Almacen> lazy = lazyConAlmacenes(List.of(a1, a2));
        frm.setModelo(lazy);

        Almacen res = frm.getIdByText("2");

        assertNotNull(res);
        assertSame(a2, res);
    }

    @Test
    void testGetIdByTextNoEncontrado() {
        Almacen a1 = new Almacen();
        a1.setId(1);

        LazyDataModel<Almacen> lazy = lazyConAlmacenes(List.of(a1));
        frm.setModelo(lazy);

        assertNull(frm.getIdByText("99"));
        assertNull(frm.getIdByText("abc"));
    }

    @Test
    void testGetIdByTextModeloNull() {
        frm.setModelo(null);
        assertNull(frm.getIdByText("1"));
    }

    @Test
    void testNuevoRegistroConTipoAlmacen() {
        TipoAlmacen t = mock(TipoAlmacen.class);


        frm.tipoAlmacenList = List.of(t);

        Almacen a = frm.nuevoRegistro();

        assertNotNull(a);
        assertTrue(a.getActivo());
        assertSame(t, a.getIdTipoAlmacen());
    }

    @Test
    void testNuevoRegistroSinTipoAlmacen() {
        frm.tipoAlmacenList = List.of();

        Almacen a = frm.nuevoRegistro();

        assertNotNull(a);
        assertTrue(a.getActivo());
        assertNull(a.getIdTipoAlmacen());
    }

    @Test
    void testBuscarRegistroPorIdEncontrado() {
        Almacen a1 = new Almacen();
        a1.setId(1);
        Almacen a2 = new Almacen();
        a2.setId(2);

        LazyDataModel<Almacen> lazy = lazyConAlmacenes(List.of(a1, a2));
        frm.setModelo(lazy);

        Almacen res = frm.buscarRegistroPorId(2);

        assertNotNull(res);
        assertSame(a2, res);
    }

    @Test
    void testBuscarRegistroPorIdNoEncontrado() {
        Almacen a1 = new Almacen();
        a1.setId(1);

        LazyDataModel<Almacen> lazy = lazyConAlmacenes(List.of(a1));
        frm.setModelo(lazy);

        assertNull(frm.buscarRegistroPorId(99));
        assertNull(frm.buscarRegistroPorId(null));
        assertNull(frm.buscarRegistroPorId("cadena"));
    }

    @Test
    void testGetModelo() {
        LazyDataModel<Almacen> lazy = lazyConAlmacenes(List.of());
        frm.setModelo(lazy);

        assertSame(lazy, frm.getModelo());
    }

    @Test
    void testGetTipoAlmacenList() {
        TipoAlmacen t = mock(TipoAlmacen.class);
        frm.tipoAlmacenList = List.of(t);

        assertEquals(1, frm.getTipoAlmacenList().size());
    }


    @Test
    void testGetIdTipoAlmacenSeleccionadoOK() {
        TipoAlmacen t = mock(TipoAlmacen.class);
        when(t.getId()).thenReturn(7);

        Almacen a = new Almacen();
        a.setIdTipoAlmacen(t);
        frm.setRegistro(a);

        assertEquals(7, frm.getIdTipoAlmacenSeleccionado());
    }

    @Test
    void testGetIdTipoAlmacenSeleccionadoNull() {
        frm.setRegistro(null);
        assertNull(frm.getIdTipoAlmacenSeleccionado());

        Almacen a = new Almacen();
        frm.setRegistro(a);
        assertNull(frm.getIdTipoAlmacenSeleccionado());
    }

    @Test
    void testSetIdTipoAlmacenSeleccionadoOK() {
        TipoAlmacen t1 = mock(TipoAlmacen.class);
        when(t1.getId()).thenReturn(1);

        TipoAlmacen t2 = mock(TipoAlmacen.class);
        when(t2.getId()).thenReturn(2);

        frm.tipoAlmacenList = List.of(t1, t2);

        Almacen a = new Almacen();
        frm.setRegistro(a);

        frm.setIdTipoAlmacenSeleccionado(2);

        assertSame(t2, a.getIdTipoAlmacen());
    }

    @Test
    void testSetIdTipoAlmacenSeleccionadoSinLista() {
        frm.tipoAlmacenList = List.of(); // vacía

        Almacen a = new Almacen();
        frm.setRegistro(a);

        frm.setIdTipoAlmacenSeleccionado(1);

        assertNull(a.getIdTipoAlmacen());
    }

    @Test
    void testGetNombreBean() {
        assertEquals("page.almacen", frm.getNombreBean());
    }

    @Test
    void testBtnGuardarHandlerTipoInactivo() {
        TipoAlmacen t = mock(TipoAlmacen.class);
        when(t.getActivo()).thenReturn(false);

        Almacen a = new Almacen();
        a.setIdTipoAlmacen(t);
        frm.setRegistro(a);

        frm.btnGuardarHandler((ActionEvent) null);

        verify(almacenDAO, never()).create(any());
        verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
    }

    @Test
    void testBtnGuardarHandlerTipoActivo() {
        TipoAlmacen t = mock(TipoAlmacen.class);
        when(t.getActivo()).thenReturn(true);

        Almacen a = new Almacen();
        a.setIdTipoAlmacen(t);
        frm.setRegistro(a);

        frm.btnGuardarHandler((ActionEvent) null);

        verify(almacenDAO).create(any(Almacen.class));
        verify(facesContext, atLeastOnce()).addMessage(eq(null), any(FacesMessage.class));
    }


    @Test
    void testBtnModificarHandlerTipoInactivo() {
        TipoAlmacen t = mock(TipoAlmacen.class);
        when(t.getActivo()).thenReturn(false);

        Almacen a = new Almacen();
        a.setIdTipoAlmacen(t);
        frm.setRegistro(a);

        frm.btnModificarHandler((ActionEvent) null);

        verify(almacenDAO, never()).update(any());
        verify(facesContext).addMessage(eq(null), any(FacesMessage.class));
    }

    @Test
    void testBtnModificarHandlerTipoActivo() {
        TipoAlmacen t = mock(TipoAlmacen.class);
        when(t.getActivo()).thenReturn(true);

        Almacen a = new Almacen();
        a.setIdTipoAlmacen(t);
        frm.setRegistro(a);

        frm.btnModificarHandler((ActionEvent) null);

        verify(almacenDAO).update(any(Almacen.class));
        verify(facesContext, atLeastOnce()).addMessage(eq(null), any(FacesMessage.class));
    }

}
