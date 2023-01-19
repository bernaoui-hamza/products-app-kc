package ma.enset.productsapp.web;

import ma.enset.productsapp.repositories.ProductRepository;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.hateoas.PagedModel;

@Controller
public class ProductController{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
private KeycloakRestTemplate keycloakRestTemplate;
    @GetMapping("/")
    public String index(){

     //   System.out.println("salam");
        return "index";
    }
    @GetMapping("/products")
    public String products(Model model){
        model.addAttribute("products",productRepository.findAll());
        return "products";
    }
    @GetMapping("/suppliers")
    public String suppliers(Model model){
        PagedModel<Supplier> pageSupplier = keycloakRestTemplate.getForObject("http://localhost:8083/suppliers", PagedModel.class);
       model.addAttribute("suppliers",pageSupplier);
        return "suppliers";
    }

    @GetMapping("/jwt")
    @ResponseBody
    public Map<String,String> map (HttpServletRequest request){
        KeycloakAuthenticationToken token= (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal= (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext context=principal.getKeycloakSecurityContext();
        Map<String,String>mp=new HashMap<>();
        mp.put("AccessToken",context.getTokenString());
return mp;
    }
    @ExceptionHandler(Exception.class)
    public String ExcemptionHandler(Exception e,Model model){
        model.addAttribute("errorMessage",e.getMessage());
        return "errors";
    }

}
class Supplier{
    private Long id;
    private String name;
    private String email;
}