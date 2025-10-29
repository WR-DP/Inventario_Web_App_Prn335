package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf.conversores;


import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ClienteDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;

import java.io.Serializable;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@FacesConverter(value = "clienteConverter", managed = true)

public class ClienteConverter implements Converter<Cliente>, Serializable {

    @Override
    public Cliente getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.isBlank()) {
            try {
                ClienteDAO dao = CDI.current().select(ClienteDAO.class).get();
                return dao.findById(UUID.fromString(value));
            } catch (Exception ex) {
                Logger.getLogger(ClienteConverter.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Cliente cliente) {
        if (cliente != null && cliente.getId() != null) {
            return cliente.getId().toString();
        }
        return "";
    }
}