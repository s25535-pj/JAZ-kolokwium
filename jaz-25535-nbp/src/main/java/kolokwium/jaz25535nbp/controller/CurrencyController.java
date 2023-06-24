package kolokwium.jaz25535nbp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kolokwium.jaz25535nbp.model.CurrencyQuery;
import kolokwium.jaz25535nbp.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping()
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(summary = "Zwóć średnią cenę danej waluty od daty do daty", method = "GET",
            description = "Podaj daty i walutę a być może nie zawiodę.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Średnia zwórcona pomyślnie",
                    content = {@Content(schema = @Schema(implementation = CurrencyQuery.class), mediaType = "application/json")}),

            @ApiResponse(responseCode = "400",
                    description = "Błędne dane (prawdopodobnie błędne daty)",
                    content = {@Content()}),

            @ApiResponse(responseCode = "404",
                    description = "Błędne dane, (prawdopodobnie błędna waluta)",
                    content = {@Content()}),

            @ApiResponse(responseCode = "504",
                    description = "Server banku zdech",
                    content = {@Content()})
    })

    @GetMapping("/currency/{code}/{startDate}/{endDate}/")
    public ResponseEntity<CurrencyQuery> getCurrencyQuery(@PathVariable String code, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        return ResponseEntity.ok(currencyService.getCurrencyQuery(code, startDate, endDate));
    }
}
