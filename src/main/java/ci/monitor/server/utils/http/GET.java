package ci.monitor.server.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GET extends Request {

    /**
     * Creates the connection & gets the results.
     * @return this - builder pattern
     * @throws IOException - thrown by URL connections
     * 
     * TODO make most of this code part of the Request class
     */
    @Override
    public GET connect() throws IOException {
        URL url = new URL(this.url);

        HttpURLConnection con;

        if (ssl) con = (HttpsURLConnection) url.openConnection();
        else con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        for (String key : properties.keySet()) {
            con.setRequestProperty(key, properties.get(key));
        }
		if (cookies != null) {
			con.setRequestProperty("Cookie", cookies);
		}
        int code = con.getResponseCode();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuffer buffer = new StringBuffer();

        String input;
        while ((input = in.readLine()) != null) {
            buffer.append(input);
        }
        in.close();
        con.disconnect();

        result = new Result(code, buffer.toString());

        return this;
    }

}
