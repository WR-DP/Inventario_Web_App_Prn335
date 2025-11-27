package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control;

import jakarta.jms.TextMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.ws.KardexEnpoint;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceptorKardexTest {

    @Mock
    KardexEnpoint kardexEnpoint;

    @Mock
    TextMessage textMessage;

    @InjectMocks
    ReceptorKardex receptor;

    @Test
    void testOnMessage_enviaMensajeCorrecto() throws Exception {

        when(textMessage.getText()).thenReturn("mensaje123");

        receptor.onMessage(textMessage);

        verify(textMessage, times(2)).getText();
        verify(kardexEnpoint).enviarMensajeBroadcast("mensaje123");
        verifyNoMoreInteractions(kardexEnpoint);
    }


    @Test
    void testOnMessage_errorEnGetText_noLanzaExcepcion() throws Exception {

        when(textMessage.getText()).thenThrow(new RuntimeException("boom"));

        receptor.onMessage(textMessage);

        verify(textMessage).getText();
        verifyNoInteractions(kardexEnpoint);
    }
}
