package org.spongepowered.ore.client.http;

import com.google.gson.Gson;
import org.spongepowered.ore.client.OreClient;

import java.io.InputStreamReader;

public class ApiCall extends OreConnection {

    private final Gson gson = new Gson();

    public ApiCall(OreClient client, String route, String queryString, Object... params) {
        super(client.getRouteUrl(route, queryString, params));
    }

    public ApiCall(OreClient client, String route, Object... params) {
        this(client, route, "", params);
    }

    public <T> T read(Class<T> modelClass) {
        if (this.in == null)
            throw new RuntimeException("nothing to read");
        return this.gson.fromJson(new InputStreamReader(this.in), modelClass);
    }

}
