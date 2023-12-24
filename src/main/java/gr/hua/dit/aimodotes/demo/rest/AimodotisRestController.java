package gr.hua.dit.aimodotes.demo.rest;

import gr.hua.dit.aimodotes.demo.repository.AimodotisRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aimodotis")
public class AimodotisRestController {
    private AimodotisRepository aimodotisRepository;

}
