package nl.hro.cookbook.controller;

import nl.hro.cookbook.model.dto.ProfileDTO;
import nl.hro.cookbook.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/profiles/")
    public List<ProfileDTO> getAllProfiles(){
        return profileService.getAllProfiles();
    }

    @RequestMapping("/profiles/{id}")
    public ProfileDTO getProfile(@PathVariable String id){
        return profileService.getProfile(id);
    }

    @RequestMapping(method = RequestMethod.POST, value="/profiles/")
    public void addProfile(@RequestBody ProfileDTO profileDTO) {
        profileService.addProfile(profileDTO);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/profiles/{id}")
    public void updateProfile(@RequestBody ProfileDTO profileDTO) {
        profileService.updateProfile(profileDTO);
    }

}
