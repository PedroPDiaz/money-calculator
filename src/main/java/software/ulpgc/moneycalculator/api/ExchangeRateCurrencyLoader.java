package software.ulpgc.moneycalculator.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.ulpgc.moneycalculator.Currency;
import software.ulpgc.moneycalculator.CurrencyLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExchangeRateCurrencyLoader implements CurrencyLoader {
    @Override
    public List<Currency> load() {
        try {
            return toList(loadJson());
        }catch (IOException e){
            return Collections.emptyList();
        }
    }

    private List<Currency> toList(String json) {
        List<Currency> listCurrencies = new ArrayList<>();
        Map<String, JsonElement> symbols = new Gson().fromJson(json, JsonObject.class)
                .get("symbols")
                .getAsJsonObject()
                .asMap();
        for (String symbol : symbols.keySet()) {
            listCurrencies.add(new Currency(symbol, symbols.get(symbol).getAsString()));
        }
        return null;
    }

    private String loadJson() throws MalformedURLException {
        URL url = new URL("http://v6.exchangerate-api.com/v6/" + ExchangeRateAPI.key + "/latest/EUR");
        try (InputStream is = url.openStream()) {
            return new String(is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
