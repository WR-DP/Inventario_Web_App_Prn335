package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.ClienteDAO;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDAOInterface;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.control.InventarioDefaultDataAccess;
import sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.entity.Cliente;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class ClienteFrm extends DefaultFrm<Cliente> implements Serializable {

    @Inject
    FacesContext facesContext;

    @Inject
    ClienteDAO clienteDAO;

    private List<Cliente> clientes;

    public ClienteFrm() {}

    @Override
    protected FacesContext getFacesContext() {
        return facesContext;
    }

    @Override
    protected InventarioDAOInterface<Cliente, Object> getDao() {
        return clienteDAO;
    }

    @Override
    protected String getIdAsText(Cliente r) {
        if (r != null && r.getId() != null) {
            return r.getId().toString();
        }
        return null;
    }

    @Override
    protected Cliente getIdByText(String id) {
        if (id != null) {
            try {
                UUID buscado = UUID.fromString(id);
                return this.modelo.getWrappedData().stream()
                        .filter(r -> r.getId().equals(buscado))
                        .findFirst().orElse(null);
            } catch (IllegalArgumentException e) {
                Logger.getLogger(ClienteFrm.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    @PostConstruct
    @Override
    public void inicializar() {
        super.inicializar();
        clientes = clienteDAO.findRange(0, Integer.MAX_VALUE);
    }

    @Override
    protected Cliente nuevoRegistro() {
        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNombre("");
        cliente.setDui("");
        cliente.setNit("");
        cliente.setActivo(true);
        return cliente;
    }

    @Override
    public InventarioDefaultDataAccess getDataAccess() {
        return clienteDAO;
    }

    @Override
    protected Cliente buscarRegistroPorId(Object id) {
        if (id instanceof UUID buscado && this.modelo != null) {
            return this.modelo.getWrappedData().stream()
                    .filter(r -> r.getId().equals(buscado))
                    .findFirst().orElse(null);
        }
        return null;
    }

    private String nombreBean = "page.cliente";

    public String getNombreBean() {
        return nombreBean;
    }

    public void setNombreBean(String nombreBean) {
        this.nombreBean = nombreBean;
    }

    public List<Cliente> getListaClientes() {
        return clientes;
    }

    public void setListaClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
