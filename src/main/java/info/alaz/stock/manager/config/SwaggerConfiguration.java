package info.alaz.stock.manager.config;

import info.alaz.stock.manager.constant.APITags;
import info.alaz.stock.manager.web.ParentStockManagerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${domain-name}")
    private String domainName;

    @Autowired
    public SwaggerConfiguration() {
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(ParentStockManagerController.SWAGGER_VERSIONING)
                .tags(new Tag(APITags.RESTLESS_STOCK_MANAGER_TAG, APITags.RESTLESS_STOCK_MANAGER_DESCRIPTION, APITags.RESTLESS_STOCK_MANAGER_ORDER))
                .tags(new Tag(APITags.RESTFUL_STOCK_MANAGER_TAG, APITags.RESTFUL_STOCK_MANAGER_DESCRIPTION, APITags.RESTFUL_STOCK_MANAGER_ORDER))
                .host(domainName)
                .select()
                .apis(p -> {
                    if (p != null && p.produces() != null) {
                        for (MediaType mt : p.produces()) {
                            if (mt.toString().equals(ParentStockManagerController.STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)) {
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .build()
                .produces(Collections.singleton(ParentStockManagerController.STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE))
                .forCodeGeneration(true)
                .globalResponseMessage(RequestMethod.GET, responseMessages())
                .globalResponseMessage(RequestMethod.POST, responseMessages())
                .globalResponseMessage(RequestMethod.PUT, responseMessages())
                .globalResponseMessage(RequestMethod.PATCH, responseMessages())
                .globalResponseMessage(RequestMethod.DELETE, responseMessages());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Stock Manager",
                "This is an API to manage current stock of products and acquire relevant statistics about them.",
                "1.0",
                null,
                null,
                null,
                null,
                Collections.emptyList()
        );
    }

    private List<ResponseMessage> responseMessages() {
        List<ResponseMessage> messages = new ArrayList<>();
        messages.add(new ResponseMessageBuilder().code(406).message("Not Acceptable. Wrong Accept MediaType").build());
        messages.add(new ResponseMessageBuilder().code(416).message("Unsupported Media Type. Wrong ContentType").build());
        return messages;
    }

}
