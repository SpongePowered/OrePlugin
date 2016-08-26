package org.spongepowered.ore;

/**
 * Collection of routes used by Ore.
 */
public final class Routes {

    public static final String PROJECT_LIST = "/api/projects";
    public static final String PROJECT = "/api/projects/%s";
    public static final String VERSION_LIST = "/api/projects/%s/versions";
    public static final String VERSION = "/api/projects/%s/versions/%s";
    public static final String USER_LIST = "/api/users";
    public static final String USER = "/api/users/%s";
    public static final String DOWNLOAD_RECOMMENDED = "/api/projects/%s/versions/recommended/download";
    public static final String DOWNLOAD = "/api/projects/%s/versions/%s/download";

    private Routes() {}

}
