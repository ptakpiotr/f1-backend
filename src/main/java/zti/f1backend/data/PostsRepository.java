package zti.f1backend.data;

import org.springframework.data.jpa.repository.JpaRepository;

import zti.f1backend.models.Post;

public interface PostsRepository extends JpaRepository<Post, Integer> {

}
