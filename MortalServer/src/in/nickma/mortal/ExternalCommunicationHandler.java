package in.nickma.mortal;

import java.io.InputStream;
import java.net.URL;

public class ExternalCommunicationHandler {

    private final String username;
    private final String password;

    private static final String INITIAL_URL =
            //"http://www.hacker.org/coil/index.php?name=%s&password=%s&gotolevel=1";
            "http://www.hacker.org/coil/index.php?name=%s&password=%s";
    private static final String SUBMIT_URL =
            "http://www.hacker.org/coil/index.php?name=%s&password=%s&path=%s&x=%d&y=%d";

    public ExternalCommunicationHandler(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public InputStream getInitialStream() {
        return buildAndHandleURL(String.format(INITIAL_URL, username, password));
    }

    public InputStream getSubmissionsStream(final String path, final Integer x, final Integer y) {
        System.out.println(String.format(SUBMIT_URL, username, password, path, x, y));
        return buildAndHandleURL(String.format(SUBMIT_URL, username, password, path, x, y));
    }

    private InputStream buildAndHandleURL(final String urlString) {
        try {
            URL url = new URL(urlString);
            return url.openStream();
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return null;
        }
    }

}
