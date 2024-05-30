package zti.f1backend.data;

import org.springframework.data.jpa.repository.JpaRepository;

import zti.f1backend.models.Comments;

public interface CommentsRepository extends JpaRepository<Comments, Integer>{
    
}
