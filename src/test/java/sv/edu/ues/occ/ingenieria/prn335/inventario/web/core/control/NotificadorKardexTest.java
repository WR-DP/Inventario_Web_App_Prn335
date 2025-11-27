package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificadorKardexTest {

    @Mock
    ConnectionFactory connectionFactory;

    @Mock
    Queue queue;

    @Mock
    Connection connection;

    @Mock
    Session session;

    @Mock
    MessageProducer producer;

    @Mock
    TextMessage textMessage;

    @InjectMocks
    NotificadorKardex notificadorKardex;

    @Test
    void testNotificarCambioKardex_exito() throws Exception {
        when(connectionFactory.createConnection()).thenReturn(connection);
        when(connection.createSession(false, Session.AUTO_ACKNOWLEDGE)).thenReturn(session);
        when(session.createProducer(queue)).thenReturn(producer);
        when(session.createTextMessage(anyString())).thenReturn(textMessage);

        notificadorKardex.notificarCambioKardex("mensaje-prueba-");

        verify(connectionFactory).createConnection();
        verify(connection).createSession(false, Session.AUTO_ACKNOWLEDGE);
        verify(session).createProducer(queue);
        verify(session).createTextMessage(anyString());
        verify(producer).send(textMessage);
        verify(session).close();
        verify(connection).close();
    }

    @Test
    void testNotificarCambioKardex_excepcionEsCapturada() throws JMSException {
        when(connectionFactory.createConnection()).thenThrow(new JMSException("Error de JMS"));

        // no debe lanzar excepción hacia afuera
        notificadorKardex.notificarCambioKardex("mensaje-error-");

        verify(connectionFactory).createConnection();
        // al fallar la conexión, nada más se ejecuta
        verifyNoMoreInteractions(connection, session, producer);
    }
}
