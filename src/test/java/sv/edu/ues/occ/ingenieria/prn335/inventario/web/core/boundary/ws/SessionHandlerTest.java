package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.ws;

import jakarta.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SessionHandlerTest {

    private SessionHandler handler;
    private Session sessionMock1;
    private Session sessionMock2;

    @BeforeEach
    void setUp() {
        handler = new SessionHandler();
        sessionMock1 = mock(Session.class);
        sessionMock2 = mock(Session.class);
    }

    @Test
    void testAddSession() {

        handler.addSession(sessionMock1);

        Set<Session> sesiones = handler.getSessions();

        assertEquals(1, sesiones.size());
        assertTrue(sesiones.contains(sessionMock1));
    }

    @Test
    void testRemoveSession() {

        handler.addSession(sessionMock1);
        handler.addSession(sessionMock2);

        handler.removeSession(sessionMock1);

        Set<Session> sesiones = handler.getSessions();

        assertEquals(1, sesiones.size());
        assertFalse(sesiones.contains(sessionMock1));
        assertTrue(sesiones.contains(sessionMock2));
    }

    @Test
    void testGetSessions_returnsActualSet() {

        handler.addSession(sessionMock1);

        Set<Session> sesiones = handler.getSessions();

        assertNotNull(sesiones);
        assertEquals(1, sesiones.size());
        assertTrue(sesiones.contains(sessionMock1));
    }

    @Test
    void testAddDuplicateSession() {

        handler.addSession(sessionMock1);
        handler.addSession(sessionMock1); // duplicado

        Set<Session> sesiones = handler.getSessions();

        // El HashSet garantiza que solo haya 1
        assertEquals(1, sesiones.size());
    }
}
