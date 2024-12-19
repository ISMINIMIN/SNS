package minzdev.sns.controller;

import lombok.AllArgsConstructor;
import minzdev.sns.controller.request.CommentCreateRequest;
import minzdev.sns.controller.request.PostCreateRequest;
import minzdev.sns.controller.request.PostUpdateRequest;
import minzdev.sns.controller.response.CommentResponse;
import minzdev.sns.controller.response.PostResponse;
import minzdev.sns.controller.response.Response;
import minzdev.sns.model.dto.Post;
import minzdev.sns.service.PostDetailService;
import minzdev.sns.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostDetailService postDetailService;

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

    @GetMapping
    public Response<Page<PostResponse>> getAll(Pageable pageable, Authentication auth) {
        return Response.success(postService.getAll(pageable).map(PostResponse::from));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> getMy(Pageable pageable, Authentication auth) {
        return Response.success(postService.getMy(auth.getName(), pageable).map(PostResponse::from));
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable Integer postId, Authentication auth) {
        postDetailService.like(postId, auth.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<Integer> countLike(@PathVariable Integer postId, Authentication auth) {
        return Response.success(postDetailService.countLike(postId));
    }

    @PostMapping("/{postId}/comments")
    public Response<Void> createComment(@PathVariable Integer postId, @RequestBody CommentCreateRequest request, Authentication auth) {
        postDetailService.createComment(postId, request.getComment(), auth.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> createComment(@PathVariable Integer postId, Pageable pageable, Authentication auth) {
        return Response.success(postDetailService.getComments(postId, pageable).map(CommentResponse::from));
    }

}
