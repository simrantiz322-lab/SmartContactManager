package com.example.SmartContactManager.controllers;

import com.example.SmartContactManager.dao.ContactRepository;
import com.example.SmartContactManager.dao.UserRepository;
import com.example.SmartContactManager.entities.Contact;
import com.example.SmartContactManager.entities.User;
import com.example.SmartContactManager.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
//so in order to make it secure for user all the handlers after user only will get acces for normal users
//all the urls starting with user will be protected and will not be public they need to login por authorise themselves first

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @ModelAttribute          //this method will work everytime when user is fired
    public void addCommonData(Model m,
                              Principal principal) {

        String userName = principal.getName();
        //get the user from username(email)
        User user = userRepository.getUserbyUserName(userName);
        m.addAttribute("user", user);


    }


    //dasboard home
    @GetMapping("/index")
    public String index(Model m) {

        m.addAttribute("title", "User Dashboard");

        return "normal/user_dashboard";

    }

    //add contact form
    @GetMapping("/add-contact")
    public String addContact(Model m, HttpSession session) {


        m.addAttribute("title", "Add Contact");
        m.addAttribute("contact", new Contact());

        return "normal/add_contact_form";
    }


    //processing contact form

    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact,
                                 @RequestParam("profileImage") MultipartFile file,
                                 Principal principal,
                                 HttpSession session) {

        try {

            String userName = principal.getName();
            //get the user from username(email)
            User user = userRepository.getUserbyUserName(userName);

            if (file.isEmpty()) {


                //  System.out.println("Image not uploaded");
                contact.setImage("contact.png");

            } else {

                 String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

                contact.setImage(fileName);

                String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";

                File uploadFolder = new File(uploadDir);

                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                Path path = Paths.get(uploadDir, fileName);

                Files.copy(file.getInputStream(),
                        path,
                        StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image uploaded successfully");
            }
            contact.setUser(user);
            /*

            user.getContacts().add(contact);
            this.userRepository.save(user);  //this updates this existing user contacts

             */

            contactRepository.save(contact);

            //message success
            session.setAttribute("message", new Message("Contact successfully added.Add more!!", "success"));


        } catch (Exception e) {
            e.printStackTrace();

            //message error

            session.setAttribute("message", new Message("Something went wrong.", "danger"));

        }
        return "normal/add_contact_form";

    }


    //show contacts handler
    //per page 5 contacts
    //page-current page(0-n)
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") int page,
                               Model m,
                               Principal principal) {


        m.addAttribute("title", "Show Contacts");

        //send contact list from here
        //one method is you get user by using principal and then fetch contacts by
        // using user.getcontacts() methods wwhich will return list but we will go some other way
        //we can make contact repository it will also help us in doing pagination
        // but if i use contactrepository.findall metjhod directly it will return all the
        // contacts in the table so we will make custom method in contactrepository
        //which gives contacts only of the logged in user

        String username = principal.getName();  //email is fetched
        User user = userRepository.getUserbyUserName(username);
        int userId = user.getUserid();
        /*befoe doing pagination
        List<Contact> contacts=contactRepository.findContactsByUser(userId);

         */


        Pageable pageable = PageRequest.of(page, 5);
        Page<Contact> contacts = contactRepository.findContactsByUser(userId, pageable);

        m.addAttribute("contacts", contacts);
        m.addAttribute("currentpage", page);


        //gives how many page used to fill contacts
        m.addAttribute("totalpages", contacts.getTotalPages());


        return "normal/show_contacts";

    }


    //showing particular contact detail
    @GetMapping("/contact/{Cid}")
    public String showContactDetails(@PathVariable("Cid") int id,
                                     Model m,
                                     Principal principal) {


        Optional<Contact> contactOptional = contactRepository.findById(id);
        Contact contact = contactOptional.get();


        String userName = principal.getName();
        //get the user from username(email)
        User user = userRepository.getUserbyUserName(userName);

        if (user.getUserid() == contact.getUser().getUserid()) {
            m.addAttribute("contact", contact);
            m.addAttribute("title", contact.getName());
        }


        return "normal/contact-detail";
    }


    //deleting a contact
    @GetMapping("/delete/{id}")
    public String deleteContact(@PathVariable("id") int id,
                                Principal principal,
                                HttpSession session) {

        //not using delete by id method cause then anyone in the url can type id and it can get deleted
        //we need to make a check for that we need contact
        Optional<Contact> contactOptional = this.contactRepository.findById(id);
        Contact contact = contactOptional.get();


        String userName = principal.getName();
        User user = userRepository.getUserbyUserName(userName);

        //check
        if (user.getUserid() == contact.getUser().getUserid()) {
            this.contactRepository.delete(contact);
            session.setAttribute("message", new Message("Contact deleted successfully.", "success"));
        }
        return "redirect:/user/show-contacts/0";





    }


    //update form handler
    @GetMapping("/update/{id}")
    public String deleteContact(@PathVariable("id") int id,
                                Model m,
                                Principal principal){

        Optional<Contact> contactOptional = this.contactRepository.findById(id);
        Contact contact = contactOptional.get();

        m.addAttribute("title","update ");
        m.addAttribute("contact",contact);

        return "normal/update-form";

    }


    //your profile handler
    @GetMapping("/profile")
    public String yourProfile(Model m){
        m.addAttribute("tit;e","Your Profile");
        //user already added in common method
        return "normal/profile";
    }


    //open settings handler
    @GetMapping("/settings")
    public String openSettings(Model m){

        m.addAttribute("title","Settings");

        return "normal/settings";
    }


    //change password handler
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldpassword") String oldpassword,
                                 @RequestParam("newpassword") String newpassword,
                                 Principal principal){


        System.out.println("old"+oldpassword);
        System.out.println("new"+newpassword);

        String userName = principal.getName();
        //get the user from username(email)
        User user = userRepository.getUserbyUserName(userName);

        if(this.bCryptPasswordEncoder.matches(oldpassword,user.getPassword())){
            //change the pass

            user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
            userRepository.save(user);

        }else{

        }



        return "redirect:/user/index";

    }



















}
