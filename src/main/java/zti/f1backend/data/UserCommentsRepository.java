package zti.f1backend.data;

import org.springframework.data.jpa.repository.JpaRepository;

import zti.f1backend.models.UserComments;

public interface UserCommentsRepository extends JpaRepository<UserComments, Integer> {

}
