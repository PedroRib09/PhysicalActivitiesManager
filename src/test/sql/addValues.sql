insert into users(uid, name, email) values (1, 'Rui Cunha', 'rui.cunh4@gmail.com');
insert into users(uid, name, email) values (2, 'Pedro Ribeiro', 'pedro.rib@gmail.com');
insert into users(uid, name, email) values (3, 'João Cravo', 'Joao.cravo@gmail.com');



insert into sport(sid, description, name) values (1, 'plays with feet', 'football');
insert into sport(sid, description, name) values (2, 'plays with skill', 'basketball');
insert into sport(sid, description, name) values (3, 'plays with hand', 'handball');

insert into route(rid, startloc, endloc, distance) values (1, 'Lisboa', 'Porto', 327.9);
insert into route(rid, startloc, endloc, distance) values (2, 'Porto', 'Coimbra', 120.6);
insert into route(rid, startloc, endloc, distance) values (3, 'Portugal', 'Espanha', 578.9);

insert into activity(aid, duration, date, uid, rid, sid) values (1, '12:30:55.444', '2020-11-06', 1, 1, 1,null);
insert into activity(aid, duration, date, uid, rid, sid) values (2, '01:20:44.312', '2021-02-06', 1, 2, 3,null);
insert into activity(aid, duration, date, uid, rid, sid) values (3, '06:32:32.654', '2019-02-06', 3, 3, 3,null);



