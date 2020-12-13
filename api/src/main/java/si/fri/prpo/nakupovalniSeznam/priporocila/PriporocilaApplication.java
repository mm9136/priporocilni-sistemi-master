package si.fri.prpo.nakupovalniSeznam.priporocila;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.*;


@OpenAPIDefinition(info = @Info(title = "Priporocila API", version = "v1",
        contact = @Contact(email = "prpo@fri.uni-lj.si"),
        license = @License(), description = "API za storitev Priporocilni sistemi"),
        servers = @Server(url = "http://localhost:8081/v1"))

@ApplicationPath("v1")
public class PriporocilaApplication extends Application {

}