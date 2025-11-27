package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.*;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Venta;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.VentaDetalle;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VentaFrmTest {

    @InjectMocks
    VentaFrm frm;

    @Mock
    FacesContext facesContext;

    @Mock
    VentaDAO ventaDAO;

    @Mock
    ClienteDAO clienteDAO;

    @Mock
    VentaDetalleDAO ventaDetalleDAO;

    @Mock
    VentaDetalleFrm ventaDetalleFrm;

    @Mock
    DeleteManager deleteManager;

    @Mock
    NotificadorKardex notificadorKardex;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        // LazyDataModel necesario para DefaultFrm
        frm.modelo = new LazyDataModel<Venta>() {
            @Override
            public List<Venta> load(int first, int pageSize,
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
    void testGetIdAsText() {
        Venta v = new Venta();
        UUID id = UUID.randomUUID();
        v.setId(id);

        assertEquals(id.toString(), frm.getIdAsText(v));
    }

    @Test
    void testGetIdAsTextNull() {
        assertNull(frm.getIdAsText(null));
    }

    @Test
    void testGetIdByTextFound() {
        UUID id = UUID.randomUUID();
        Venta v = new Venta();
        v.setId(id);

        frm.modelo.setWrappedData(List.of(v));

        Venta result = frm.getIdByText(id.toString());
        assertEquals(v, result);
    }

    @Test
    void testGetIdByTextNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText(UUID.randomUUID().toString()));
    }

    @Test
    void testGetIdByTextInvalidUUID() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.getIdByText("NO-UUID"));
    }

    @Test
    void testInicializar() {
        when(ventaDAO.findRange(0, Integer.MAX_VALUE))
                .thenReturn(List.of(new Venta()));
        when(clienteDAO.findRange(0, Integer.MAX_VALUE))
                .thenReturn(List.of(new Cliente()));

        frm.inicializar();

        assertNotNull(frm.getListaVentas());
        assertEquals(1, frm.getListaVentas().size());

        assertNotNull(frm.getListaClientes());
        assertEquals(1, frm.getListaClientes().size());
    }

    @Test
    void testNuevoRegistro() {
        Venta v = frm.nuevoRegistro();

        assertNotNull(v.getId());
        assertEquals("PENDIENTE", v.getEstado());
        assertEquals("", v.getObservaciones());
        assertNotNull(v.getFecha());
    }

    @Test
    void testBuscarRegistroPorIdFound() {
        UUID id = UUID.randomUUID();
        Venta v = new Venta();
        v.setId(id);

        frm.modelo.setWrappedData(List.of(v));

        Venta result = frm.buscarRegistroPorId(id);
        assertEquals(v, result);
    }

    @Test
    void testBuscarRegistroPorIdNotFound() {
        frm.modelo.setWrappedData(List.of());
        assertNull(frm.buscarRegistroPorId(UUID.randomUUID()));
    }

    @Test
    void testCalcularMontoTotalVentaNull() {
        BigDecimal total = frm.calcularMontoTotal(null);
        assertEquals(BigDecimal.ZERO, total);
        verifyNoInteractions(ventaDetalleDAO);
    }

    @Test
    void testCalcularMontoTotalOk() {
        UUID id = UUID.randomUUID();
        Venta v = new Venta();
        v.setId(id);

        List<VentaDetalle> detalles = List.of(new VentaDetalle());

        when(ventaDetalleDAO.findByIdVenta(id, 0, Integer.MAX_VALUE))
                .thenReturn(detalles);
        when(ventaDetalleDAO.calcularMontoTotal(detalles))
                .thenReturn(new BigDecimal("25.50"));

        BigDecimal total = frm.calcularMontoTotal(v);

        assertEquals(new BigDecimal("25.50"), total);
        verify(ventaDetalleDAO).findByIdVenta(id, 0, Integer.MAX_VALUE);
        verify(ventaDetalleDAO).calcularMontoTotal(detalles);
    }

    @Test
    void testBtnGuardarClienteInactivo() {
        Cliente c = new Cliente();
        c.setActivo(false);

        Venta v = new Venta();
        v.setIdCliente(c);
        v.setEstado("ACTIVA");
        v.setFecha(new Date());

        frm.setRegistro(v);

        frm.btnGuardarHandler(mock(ActionEvent.class));

        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));
        verify(ventaDAO, never()).create(any());
        assertSame(v, frm.getRegistro());
    }

    @Test
    void testBtnGuardarCamposInvalidos() {
        Venta v = new Venta();
        v.setIdCliente(null);        // dispara validarCampos()
        v.setEstado("ACTIVA");
        v.setFecha(new Date());

        frm.setRegistro(v);

        frm.btnGuardarHandler(mock(ActionEvent.class));

        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));
        verify(ventaDAO, never()).create(any());
        assertSame(v, frm.getRegistro());
    }

    @Test
    void testBtnGuardarExitoso() {
        Cliente c = new Cliente();
        c.setActivo(true);

        Venta v = new Venta();
        v.setIdCliente(c);
        v.setEstado("ACTIVA");
        v.setFecha(new Date());

        frm.setRegistro(v);

        ActionEvent evt = mock(ActionEvent.class);

        frm.btnGuardarHandler(evt);

        verify(ventaDAO).create(any(Venta.class));
        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));

        assertNull(frm.getRegistro());
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    @Test
    void testBtnModificarClienteInactivo() {
        Cliente c = new Cliente();
        c.setActivo(false);

        Venta v = new Venta();
        v.setIdCliente(c);
        v.setEstado("ACTIVA");
        v.setFecha(new Date());

        frm.setRegistro(v);

        frm.btnModificarHandler(mock(ActionEvent.class));

        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));
        verify(ventaDAO, never()).update(any());
        assertSame(v, frm.getRegistro());
    }

    @Test
    void testBtnModificarCamposInvalidos() {
        Venta v = new Venta();
        v.setIdCliente(null);    // dispara validarCampos()
        v.setEstado("ACTIVA");
        v.setFecha(new Date());

        frm.setRegistro(v);

        frm.btnModificarHandler(mock(ActionEvent.class));

        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));
        verify(ventaDAO, never()).update(any());
        assertSame(v, frm.getRegistro());
    }

    @Test
    void testBtnModificarExitoso() {
        Cliente c = new Cliente();
        c.setActivo(true);

        Venta v = new Venta();
        v.setIdCliente(c);
        v.setEstado("ACTIVA");
        v.setFecha(new Date());

        frm.setRegistro(v);

        ActionEvent evt = mock(ActionEvent.class);

        frm.btnModificarHandler(evt);

        verify(ventaDAO).update(any(Venta.class));
        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));

        assertNull(frm.getRegistro());
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    @Test
    void testSeleccionarRegistro() throws Exception {
        UUID id = UUID.randomUUID();
        Venta v = new Venta();
        v.setId(id);

        SelectEvent<Venta> evt = mock(SelectEvent.class);
        when(evt.getObject()).thenReturn(v);

        doNothing().when(ventaDetalleFrm).setIdVenta(id);
        doNothing().when(ventaDetalleFrm).inicializarRegistros();

        frm.seleccionarRegistro(evt);

        assertEquals(v, frm.getRegistro());
        assertEquals(ESTADO_CRUD.MODIFICAR, frm.getEstado());

        verify(ventaDetalleFrm).setIdVenta(id);
        verify(ventaDetalleFrm).inicializarRegistros();
    }

    @Test
    void testBtnSeleccionarClienteSinRegistro() {
        frm.setRegistro(null);

        frm.btnSeleccionarClienteHandler(mock(ActionEvent.class));

        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));
    }

    @Test
    void testBtnSeleccionarClienteSinCliente() {
        Venta v = new Venta();
        v.setIdCliente(null);
        frm.setRegistro(v);

        frm.btnSeleccionarClienteHandler(mock(ActionEvent.class));

        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));
        verify(clienteDAO, never()).findById(any());
    }

    @Test
    void testBtnSeleccionarClienteConCargaCompleta() {
        UUID id = UUID.randomUUID();

        Cliente sel = new Cliente();
        sel.setId(id);

        Cliente completo = new Cliente();
        completo.setId(id);

        Venta v = new Venta();
        v.setIdCliente(sel);

        frm.setRegistro(v);

        when(clienteDAO.findById(id)).thenReturn(completo);

        frm.btnSeleccionarClienteHandler(mock(ActionEvent.class));

        assertSame(completo, frm.getRegistro().getIdCliente());
        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));
    }

    @Test
    void testSeleccionarClienteNull() {
        frm.setRegistro(new Venta());

        frm.seleccionarCliente(null);

        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));
        verify(clienteDAO, never()).findById(any());
    }

    @Test
    void testSeleccionarClienteConId() {
        frm.setRegistro(new Venta());

        UUID id = UUID.randomUUID();

        Cliente sel = new Cliente();
        sel.setId(id);

        Cliente completo = new Cliente();
        completo.setId(id);

        SelectEvent<Cliente> evt = mock(SelectEvent.class);
        when(evt.getObject()).thenReturn(sel);
        when(clienteDAO.findById(id)).thenReturn(completo);

        frm.seleccionarCliente(evt);

        assertSame(completo, frm.getRegistro().getIdCliente());
        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));
    }

    @Test
    void testBuscarClientesPorNombreOk() {
        Cliente c = new Cliente();
        when(clienteDAO.buscarClientePorNombre("JUAN", 0, 50))
                .thenReturn(List.of(c));

        List<Cliente> result = frm.buscarClientesPorNombre("JUAN");

        assertEquals(1, result.size());
        verify(clienteDAO).buscarClientePorNombre("JUAN", 0, 50);
    }

    @Test
    void testBuscarClientesPorNombreVacio() {
        List<Cliente> result = frm.buscarClientesPorNombre("   ");

        assertTrue(result.isEmpty());
        verify(clienteDAO, never()).buscarClientePorNombre(anyString(), anyInt(), anyInt());
    }

    @Test
    void testNombreBeanGetSet() {
        assertEquals("page.venta", frm.getNombreBean());

        frm.setNombreBean("otroBean");
        assertEquals("otroBean", frm.getNombreBean());
    }

    @Test
    void testListaVentasGetSet() {
        List<Venta> lista = List.of(new Venta());
        frm.setListaVentas(lista);

        assertEquals(lista, frm.getListaVentas());
    }

    @Test
    void testListaClientesGetSet() {
        List<Cliente> lista = List.of(new Cliente());
        frm.setListaClientes(lista);

        assertEquals(lista, frm.getListaClientes());
    }

    @Test
    void testGetVentaDetalleFrm() {
        UUID id = UUID.randomUUID();
        Venta v = new Venta();
        v.setId(id);

        frm.setRegistro(v);

        doNothing().when(ventaDetalleFrm).setIdVenta(id);

        VentaDetalleFrm result = frm.getVentaDetalleFrm();

        assertNotNull(result);
        verify(ventaDetalleFrm).setIdVenta(id);
    }

    @Test
    void testBtnEliminarSinRegistro() {
        frm.setRegistro(null);

        frm.btnEliminarHandler(mock(ActionEvent.class));

        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));
        verify(deleteManager, never()).eliminarVentaEnCascada(any());
    }

    @Test
    void testBtnEliminarExitosoConDependencias() {
        UUID id = UUID.randomUUID();
        Venta v = new Venta();
        v.setId(id);
        frm.setRegistro(v);
        frm.setEstado(ESTADO_CRUD.MODIFICAR);

        when(deleteManager.contarDetallesDeVenta(id)).thenReturn(3);
        when(deleteManager.contarKardexDeVenta(id)).thenReturn(2);

        doNothing().when(deleteManager).eliminarVentaEnCascada(id);

        frm.btnEliminarHandler(mock(ActionEvent.class));

        verify(deleteManager).eliminarVentaEnCascada(id);
        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));

        assertNull(frm.getRegistro());
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }

    @Test
    void testBtnEliminarConError() {
        UUID id = UUID.randomUUID();
        Venta v = new Venta();
        v.setId(id);
        frm.setRegistro(v);
        frm.setEstado(ESTADO_CRUD.MODIFICAR);

        when(deleteManager.contarDetallesDeVenta(id)).thenReturn(0);
        when(deleteManager.contarKardexDeVenta(id)).thenReturn(0);
        doThrow(new RuntimeException("Fallo"))
                .when(deleteManager).eliminarVentaEnCascada(id);

        frm.btnEliminarHandler(mock(ActionEvent.class));

        verify(facesContext, atLeastOnce()).addMessage(any(), any(FacesMessage.class));

        assertEquals(ESTADO_CRUD.MODIFICAR, frm.getEstado());
        assertSame(v, frm.getRegistro());
    }

    @Test
    void testNotificarCambioKardexSinRegistro() {
        frm.setRegistro(null);

        frm.notificarCambioKardex(mock(ActionEvent.class));

        verify(ventaDAO, never()).update(any());
        verify(notificadorKardex, never()).notificarCambioKardex(anyString());
    }

    @Test
    void testNotificarCambioKardexOk() {
        UUID id = UUID.randomUUID();
        Venta v = new Venta();
        v.setId(id);
        v.setEstado("PENDIENTE");

        frm.setRegistro(v);

        ActionEvent evt = mock(ActionEvent.class);

        frm.notificarCambioKardex(evt);

        verify(ventaDAO).update(any(Venta.class));
        verify(notificadorKardex).notificarCambioKardex("Cambio en venta ID: ");

        assertNull(frm.getRegistro());
        assertEquals(ESTADO_CRUD.NADA, frm.getEstado());
    }
}
