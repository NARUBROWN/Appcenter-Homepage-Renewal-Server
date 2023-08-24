package home.inuappcenter.kr.appcenterhomepagerenewalserver;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers =
                            {@Server(url = "https://server.inuappcenter.kr/", description = "Default Server URL"), @Server(url = "/", description = "Development Server URL")})
@SpringBootApplication
@Slf4j
public class AppcenterHomepageRenewalServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppcenterHomepageRenewalServerApplication.class, args);
    }

}
