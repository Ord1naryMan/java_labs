package com.demo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class DemoApplication {

    private final Repository repository;

    @Autowired
    public DemoApplication(Repository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }



    @PostMapping(value = "/user", headers = {"application/json"})
    public void getInfo(@RequestBody String userDto) {
        System.out.println(userDto);
//        repository.save(new User(
//                userDto.getName(),
//                userDto.getPhone(),
//                userDto.getEmail()
//        ));
    }

}
