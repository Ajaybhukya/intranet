// package com.intranet.client;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.context.annotation.Profile;
// import org.springframework.stereotype.Component;

// import com.intranet.dto.external.UserDTO;

// @Component
// @Profile("mock")
// public class UserClientFallback implements UserClient {

//     @Override
//     public UserDTO getUserById(Long id) {
//         return new UserDTO(id, "Mock User " + id, "mock" + id + "@example.com");
//     }

//     @Override
//     public List<UserDTO> getUsersByIds(List<Long> ids) {
//         return ids.stream()
//                 .map(id -> new UserDTO(id, "Mock User " + id, "mock" + id + "@example.com"))
//                 .collect(Collectors.toList());
//     }
// }
