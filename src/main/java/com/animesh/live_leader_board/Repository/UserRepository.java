package com.animesh.live_leader_board.Repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import com.animesh.live_leader_board.Model.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<Boolean> existsByUsername(String username);  //simple query to check wheter registration is new or not
}