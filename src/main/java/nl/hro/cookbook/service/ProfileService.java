package nl.hro.cookbook.service;


import nl.hro.cookbook.model.dto.ProfileDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProfileService {

    private List<ProfileDTO> profileDTO = new ArrayList<> (Arrays.asList(
            new ProfileDTO( "1", "Anuar", "Cat_meme.jpg"),
            new ProfileDTO( "2", "Bakkali", "Cat_meme2.jpg")
    ));

    public List<ProfileDTO> getAllProfiles(){
        return profileDTO;
    }

    public ProfileDTO getProfile(String id){

        return profileDTO.stream().filter(p -> p.getId().equals(id)).findFirst().get();
    }

    public void addProfile(ProfileDTO profileDTO){
        this.profileDTO.add(profileDTO);
    }

    public void updateProfile(ProfileDTO profileDTO){
        for (int i = 0; i < this.profileDTO.size(); i++) {
            ProfileDTO p = this.profileDTO.get(i);
            if (p.getId().equals(p.getId())) {
                this.profileDTO.set(i, profileDTO);
                return;
            }
        }
    }

}
