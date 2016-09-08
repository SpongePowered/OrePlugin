package org.spongepowered.ore.client.http;

import com.google.gson.Gson;
import org.spongepowered.ore.client.OreClient;
import org.spongepowered.ore.client.exception.OreConnectException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

/**
 * Represents a connection to the Ore server.
 */
public class OreConnection {

    private final OreClient client;
    private final Gson gson = new Gson();
    private final URL routeUrl;
    protected InputStream in;
    protected HttpURLConnection http;

    protected OreConnection(OreClient client, String route, String queryString, Object... params) {
        this.client = client;
        this.routeUrl = client.getRouteUrl(route, queryString, params);
    }

    protected OreConnection(OreClient client, String route, Object... params) {
        this(client, route, "", params);
    }

    /**
     * Returns the full {@link URL} for the connection.
     *
     * @return URL for connection
     */
    public URL getUrl() {
        return this.routeUrl;
    }

    /**
     * Returns the connections {@link InputStream} if available, empty
     * otherwise.
     *
     * @return InputStream if available
     */
    public Optional<InputStream> getInputStream() {
        return Optional.ofNullable(this.in);
    }

    /**
     * Opens a connection to the server.
     *
     * @throws IOException
     */
    public OreConnection open() throws IOException {
        // Establish connection
        try {
            this.http = (HttpURLConnection) this.routeUrl.openConnection();
            this.in = this.http.getInputStream();
            return this;
        } catch (ConnectException e) {
            throw new OreConnectException(this.client.getRootUrl().toString());
        }
    }

    /**
     * Reads the connection as a GSON object for the specified class.
     *
     * @param modelClass GSON model class
     * @param <T> Model type
     * @return Model
     */
    public <T> T read(Class<T> modelClass) {
        if (this.in == null)
            throw new RuntimeException("nothing to read");
        return this.gson.fromJson(new InputStreamReader(this.in), modelClass);
    }

    /**
     * Constructs a new connection for the specified client.
     *
     * @param client Ore client
     * @param route API route
     * @param queryString Query string
     * @param params Route parameters
     */
    public static OreConnection openWithQuery(OreClient client, String route, String queryString, Object... params)
        throws IOException {
        return new OreConnection(client, route, queryString, params).open();
    }

    /**
     * Constructs a new connection for the specified client.
     *
     * @param client Ore client
     * @param route API route
     * @param params Route parameters
     */
    public static OreConnection open(OreClient client, String route, Object... params) throws IOException {
        return new OreConnection(client, route, params).open();
    }

}
