package com.myorg.todo.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/admin/users")
public class UserController {

    @GetMapping("/get-all")
    public ResponseEntity<String> getAllUsers() {
        return new ResponseEntity<>("DONE ALL USER", HttpStatus.OK);
    }

    @GetMapping("/list-all-header")
    public ResponseEntity<Map<String, String>> listAllHeaders(@RequestHeader Map<String, String> headers) throws Exception {
        Map<String, String> headersMap = new HashMap<>();
        headers.forEach((key, value) -> {
            headersMap.put(key, value);
        });

        String token = headersMap.get("authorization");
        return new ResponseEntity<>(headersMap, HttpStatus.OK);
    }
}
