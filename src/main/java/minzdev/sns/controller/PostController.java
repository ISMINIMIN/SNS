package minzdev.sns.controller;

import lombok.AllArgsConstructor;
import minzdev.sns.controller.request.PostCreateRequest;
import minzdev.sns.controller.response.Response;
import minzdev.sns.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication auth) {
        postService.create(request.getTitle(), request.getBody(), auth.getName());
        return Response.success();
    }

}
