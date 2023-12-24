package gr.hua.dit.aimodotes.demo.repository;

import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import org.springframework.data.jpa.repository.JpaRepository;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path= "aimodotis")
@Hidden
public interface AimodotisRepository extends JpaRepository<Aimodotis, Integer> {
}
