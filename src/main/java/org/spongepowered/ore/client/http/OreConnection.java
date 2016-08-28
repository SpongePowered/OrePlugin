package org.spongepowered.ore.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

public class OreConnection {

    private final URL routeUrl;
    protected InputStream in;
    protected HttpURLConnection http;

    public OreConnection(URL routeUrl) {
        this.routeUrl = routeUrl;
    }

    public URL getRouteUrl() {
        return this.routeUrl;
    }

    public Optional<InputStream> getInputStream() {
        return Optional.ofNullable(this.in);
    }

    /**
     * Opens a connection to the server.
     *
     * @throws IOException
     */
    public void openConnection() throws IOException {
        // Establish connection
        this.http = (HttpURLConnection) this.routeUrl.openConnection();
        this.in = this.http.getInputStream();
    }

}
