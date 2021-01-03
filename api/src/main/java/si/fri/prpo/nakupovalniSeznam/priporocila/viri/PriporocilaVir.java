package si.fri.prpo.nakupovalniSeznam.priporocila.viri;

import com.arjuna.ats.internal.arjuna.objectstore.TwoPhaseVolatileStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.*;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import kong.unirest.HttpRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.eclipse.jetty.client.HttpClient;
import si.fri.prpo.nakupovalniSeznam.priporocila.dtos.Artikel;
import javax.annotation.PostConstruct;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Path("priporocila")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PriporocilaVir extends Application {

    private Logger log = Logger.getLogger(PriporocilaVir.class.getName());

    private Map<String, Integer> priporocila;

//    @Inject
//    private SpellCheckApiOdjemalec spellCheckApiOdjemalec;

    @PostConstruct
    private void init(){
        log.info("Inicializacija zrna "+ PriporocilaVir.class.getSimpleName());
        priporocila = new HashMap<>();
        priporocila.put(("Suknja"), 4);
    }

    @PreDestroy
    private void destroy(){
        log.info("Deinicializacija zrna "+ PriporocilaVir.class.getSimpleName());
    }

    @Operation(description = "Vrne vsa priporočila najbolj iskanih artiklov", summary = "Seznam priporočil",
            tags = "priporočila",
            responses = {@ApiResponse(responseCode = "200",
                    description = "Seznam priporočil in pripadajoče število iskanj",
                    content = @Content(
                            schema = @Schema(implementation = Artikel.class))
            )})
    @GET
    public Response pridobiPriporocila() {
        return Response.ok(
                priporocila
                        .entrySet()
                        .stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)
                        ))
                .build();
    }

    @Operation(description = "Doda artikel med priporočila", summary = "Doda artikel med priporočila",
            tags = "priporočila",
            responses = {@ApiResponse(responseCode = "201",
                    description = "Artikel dodan med priporočila",
                    content = @Content(
                            schema = @Schema(implementation = Artikel.class))
            )})
    @POST
    public boolean dodajVnos(Artikel artikel){


        //vaje 8
//        if(!spellCheckApiOdjemalec.jeUstreznoCrkovan(naziv)){
//            //return Response.status(Response.Status.BAD_REQUEST).build();
//            return false;
//       }
        String naziv = artikel.getNaziv();
        HttpResponse<String> response = null;
        try{
            response = Unirest.get("https://montanaflynn-spellcheck.p.rapidapi.com/check/?text=" + naziv)
                    .header("x-rapidapi-key", "c1a417f21cmshf584f138be3c98bp1eb29fjsn2995f2f6e3ac")
                    .header("x-rapidapi-host", "montanaflynn-spellcheck.p.rapidapi.com")
                    .asString();
        }catch (WebApplicationException | ProcessingException e){
            log.severe(e.getMessage());
            throw new InternalServerErrorException(e);

        }

        if((response.original).equals(response.suggestion)){
            return true;
        }
        else {
            log.info("Napaka pri crkovanju besede: " + response.original + " a ste morda mislili:" + response.suggestion);
            return false;
        }

        /////
         ////vaje 7
        if(!priporocila.containsKey(naziv)){
            priporocila.put(naziv,1);
        } else {
            priporocila.put(naziv, priporocila.get(naziv) + 1);
        }

        log.info("Priporocila posodobljena. Nova vrednost " + priporocila.get(naziv));
        return true;
        //return Response.ok.build();

    }

    @Operation(description = "Odstrani eno sikanje artikla iz priporočil.", summary = "Odstrani eno sikanje artikla iz priporočil",
            tags = "priporočila",
            responses = {@ApiResponse(responseCode = "201",
                    description = "Artikel odstranjen iz priporočil",
                    content = @Content(
                            schema = @Schema(implementation = Artikel.class))
            )})
    @DELETE
    public Response odstraniVnos(Artikel artikel){
        String naziv = artikel.getNaziv();
        if(!priporocila.containsKey(naziv)){
            log.info("Artikel ni v priporocilnem sistemu.");
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            Integer novaVrednost = priporocila.get(naziv) - 1;
            if(novaVrednost == 0){
                priporocila.remove(naziv);
            } else {
                priporocila.put(naziv, novaVrednost);
            }
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

}
