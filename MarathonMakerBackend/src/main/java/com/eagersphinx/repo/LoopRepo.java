package com.eagersphinx.repo;

import com.eagersphinx.domain.dto.MarathonResponse;
import com.eagersphinx.domain.entity.Loop;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoopRepo extends CrudRepository<Loop, Integer> {
    Optional<Loop> findFirst1ByBibOrderByLoopDesc(int bib);

    List<Loop> findByBib(Integer bib);

    List<Loop> findByBibIn(List<Integer> collect);

    @Query(value = "select t2.awarded, t2.bib, loop.loop, loop.lat, loop.long longitude, loop.created_at, loop.checkpoint, t2.id, t2.address,  t2.event_id, t2.user_id, t2.uname, t2.ename from loop right outer join\n" +
            "     (select users.is_awarded awarded, users.name uname, users.id, users.address, t1.bib, t1.event_id, t1.user_id, t1.name ename from users  \n" +
            "           inner join\n" +
            "                   (select user_bib.bib, user_bib.event_id, user_bib.user_id, event.name from user_bib \n" +
            "                              join event \n" +
            "                    on event.id = user_bib.event_id) t1 \n" +
            "      on users.id=t1.user_id) t2 \n" +
            "on t2.bib = loop.bib", nativeQuery = true)
    List<MarathonResponse> findStatus();

}
