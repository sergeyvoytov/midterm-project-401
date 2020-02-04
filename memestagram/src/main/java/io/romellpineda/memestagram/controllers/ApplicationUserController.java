package io.romellpineda.memestagram.controllers;

import io.romellpineda.memestagram.models.ApplicationUser;
import io.romellpineda.memestagram.models.ApplicationUserRepository;

import io.romellpineda.memestagram.models.MemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class ApplicationUserController {

    @Autowired
    public MemeRepository memeRepository;

    @Autowired
    public ApplicationUserRepository applicationUserRepository;



    @Autowired
    private PasswordEncoder passwordEncoder;




    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }



    @PostMapping("/join")
    public RedirectView createNewApplicationUser(String username, String password, String profilePicture, String bio, String firstName){
        ApplicationUser newUser = new ApplicationUser(username, passwordEncoder.encode(password), profilePicture, bio, firstName);
        applicationUserRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new RedirectView("/logged");
    }

    @GetMapping("/developers")
    public String showdevelopers(){

        return "developers";
    }

    @GetMapping("/login")
    public String showlogin(){
        return "login";
    }



    @GetMapping("/users/{id}")
    public String showUserDetails(@PathVariable long id, Principal p, Model m){
        ApplicationUser usernameWeAreVisiting = applicationUserRepository.findById(id).get();
        m.addAttribute("usernameWeAreVisiting", usernameWeAreVisiting);
        m.addAttribute("principalName", p.getName());
        m.addAttribute("meme", memeRepository);
        return "profile";
    }

    @GetMapping("/userprofile")
    public RedirectView getLoggedInUsersId(Principal p, Model m){
        if (p != null) {
        ApplicationUser loggedInUser = applicationUserRepository.findByUsername(p.getName());
        return new RedirectView("/users/" + loggedInUser.id);
        } else {
            return new RedirectView("/login");
        }
    }

    @PostMapping("/logged")
    public RedirectView authententicatedUser(Principal p, Model m) {
        if (p != null) {
            ApplicationUser loggedUser = applicationUserRepository.findByUsername(p.getName());
            return new RedirectView("/users/" + loggedUser.getId());
        } else {
            return new RedirectView("/login");
        }
    }

}
