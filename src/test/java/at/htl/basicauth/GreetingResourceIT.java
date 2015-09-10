package at.htl.basicauth;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class GreetingResourceIT {

    private Client client;
    private WebTarget target;

    @Before
    public void initClient() {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature
                .basicBuilder()
                .nonPreemptive()
                .credentials("student","passmex")
                .build();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(feature);
        client = ClientBuilder.newClient(clientConfig);

//        client = ClientBuilder.newClient();
        this.target = client.target("http://localhost:8080/basicauth/rs/greeting");
    }

    @Test
    public void greetingTestOk() {
//        String hello = this.target
//                .request(MediaType.TEXT_HTML)
//                .get(String.class);
       final Response response = this.target
                .request()
                .get();
        String hello = response.readEntity(String.class);
        System.out.println(hello);
        assertThat(hello,containsString("Hello from REST!"));
        assertThat(response.getStatus(),is(200));
    }

    /**
     * Hier werden die Credentials überschrieben
     */
    @Test
    public void greetingTestFailedWrongCredentials() {
       final Response response = this.target
                .request()
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME,"wrong username")
                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD,"wrong password")
                .get();
        String hello = response.readEntity(String.class);
        assertThat(response.getStatus(),is(401));
    }

}
