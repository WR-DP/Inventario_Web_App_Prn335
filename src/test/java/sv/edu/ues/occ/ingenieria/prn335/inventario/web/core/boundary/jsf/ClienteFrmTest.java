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
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ClienteDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.DeleteManager;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteFrmTest {

    @Mock
    FacesContext facesContext;

    @Mock
    ClienteDAO clienteDAO;

    @Mock
    DeleteManager deleteManager;

    @InjectMocks
    ClienteFrm frm;

    @BeforeEach
    void setUp() {
        frm.inicializarRegistros();
    }

    private LazyDataModel<Cliente> lazy(List<Cliente> datos) {
        LazyDataModel<Cliente> l = new LazyDataModel<>() {
            @Override
            public int count(Map<String, FilterMeta> map) {
                return datos.size();
            }

            @Override
            public List<Cliente> load(int first, int pageSize,
                                      Map<String, SortMeta> sort, Map<String, FilterMeta> filter) {
                return datos;
            }
        };
        l.setWrappedData(datos);
        return l;
    }

    @Test
    void testGetFacesContext() {
        assertSame(facesContext, frm.getFacesContext());
    }

    @Test
    void testGetDao() {
        assertSame(clienteDAO, frm.getDao());
    }

    @Test
    void testGetIdAsTextOK() {
        Cliente c = new Cliente();
        UUID id = UUID.randomUUID();
        c.setId(id);

        assertEquals(id.toString(), frm.getIdAsText(c));
    }

    @Test
    void testGetIdAsTextNull() {
        assertNull(frm.getIdAsText(null));
    }

    @Test
    void testGetIdAsTextIdNull() {
        Cliente c = new Cliente();
        c.setId(null);

        assertNull(frm.getIdAsText(c));
    }

    @Test
    void testGetIdByTextFound() {
        UUID id = UUID.randomUUID();

        Cliente c = new Cliente();
        c.setId(id);

        frm.setModelo(lazy(List.of(c)));

        Cliente r = frm.getIdByText(id.toString());
        assertNotNull(r);
        assertSame(c, r);
    }

    @Test
    void testGetIdByTextInvalidUUID() {
        frm.setModelo(lazy(Collections.emptyList()));
        assertNull(frm.getIdByText("XXX"));
    }

    @Test
    void testGetIdByTextModelNull() {
        frm.setModelo(null);
        assertNull(frm.getIdByText("id"));
    }

    @Test
    void testNuevoRegistro() {
        Cliente c = frm.nuevoRegistro();

        assertNotNull(c);
        assertEquals("", c.getNombre());
        assertEquals("", c.getDui());
        assertEquals("", c.getNit());
        assertTrue(c.getActivo());
    }

    @Test
    void testValidarCamposOK() {
        Cliente c = new Cliente();
        c.setDui("123456789");
        c.setNit("12345678901234");
        frm.setRegistro(c);

        assertTrue(frm.getRegistro().getDui().length() == 9);
        assertTrue(frm.getRegistro().getNit().length() == 14);
    }

    @Test
    void testValidarCamposDuiInvalido() {
        Cliente c = new Cliente();
        c.setDui("123"); // mal
        c.setNit("12345678901234");
        frm.setRegistro(c);

        frm.btnGuardarHandler((ActionEvent) null);

        verify(facesContext, atLeastOnce()).addMessage(eq(null), any());
    }

    @Test
    void testValidarCamposNitInvalido() {
        Cliente c = new Cliente();
        c.setDui("123456789");
        c.setNit("1234"); // mal
        frm.setRegistro(c);

        frm.btnGuardarHandler((ActionEvent) null);

        verify(facesContext, atLeastOnce()).addMessage(eq(null), any());
    }

    @Test
    void testBtnGuardarHandlerOK() {
        Cliente c = new Cliente();
        c.setDui("123456789");
        c.setNit("12345678901234");
        frm.setRegistro(c);

        frm.btnGuardarHandler((ActionEvent) null);

        verify(clienteDAO).create(any());
        verify(facesContext, atLeastOnce()).addMessage(eq(null), any());
    }

    @Test
    void testBtnGuardarHandlerRegistroNull() {
        frm.setRegistro(null);

        frm.btnGuardarHandler((ActionEvent) null);

        verify(facesContext, atLeastOnce()).addMessage(eq(null), any());
        verify(clienteDAO, never()).create(any());
    }

    @Test
    void testBtnModificarHandlerOK() {
        Cliente c = new Cliente();
        c.setDui("123456789");
        c.setNit("12345678901234");
        frm.setRegistro(c);

        frm.btnModificarHandler((ActionEvent) null);

        verify(clienteDAO).update(any());
        verify(facesContext, atLeastOnce()).addMessage(eq(null), any());
    }

    @Test
    void testBtnModificarHandlerRegistroNull() {
        frm.setRegistro(null);

        frm.btnModificarHandler((ActionEvent) null);

        verify(facesContext, atLeastOnce()).addMessage(eq(null), any());
        verify(clienteDAO, never()).update(any());
    }

    @Test
    void testBtnEliminarHandlerRegistroNull() {
        frm.setRegistro(null);

        frm.btnEliminarHandler((ActionEvent) null);

        verify(facesContext, atLeastOnce()).addMessage(eq(null), any());
    }

    @Test
    void testBtnEliminarHandlerOK() {

        UUID id = UUID.randomUUID();
        Cliente c = new Cliente();
        c.setId(id);

        frm.setRegistro(c);

        when(deleteManager.contarVentasDeCliente(id)).thenReturn(2);
        when(deleteManager.contarDetallesDeVentaCliente(id)).thenReturn(5);
        when(deleteManager.contarKardexDeCliente(id)).thenReturn(3);

        doNothing().when(deleteManager).eliminarVentasDeCliente(id);
        doNothing().when(clienteDAO).delete(c);

        frm.btnEliminarHandler((ActionEvent) null);

        verify(deleteManager).contarVentasDeCliente(id);
        verify(deleteManager).eliminarVentasDeCliente(id);
        verify(clienteDAO).delete(c);
        verify(facesContext, atLeastOnce()).addMessage(eq(null), any());
    }

    @Test
    void testBtnEliminarHandlerException() {
        UUID id = UUID.randomUUID();
        Cliente c = new Cliente();
        c.setId(id);

        frm.setRegistro(c);

        doThrow(new RuntimeException("boom"))
                .when(deleteManager).eliminarVentasDeCliente(any());

        frm.btnEliminarHandler((ActionEvent) null);

        verify(facesContext, atLeastOnce()).addMessage(eq(null), any());
        verify(clienteDAO, never()).delete(c);
    }
}
