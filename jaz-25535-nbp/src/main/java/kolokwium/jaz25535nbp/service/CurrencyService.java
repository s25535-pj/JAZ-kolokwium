package kolokwium.jaz25535nbp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kolokwium.jaz25535nbp.exceptions.WrongInputException;
import kolokwium.jaz25535nbp.model.CurrencyQuery;
import kolokwium.jaz25535nbp.storage.CurrencyQueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class CurrencyService {
    private final RestTemplate restTemplate;
    private final CurrencyQueryRepository currencyQueryRepository;

    public CurrencyService(RestTemplate restTemplate, CurrencyQueryRepository currencyQueryRepository) {
        this.restTemplate = restTemplate;
        this.currencyQueryRepository = currencyQueryRepository;
    }


    // Zrobiłem jak umiałem, okrężnie, ale jednak. Przynajmniej wiadomo że nie zgapiałem.
    public CurrencyQuery getCurrencyQuery(String currency, LocalDate startDate, LocalDate endDate) {

        int days = (int) DAYS.between(startDate, endDate);
        if(days <= 0){
            throw new WrongInputException("Daty podane niepoprawnie, liczba dni wychodzi ujemna.");
        }
//                    http://api.nbp.pl/api/exchangerates/rates/A/EUR/2019-01-01/2019-01-10/
        String url = "http://api.nbp.pl/api/exchangerates/rates/A/" + currency + "/" + startDate +  "/" + endDate + "/" + "?format=json";
        String jsonResponse = restTemplate.getForObject(url, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            int i = days;
            double sum = 0;
            double rate;
            while(i > 0) {
                rate = rootNode.get("rates").get(0).get("mid").asDouble();
                sum = sum + rate;
                System.out.println(rate);
                i--;
            }
            double averageRate = sum/days;
            return createCurrencyQuery(currency, days, averageRate, startDate, endDate);

        } catch (Exception e) {
            throw new WrongInputException("Waluta nie taka tutaj");
        }
    }

    private CurrencyQuery createCurrencyQuery(String currency, int days, double rate, LocalDate startDate, LocalDate endDate ) {
        CurrencyQuery currencyQuery = new CurrencyQuery();
        currencyQuery.setCurrency(currency);
        currencyQuery.setDays(days);
        currencyQuery.setRate(rate);
        currencyQuery.setStartDate(startDate);
        currencyQuery.setEndDate(endDate);
        currencyQueryRepository.save(currencyQuery);

        return currencyQuery;
    }
}