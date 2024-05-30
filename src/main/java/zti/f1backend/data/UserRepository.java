package zti.f1backend.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zti.f1backend.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}
