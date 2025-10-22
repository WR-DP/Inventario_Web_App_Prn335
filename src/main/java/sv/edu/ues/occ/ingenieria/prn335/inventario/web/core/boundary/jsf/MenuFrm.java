package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;

import java.io.IOException;
import java.io.Serializable;

@Named
@ViewScoped
public class MenuFrm implements Serializable {
    @Inject
    FacesContext facesContext;
    DefaultMenuModel model;

    @PostConstruct
    public void init() {

        //voy a agregar los de mas items del menu, hacer parte de los especificos
        model = new DefaultMenuModel();
        DefaultSubMenu tipos = DefaultSubMenu.builder().label("Tipos").expanded(true).build();
        DefaultMenuItem itemAlmacen = DefaultMenuItem.builder()
                .value("Tipo Almacén")
                .icon("pi pi-inbox")
                .ajax(true)
                .command("#{menuFrm.navegar('TipoAlmacen.jsf')}")
                .build();
        DefaultMenuItem itemProducto = DefaultMenuItem.builder()
                .value("Tipo Producto")
                .icon("pi pi-box")
                .ajax(true)
                .command("#{menuFrm.navegar('TipoProducto.jsf')}")
                .build();
        DefaultMenuItem itemTUnidadMedida = DefaultMenuItem.builder()
                .value("Tipo de Unidad de Medida")
                .icon("")
                .ajax(true)
                .command("#{menuFrm.navegar('TipoUnidadMedida.jsf')}")
                .build();

        DefaultMenuItem itemTCaracteristica = DefaultMenuItem.builder()
                .value("Tipo de Producto Caracteristica")
                .icon("")
                .ajax(true)
                .command("#{menuFrm.navegar('TipoProductoCaracteristica.jsf')}")
                .build();

        DefaultSubMenu especificos = DefaultSubMenu.builder().label("Especificos").expanded(true).build();
        DefaultMenuItem itemEAlmacen = DefaultMenuItem.builder()
                .value("Almacen")
                .icon("")
                .ajax(true)
                .command("#{menuFrm.navegar('Almacen.jsf')}")
                .build();
        DefaultMenuItem itemEProducto = DefaultMenuItem.builder()
                .value("Producto")
                .icon("")
                .ajax(true)
                .command("#{menuFrm.navegar('Producto.jsf')}")
                .build();
        DefaultMenuItem itemECliente = DefaultMenuItem.builder()
                .value("Cliente")
                .icon("")
                .ajax(true)
                .command("#{menuFrm.navegar('Cliente.jsf')}")
                .build();
        DefaultMenuItem itemEProveedor = DefaultMenuItem.builder()
                .value("Proveedor")
                .icon("")
                .ajax(true)
                .command("#{menuFrm.navegar('Proveedor.jsf')}")
                .build();

        //Tipos
        tipos.getElements().add(itemAlmacen);
        tipos.getElements().add(itemProducto);
        tipos.getElements().add(itemTUnidadMedida);
        tipos.getElements().add(itemTCaracteristica);

        //Especificos
        especificos.getElements().add(itemEAlmacen);
        especificos.getElements().add(itemEProducto);
        especificos.getElements().add(itemECliente);
        especificos.getElements().add(itemEProveedor);

        model.getElements().add(tipos);
        model.getElements().add(especificos);

    }

    public void navegar(String pagina) throws IOException {
        facesContext.getExternalContext().redirect(pagina);
    }

    public DefaultMenuModel getModel() {
        return model;
    }

}
