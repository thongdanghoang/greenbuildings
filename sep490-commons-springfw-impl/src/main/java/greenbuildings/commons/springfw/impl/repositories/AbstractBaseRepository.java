package greenbuildings.commons.springfw.impl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AbstractBaseRepository<E> extends JpaRepository<E, UUID> {
}
