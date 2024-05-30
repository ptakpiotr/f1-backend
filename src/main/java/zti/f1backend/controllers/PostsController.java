package zti.f1backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        Optional<User> user = userRepository.findById(postDto.getUserId());

        if (!user.isPresent()) {
            return new ResponseEntity<>(new GeneralizedResponse("Cannot create new post"), HttpStatus.BAD_REQUEST);
        }
        User existingUser = user.get();
        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setPhoto(postDto.getPhoto());
        post.setTitle(postDto.getTitle());
        post.setUser(existingUser);
        postsRepository.save(post);

        return new ResponseEntity<>(new GeneralizedResponse("New post created"), HttpStatus.CREATED);
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<HttpStatusCode> deletePost(@PathVariable int postId) {
        postsRepository.deleteById(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
