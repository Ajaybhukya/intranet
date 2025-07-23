package com.intranet.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.intranet.dto.external.UserDTO;

@FeignClient(
    name = "user-management",
    url = "${user.service.url}", // configurable via application.properties
    fallback = UserClientFallback.class
)
public interface UserClient {

    @GetMapping("/users/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);

    @GetMapping("/users/bulk")
    List<UserDTO> getUsersByIds(@RequestParam("ids") List<Long> ids);
}
