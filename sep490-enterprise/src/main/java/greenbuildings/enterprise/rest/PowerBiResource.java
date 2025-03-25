package greenbuildings.enterprise.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/power-bi")
@RequiredArgsConstructor
@Slf4j
public class PowerBiResource {
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> test() throws IOException {
        log.info("Hello power-bi!!!");
        try (InputStream inputStream = Thread.currentThread()
                                             .getContextClassLoader()
                                             .getResourceAsStream("power-bi-response.json")) {
            
            return ResponseEntity.ok(StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8));
        }
    }
    
}
