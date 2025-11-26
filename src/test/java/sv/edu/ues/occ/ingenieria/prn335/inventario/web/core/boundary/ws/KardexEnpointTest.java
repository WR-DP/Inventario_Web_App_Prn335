package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.ws;

import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

class KardexEnpointTest {

    private KardexEnpoint endpoint;
    private SessionHandler sessionHandler;
    private Session sessionMock;
    private RemoteEndpoint.Basic basicRemote;

    @BeforeEach
    void setUp() throws Exception {

        endpoint = new KardexEnpoint();

        sessionHandler = mock(SessionHandler.class);
        sessionMock = mock(Session.class);
        basicRemote = mock(RemoteEndpoint.Basic.class);

        // Inyectar sessionHandler mediante reflexión
        var field = KardexEnpoint.class.getDeclaredField("sessionHandler");
        field.setAccessible(true);
        field.set(endpoint, sessionHandler);

        when(sessionMock.isOpen()).thenReturn(true);
        when(sessionMock.getBasicRemote()).thenReturn(basicRemote);
    }

    @Test
    void testAbrirConexion() {
        endpoint.abrirConexion(sessionMock);
        verify(sessionHandler).addSession(sessionMock);
    }

    @Test
    void testCerrarConexion() {
        endpoint.cerrarConexion(sessionMock);
        verify(sessionHandler).removeSession(sessionMock);
    }

    @Test
    void testEnviarMensajeBroadcast_sesionAbierta() throws Exception {

        List<Session> sesiones = java.util.List.of(sessionMock);
        when(sessionHandler.getSessions()).thenReturn(Set.of(sessionMock));

        endpoint.enviarMensajeBroadcast("hola");

        verify(sessionMock).isOpen();
        verify(sessionMock).getBasicRemote();
        verify(basicRemote).sendText("hola");
    }

    @Test
    void testEnviarMensajeBroadcast_sesionCerrada() throws Exception {

        when(sessionMock.isOpen()).thenReturn(false);
        when(sessionHandler.getSessions()).thenReturn(Set.of(sessionMock));


        endpoint.enviarMensajeBroadcast("hola");

        verify(sessionMock).isOpen();
        verify(sessionMock, never()).getBasicRemote();
        verify(basicRemote, never()).sendText(anyString());
    }

    @Test
    void testEnviarMensajeBroadcast_excepcion() throws Exception {

        when(sessionHandler.getSessions()).thenReturn(Set.of(sessionMock));
        doThrow(new RuntimeException("ERR"))
                .when(basicRemote).sendText("hola");

        endpoint.enviarMensajeBroadcast("hola");

        verify(sessionMock).isOpen();
        verify(basicRemote).sendText("hola");
        // NO lanza excepción → catch funciona
    }
}
