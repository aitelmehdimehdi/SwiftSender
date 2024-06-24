
insert into camion(active,matricule) values(false,'A123');
insert into conducteur(active,email,nom,prenom,telephone) values(false,'conducteur@gmail.com','chami','ahmad','0688991234');
insert	into compte(id,email,password,type) values('2024-12-03',3,'manager@gmail.com','manager','com.PFE.Entity.ManagerEntity');

insert into compte(date_creation_de_compte,id,email,password,type) values('2018-04-12',3,'manager@gmail.com','manager','com.PFE.EntityManagerEntity');

delete from compte where id=3;

select * from camion;
select * from conducteur;

select * from compte;

select * from conducteur;



select * from service;
select *from camion;
select * from service;
select * from conducteur;

delete from conducteur where email='aitelmehdi04@gmail.com';
select * from facture;
delete from compte where id=352;
select * from transit;camion
select * from service;
select * from compte;
select * from client;
rollback;

--truncate compte;
--truncate client;
--truncate table service_vehicule;

SET FOREIGN_KEY_CHECKS = 0;


alter table service drop foreign keyFKmlcw3qq5jq25xmhmjk9miussycompte

