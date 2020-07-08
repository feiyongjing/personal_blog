package FirstSpring.integration;

import FirstSpring.Application;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class MyIntegrationTast {
    @Inject
    Environment environment;

    @Test
    public void IndexHtmlAccessible() throws IOException {
        String port = environment.getProperty("local.server.port");
        try {
            HttpResponse httpResponse = Request.Get("http://localhost:" + port + "auth")
                    .execute().returnResponse();
            String body = EntityUtils.toString(httpResponse.getEntity());
            int firstBodyIndex = body.indexOf("\"msg\":\"");
            int lastBodyIndex = body.indexOf("\",\"data\":");
            System.out.println(httpResponse.getStatusLine().getStatusCode());
            Assertions.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
            Assertions.assertEquals("\"msg\":\"用户没有登录", body.substring(firstBodyIndex, lastBodyIndex));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
