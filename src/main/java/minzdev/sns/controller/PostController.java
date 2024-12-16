package minzdev.sns.controller;

import lombok.AllArgsConstructor;
import minzdev.sns.controller.request.PostCreateRequest;
import minzdev.sns.controller.request.PostUpdateRequest;
import minzdev.sns.controller.response.PostResponse;
import minzdev.sns.controller.response.Response;
import minzdev.sns.model.dto.Post;
import minzdev.sns.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{postId}")
    public Response<PostResponse> update(@PathVariable Integer postId, @RequestBody PostUpdateRequest request, Authentication auth) {
        Post post = postService.update(request.getTitle(), request.getBody(), auth.getName(), postId);
        return Response.success(PostResponse.from(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication auth) {
        postService.delete(auth.getName(), postId);
        return Response.success();
    }

}
