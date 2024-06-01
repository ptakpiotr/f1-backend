package zti.f1backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import zti.f1backend.data.PostsRepository;
import zti.f1backend.data.UserRepository;
import zti.f1backend.models.GeneralizedResponse;
import zti.f1backend.models.Post;
import zti.f1backend.models.User;
import zti.f1backend.models.dto.PostCreateDTO;
import zti.f1backend.models.dto.PostEditDTO;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    public PostsController(PostsRepository postsRepository, UserRepository userRepository) {
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postsRepository.findAll();

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<GeneralizedResponse> addPost(@Valid @RequestBody PostCreateDTO postDto) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());

        if (user == null) {
            return new ResponseEntity<>(new GeneralizedResponse("Cannot create new post"), HttpStatus.BAD_REQUEST);
        }
        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setPhoto(postDto.getPhoto());
        post.setTitle(postDto.getTitle());
        post.setUser(user);
        postsRepository.save(post);

        return new ResponseEntity<>(new GeneralizedResponse("New post created"), HttpStatus.CREATED);
    }

    @PutMapping("{postId}")
    public ResponseEntity<HttpStatusCode> editPost(@PathVariable int postId, @Valid @RequestBody PostEditDTO postDto) {
        Post post = postsRepository.findById(postId).get();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());

        if (postDto.getContent() != null) {
            post.setContent(postDto.getContent());
        }

        if (postDto.getPhoto() != null) {
            post.setPhoto(postDto.getPhoto());
        }

        if (postDto.getTitle() != null) {
            post.setTitle(postDto.getTitle());
        }

        if (user.getId() == post.getUser().getId()) {
            postsRepository.save(post);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<HttpStatusCode> deletePost(@PathVariable int postId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        Optional<Post> post = postsRepository.findById(postId);

        if (post.isPresent() && user.getId() == post.get().getUser().getId()) {
            postsRepository.delete(post.get());
        } else if (!post.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
