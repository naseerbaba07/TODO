// package com.example.myapp.controller;

// import com.example.myapp.entity.AppUser;
// import com.example.myapp.entity.TodoUser;
// import com.example.myapp.repo.UserRepository;
// import com.example.myapp.repo.TodoRepo;
// // import com.example.myapp.config.JwtUtil;

// import java.security.Principal;
// import java.util.List;
// import java.util.Map;

// // import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.*;

// import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/auth")
// @RequiredArgsConstructor
// public class AuthController {

//     private final UserRepository userRepo;
//     private final TodoRepo todoRepo;
//     private final PasswordEncoder encoder;
//     private final JwtUtil jwtUtil;

//     @PostMapping("/signup")
//     public String signup(@RequestBody AppUser user) {
//         user.setPassword(encoder.encode(user.getPassword()));
//         userRepo.save(user);
//         return "User registered";
//     }

//     @PostMapping("/login")
//     public Map<String, String> login(@RequestBody AppUser request) {
//         AppUser user = userRepo.findByUsername(request.getUsername())
//                 .orElseThrow(() -> new RuntimeException("User not found"));

//         if (!encoder.matches(request.getPassword(), user.getPassword())) {
//             throw new RuntimeException("Invalid password");
//         }

//         String token = jwtUtil.generateToken(user.getUsername());
//         return Map.of("token", token);
//     }

//    @PostMapping("/save")
//     public TodoUser saveTodo(@RequestBody TodoUser todo, Principal principal) {
//     AppUser user = userRepo.findByUsername(principal.getName())
//             .orElseThrow(() -> new RuntimeException("User not found"));

//     todo.setUser(user);
//     return todoRepo.save(todo);
// }

//     @GetMapping("/todos")
//     public List<TodoUser> getTodos(Principal principal) {
//     return todoRepo.findByUserUsername(principal.getName());
// }
// }