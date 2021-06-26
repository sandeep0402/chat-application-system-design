package gchat.group;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Integer> {

    ChatGroup findByName(String name);

    List<ChatGroup> findAllByUsers_Id(Long id);

    ChatGroup findFirstByNameAndUsers_Id(String name, Long id);
}
