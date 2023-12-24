package gr.hua.dit.aimodotes.demo.repository;


import gr.hua.dit.aimodotes.demo.entity.DonationRequest;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path= "donationrequest")
@Hidden
public interface DonationRequestRepository extends JpaRepository<DonationRequest, Integer> {
}
