package sv.edu.ues.occ.ingenieria.prn335.inventario.web.core.boundary.jsf;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

@Named("menuFrm")
@SessionScoped
public class MenuFrm implements Serializable {

    @Inject
    FacesContext facesContext;

    private DefaultMenuModel model;

    @PostConstruct
    public void init() {

        //voy a agregar los de mas items del menu, hacer parte de los especificos
        model = new DefaultMenuModel();
        DefaultSubMenu tipos = DefaultSubMenu.builder().label("Tipos").expanded(true).build();
        DefaultMenuItem itemAlmacen = DefaultMenuItem.builder()
                .value("Tipo Almacén")
                .icon("pi pi-building")
                .ajax(true)
                .command("#{menuFrm.navegar('TipoAlmacen.jsf')}")
                .build();
        DefaultMenuItem itemProducto = DefaultMenuItem.builder()
                .value("Tipo Producto")
                .icon("pi pi-th-large")
                .ajax(true)
                .command("#{menuFrm.navegar('TipoProducto.jsf')}")
                .build();
        DefaultMenuItem itemTUnidadMedida = DefaultMenuItem.builder()
                .value("Tipo de Unidad de Medida")
                .icon("pi pi-calculator")
                .ajax(true)
                .command("#{menuFrm.navegar('TipoUnidadMedida.jsf')}")
                .build();

        DefaultSubMenu especificos = DefaultSubMenu.builder().label("Especificos").expanded(true).build();
        DefaultMenuItem itemEAlmacen = DefaultMenuItem.builder()
                .value("Almacen")
                .icon("pi pi-warehouse")
                .ajax(true)
                .command("#{menuFrm.navegar('Almacen.jsf')}")
                .build();
        DefaultMenuItem itemEProducto = DefaultMenuItem.builder()
                .value("Producto")
                .icon("pi pi-box")
                .ajax(true)
                .command("#{menuFrm.navegar('Producto.jsf')}")
                .build();
        DefaultMenuItem itemECliente = DefaultMenuItem.builder()
                .value("Cliente")
                .icon("pi pi-user")
                .ajax(true)
                .command("#{menuFrm.navegar('Cliente.jsf')}")
                .build();
        DefaultMenuItem itemEProveedor = DefaultMenuItem.builder()
                .value("Proveedor")
                .icon("pi pi-briefcase")
                .ajax(true)
                .command("#{menuFrm.navegar('Proveedor.jsf')}")
                .build();
        DefaultMenuItem itemECaracteristica = DefaultMenuItem.builder()
                .value("Caracterticas")
                .icon("pi pi-tags")
                .ajax(true)
                .command("#{menuFrm.navegar('Caracteristica.jsf')}")
                .build();
        DefaultMenuItem itemECompras = DefaultMenuItem.builder()
                .value("Compras")
                .icon("pi pi-shopping-cart")
                .ajax(true)
                .command("#{menuFrm.navegar('Compras.jsf')}")
                .build();
        DefaultMenuItem itemEVentas = DefaultMenuItem.builder()
                .value("Ventas")
                .icon("pi pi-dollar")
                .ajax(true)
                .command("#{menuFrm.navegar('Ventas.jsf')}")
                .build();


            ResourceBundle bundle;
            try {
                bundle = ResourceBundle.getBundle("Traducciones", locale);
            } catch (Exception e) {
                bundle = null;
            }

        //Tipos
        tipos.getElements().add(itemAlmacen);
        tipos.getElements().add(itemProducto);
        tipos.getElements().add(itemTUnidadMedida);

        //Especificos
        especificos.getElements().add(itemEAlmacen);
        especificos.getElements().add(itemEProducto);
        especificos.getElements().add(itemECliente);
        especificos.getElements().add(itemEProveedor);
        especificos.getElements().add(itemECaracteristica);
        especificos.getElements().add(itemECompras);
        especificos.getElements().add(itemEVentas);

        model.getElements().add(tipos);
        model.getElements().add(especificos);

            DefaultSubMenu especificos = DefaultSubMenu.builder().label(labelEspecificos).expanded(true).build();
            especificos.getElements().add(crearItem(bundle != null ? bundle.getString("menu.almacen") : "Almacén", "pi pi-warehouse", "Almacen.jsf"));
            especificos.getElements().add(crearItem(bundle != null ? bundle.getString("menu.producto") : "Producto", "pi pi-box", "Producto.jsf"));
            especificos.getElements().add(crearItem(bundle != null ? bundle.getString("menu.cliente") : "Cliente", "pi pi-user", "Cliente.jsf"));
            especificos.getElements().add(crearItem(bundle != null ? bundle.getString("menu.proveedor") : "Proveedor", "pi pi-briefcase", "Proveedor.jsf"));
            especificos.getElements().add(crearItem(bundle != null ? bundle.getString("menu.caracteristicas") : "Características", "pi pi-tags", "Caracteristica.jsf"));
            especificos.getElements().add(crearItem(bundle != null ? bundle.getString("menu.compras") : "Compras", "pi pi-shopping-cart", "Compras.jsf"));
            especificos.getElements().add(crearItem(bundle != null ? bundle.getString("menu.ventas") : "Ventas", "pi pi-dollar", "Ventas.jsf"));

            model.getElements().add(tipos);
            model.getElements().add(especificos);

        } catch (Exception e) {
            e.printStackTrace();
            model = new DefaultMenuModel();
        }
    }


    private DefaultMenuItem crearItem(String label, String icon, String pagina) {
        return DefaultMenuItem.builder()
                .value(label)
                .icon(icon)
                .ajax(false)
                .command("#{menuFrm.navegar('" + pagina + "')}")
                .build();
    }

    public void navegar(String pagina) throws IOException {
        facesContext.getExternalContext().redirect(pagina);
    }

    public DefaultMenuModel getModel() {
        if (model == null || model.getElements().isEmpty()) {
            construirMenu();
        }
        return model;
    }
}
