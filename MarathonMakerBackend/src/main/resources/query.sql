select loop.bib, loop.loop, loop.lat, loop.long longitude, loop.created_at, loop.checkpoint, t2.id, t2.address,  t2.event_id, t2.user_id, t2.uname, t2.ename from loop right outer join
     (select users.name uname, users.id, users.address, t1.bib, t1.event_id, t1.user_id, t1.name ename from users
           inner join
                   (select user_bib.bib, user_bib.event_id, user_bib.user_id, event.name from user_bib
                              join event
                    on event.id = user_bib.event_id) t1
      on users.id=t1.user_id) t2
on t2.bib = loop.bib