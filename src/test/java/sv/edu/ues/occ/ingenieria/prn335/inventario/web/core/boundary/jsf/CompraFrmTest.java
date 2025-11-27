package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.*;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Compra;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.CompraDetalle;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Proveedor;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompraFrmTest {

    @InjectMocks
    CompraFrm frm;

    @Mock
    FacesContext facesContext;

    @Mock
    CompraDAO compraDAO;

    @Mock
    ProveedorDAO proveedorDAO;

    @Mock
    CompraDetalleDAO compraDetalleDAO;

    @Mock
    CompraDetalleFrm compraDetalleFrm;

    @Mock
    NotificadorKardex notificadorKardex;

    AutoCloseable mocks;

    @BeforeEach
    void setup() {
        mocks = MockitoAnnotations.openMocks(this);

        // LazyDataModel falso
        frm.modelo = mock(LazyDataModel.class);
        when(frm.modelo.getWrappedData()).thenReturn(new ArrayList<>());
    }


    @Test
    void testInicializarOK() {
        when(compraDAO.findRange(0, Integer.MAX_VALUE)).thenReturn(List.of(new Compra()));
        when(proveedorDAO.findRange(0, Integer.MAX_VALUE)).thenReturn(List.of(new Proveedor()));

        frm.inicializar();

        assertNotNull(frm.getListaCompras());
        assertNotNull(frm.getListaProveedores());
        assertEquals(1, frm.getListaCompras().size());
        assertEquals(1, frm.getListaProveedores().size());
    }


    @Test
    void testNuevoRegistro() {
        Compra c = frm.nuevoRegistro();
        assertNotNull(c.getFecha());
        assertEquals("ACTIVA", c.getEstado());
        assertEquals("", c.getObservaciones());
    }


    @Test
    void testCalcularMontoTotal_OK() {
        Compra compra = new Compra();
        compra.setId(10L);

        CompraDetalle d1 = new CompraDetalle();
        d1.setCantidad(BigDecimal.valueOf(2));
        d1.setPrecio(BigDecimal.valueOf(5));

        when(compraDetalleDAO.findByIdCompra(10L, 0, Integer.MAX_VALUE))
                .thenReturn(List.of(d1));

        when(compraDetalleDAO.calcularMontoTotal(anyList()))
                .thenReturn(BigDecimal.TEN);

        BigDecimal total = frm.calcularMontoTotal(compra);

        assertEquals(BigDecimal.TEN, total);
    }

    @Test
    void testCalcularMontoTotal_Null() {
        assertEquals(BigDecimal.ZERO, frm.calcularMontoTotal(null));
    }


    @Test
    void testValidarCamposSinProveedor() {
        Compra c = new Compra();
        c.setEstado("ACTIVA");
        c.setFecha(new Date());
        frm.registro = c;

        frm.btnGuardarHandler(mock(ActionEvent.class));

        verify(facesContext, atLeastOnce())
                .addMessage(any(), any(FacesMessage.class));

        // No debe llamar a create()
        verify(compraDAO, never()).create(any());
    }


    @Test
    void testBtnGuardarProveedorInactivo() {
        Compra compra = new Compra();
        Proveedor p = new Proveedor();
        p.setActivo(false);
        compra.setIdProveedor(p);
        compra.setEstado("ACTIVA");
        compra.setFecha(new Date());
        frm.registro = compra;

        frm.btnGuardarHandler(mock(ActionEvent.class));

        verify(facesContext, atLeastOnce()).addMessage(any(), any());
        verify(compraDAO, never()).create(any());
    }

    @Test
    void testBtnGuardarOK() {
        Compra compra = new Compra();
        Proveedor p = new Proveedor();
        p.setId(1);
        p.setActivo(true);

        compra.setIdProveedor(p);
        compra.setEstado("ACTIVA");
        compra.setFecha(new Date());
        frm.registro = compra;

        when(proveedorDAO.findById(1)).thenReturn(p);

        frm.btnGuardarHandler(mock(ActionEvent.class));

        verify(compraDAO).create(any());
    }


    @Test
    void testBtnModificarProveedorInactivo() {
        Compra compra = new Compra();
        Proveedor p = new Proveedor();
        p.setActivo(false);

        compra.setIdProveedor(p);
        compra.setEstado("ACTIVA");
        compra.setFecha(new Date());
        frm.registro = compra;

        frm.btnModificarHandler(mock(ActionEvent.class));

        verify(compraDAO, never()).update(any());
        verify(facesContext, atLeastOnce()).addMessage(any(), any());
    }

    @Test
    void testBtnModificarOK() {
        Compra compra = new Compra();
        Proveedor p = new Proveedor();
        p.setId(1);
        p.setActivo(true);

        compra.setIdProveedor(p);
        compra.setFecha(new Date());
        compra.setEstado("ACTIVA");

        frm.registro = compra;

        when(proveedorDAO.findById(1)).thenReturn(p);

        frm.btnModificarHandler(mock(ActionEvent.class));

        verify(compraDAO).update(any());
    }


    @Test
    void testSeleccionarRegistro() {
        Compra c = new Compra();
        c.setId(10L);

        SelectEvent<Compra> evt = mock(SelectEvent.class);
        when(evt.getObject()).thenReturn(c);

        frm.registro = c;

        frm.seleccionarRegistro(evt);

        verify(compraDetalleFrm).setIdCompra(10L);
        verify(compraDetalleFrm).inicializarRegistros();
    }


    @Test
    void testSeleccionarProveedorOK() {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(1);

        SelectEvent<Proveedor> evt = mock(SelectEvent.class);
        when(evt.getObject()).thenReturn(proveedor);

        frm.registro = new Compra();

        when(proveedorDAO.findById(1)).thenReturn(proveedor);

        frm.seleccionarProveedor(evt);

        assertNotNull(frm.registro.getIdProveedor());
        verify(facesContext).addMessage(any(), any());
    }


    @Test
    void testNotificarCambioKardex() {
        Compra compra = new Compra();
        compra.setId(10L);

        frm.registro = compra;

        frm.notificarCambioKardex(mock(ActionEvent.class));

        verify(notificadorKardex).notificarCambioKardex(anyString());
    }
}
