package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultFrmTest {

    static class EntidadDummy {
        Integer id;

        EntidadDummy(Integer id) { this.id = id; }

        public Integer getId() { return id; }
    }

    static class DummyFrm extends DefaultFrm<EntidadDummy> {

        FacesContext ctxMock;
        InventarioDAOInterface<EntidadDummy, Object> daoMock;

        @Override
        protected FacesContext getFacesContext() {
            return ctxMock;
        }

        @Override
        protected InventarioDAOInterface<EntidadDummy, Object> getDao() {
            return daoMock;
        }

        @Override
        protected String getIdAsText(EntidadDummy r) {
            return r == null ? null : r.getId().toString();
        }

        @Override
        protected EntidadDummy getIdByText(String id) {
            if (id == null || this.modelo == null || this.modelo.getWrappedData() == null)
                return null;
            return this.modelo.getWrappedData().stream()
                    .filter(r -> r.getId().toString().equals(id))
                    .findFirst().orElse(null);
        }

        @Override
        protected EntidadDummy nuevoRegistro() {
            return new EntidadDummy(999);
        }

        @Override
        public InventarioDefaultDataAccess getDataAccess() {
            return (InventarioDefaultDataAccess) daoMock;
        }


        @Override
        protected EntidadDummy buscarRegistroPorId(Object id) {
            if (id instanceof EntidadDummy d) return d;
            return null;
        }
    }

    DummyFrm frm;
    FacesContext ctx;
    InventarioDAOInterface<EntidadDummy, Object> dao;

    @BeforeEach
    void setup() {
        frm = new DummyFrm();
        ctx = mock(FacesContext.class);
        dao = mock(InventarioDAOInterface.class);
        frm.ctxMock = ctx;
        frm.daoMock = dao;
        frm.inicializar();
    }

    // ========================================================
    // cargarDatos
    // ========================================================
    @Test
    void testCargarDatosOK() {
        when(dao.findRange(0, 5)).thenReturn(List.of(new EntidadDummy(1)));
        List<EntidadDummy> r = frm.cargarDatos(0, 5);
        assertEquals(1, r.size());
    }

    @Test
    void testCargarDatosError() {
        when(dao.findRange(0, 5)).thenThrow(new RuntimeException("ERR"));
        List<EntidadDummy> r = frm.cargarDatos(0, 5);
        assertTrue(r.isEmpty());
    }

    // ========================================================
    // contarDatos
    // ========================================================
    @Test
    void testContarDatosOK() {
        when(dao.count()).thenReturn(5);
        assertEquals(5, frm.contarDatos());
    }

    @Test
    void testContarDatosError() {
        when(dao.count()).thenThrow(new RuntimeException("ERR"));
        assertEquals(0, frm.contarDatos());
    }

    // ========================================================
    // LazyModel: getRowKey y getRowData (SIN primefaces)
    // ========================================================
    @Test
    void testLazyModelRowKey() {
        EntidadDummy d = new EntidadDummy(7);
        String key = frm.getModelo().getRowKey(d);
        assertEquals("7", key);
    }

    LazyDataModel<EntidadDummy> lazy = new LazyDataModel<>() {
        @Override
        public List<EntidadDummy> load(int first, int pageSize,
                                       Map<String, SortMeta> sort,
                                       Map<String, FilterMeta> filter) {
            return List.of(new EntidadDummy(1));
        }

        @Override
        public int count(Map<String, FilterMeta> filter) {
            return 1;
        }
    };


    // ========================================================
    // btnNuevoHandler
    // ========================================================
    @Test
    void testBtnNuevoHandler() {
        frm.btnNuevoHandler(null);
        assertNotNull(frm.getRegistro());
        assertEquals(ESTADO_CRUD.CREAR, frm.getEstado());
    }

    // ========================================================
    // btnCancelarHandler
    // ========================================================
    @Test
    void testBtnCancelarHandler() {
        frm.setRegistro(new EntidadDummy(1));
        frm.btnCancelarHandler(null);
        assertNull(frm.getRegistro());
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    // ========================================================
    // btnGuardarHandler (OK)
    // ========================================================
    @Test
    void testBtnGuardarHandlerOK() {
        EntidadDummy d = new EntidadDummy(5);
        frm.setRegistro(d);

        frm.btnGuardarHandler(null);

        verify(dao).create(d);
        verify(ctx).addMessage(eq(null), any(FacesMessage.class));
        assertNull(frm.getRegistro());
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    // ========================================================
    // btnGuardarHandler (registro null)
    // ========================================================
    @Test
    void testBtnGuardarHandlerNull() {
        frm.setRegistro(null);
        frm.btnGuardarHandler(null);
        verify(ctx).addMessage(eq(null), any(FacesMessage.class));
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    // ========================================================
    // btnGuardarHandler ERROR
    // ========================================================
    @Test
    void testBtnGuardarHandlerError() {
        EntidadDummy d = new EntidadDummy(7);
        frm.setRegistro(d);

        doThrow(new RuntimeException("ERR")).when(dao).create(d);

        frm.btnGuardarHandler(null);

        verify(ctx).addMessage(eq(null), any(FacesMessage.class));
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    // ========================================================
    // btnSeleccionarHandler
    // ========================================================
    @Test
    void testBtnSeleccionarHandlerOK() {
        EntidadDummy d = new EntidadDummy(3);
        frm.btnSeleccionarHandler(d);

        assertEquals(ESTADO_CRUD.MODIFICAR, frm.getEstado());
        assertEquals(3, frm.getRegistro().getId());
    }

    @Test
    void testBtnSeleccionarHandlerNull() {
        frm.btnSeleccionarHandler(null);

        verify(ctx).addMessage(eq(null), any(FacesMessage.class));
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    // ========================================================
    // btnModificarHandler
    // ========================================================
    @Test
    void testBtnModificarHandlerOK() {
        EntidadDummy d = new EntidadDummy(9);
        frm.setRegistro(d);

        frm.btnModificarHandler(null);

        verify(dao).update(d);
        verify(ctx).addMessage(eq(null), any(FacesMessage.class));
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    @Test
    void testBtnModificarHandlerNull() {
        frm.setRegistro(null);

        frm.btnModificarHandler(null);

        verify(ctx).addMessage(eq(null), any(FacesMessage.class));
    }

    @Test
    void testBtnModificarHandlerError() {
        EntidadDummy d = new EntidadDummy(4);
        frm.setRegistro(d);
        when(dao.update(d)).thenThrow(new RuntimeException("ERR"));

        frm.btnModificarHandler(null);

        verify(ctx).addMessage(eq(null), any(FacesMessage.class));
    }

    // ========================================================
    // btnEliminarHandler
    // ========================================================
    @Test
    void testBtnEliminarHandlerOK() {
        EntidadDummy d = new EntidadDummy(8);
        frm.setRegistro(d);

        frm.btnEliminarHandler(null);

        verify(dao).delete(d);
        verify(ctx).addMessage(eq(null), any(FacesMessage.class));
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    @Test
    void testBtnEliminarHandlerNull() {
        frm.setRegistro(null);
        frm.btnEliminarHandler(null);
        verify(ctx).addMessage(eq(null), any(FacesMessage.class));
    }

    @Test
    void testBtnEliminarHandlerError() {
        EntidadDummy d = new EntidadDummy(9);
        frm.setRegistro(d);

        doThrow(new RuntimeException("ERR")).when(dao).delete(d);

        frm.btnEliminarHandler(null);

        verify(ctx).addMessage(eq(null), any(FacesMessage.class));
    }

    // ========================================================
    // seleccionarRegistro
    // ========================================================
    @Test
    void testSeleccionarRegistroEventOK() {
        EntidadDummy d = new EntidadDummy(10);

        SelectEvent<EntidadDummy> evt = new SelectEvent<>(
                mock(jakarta.faces.component.UIComponent.class),
                mock(jakarta.faces.component.behavior.Behavior.class),
                d
        );

        frm.seleccionarRegistro(evt);

        assertEquals(10, frm.getRegistro().getId());
        assertEquals(ESTADO_CRUD.MODIFICAR, frm.getEstado());
    }


    @Test
    void testSeleccionarRegistroEventNull() {
        FacesContextMocker.setCurrentInstance(ctx); // ahora funciona

        frm.seleccionarRegistro(null);

        verify(ctx).addMessage(eq(null), any(FacesMessage.class));

        FacesContextMocker.release();
    }




    // ========================================================
    // enviarMensaje
    // ========================================================
    @Test
    void testEnviarMensaje() {
        frm.enviarMensaje("Hola", FacesMessage.SEVERITY_INFO);
        verify(ctx).addMessage(eq(null), any(FacesMessage.class));
    }
}
