package com.example.demo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("api/v1")
public class UrlShortenerController {

    StringRedisTemplate redisTemplate;

    public UrlShortenerController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/{id}")
    public RedirectView getUrl(@PathVariable String id){
        var url = redisTemplate.opsForValue().get(id);

        if(url == null){
            throw new RuntimeException("This URL is not available or has been expired");
        }
        return new RedirectView(url);
    }

    @PostMapping
    public URI create(@RequestBody String url){

        var urlValidator = new UrlValidator(new String[]{"http", "https"});

        if(urlValidator.isValid(url)){
            var id = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString();
            System.out.println("URL ID GENERATED: " + id);
            redisTemplate.opsForValue().set(id, url);
            return linkTo(methodOn(UrlShortenerController.class).getUrl(id)).toUri();
        }
        throw new RuntimeException("URL is not valid");
    }    
}
