drop table if exists Activity;
drop table if exists belong;
drop table if exists "Group";
drop table if exists "User";
drop table if exists Participant;
drop domain if exists EmailValue;
drop type if exists RoleValue;
drop domain if exists RateValue;
create domain EmailValue as varchar(100)
check (value ~ '[a-zA-Z0-9_.-]+@[a-zA-Z0-9_.-]+');
-- the regexp is approximate e.g. it allows "a@b", "_@.", etc.
create domain RoleValue as varchar(8)
check (value in('student', 'lecturer', 'mentor'));
create domain RateValue as varchar(8)
check (value in('t', 'f'));
-- there's no real order here, but we use enum for variety
drop table if exists Participant;
create table Participant (
id serial,
primary key(id)
);
create table "User" (
id integer references Participant(id) on delete cascade,
name text NOT NULL,
email EmailValue NOT NULL UNIQUE,
password text NOT NULL,
is_admin boolean NOT NULL DEFAULT FALSE,
primary key(id)

);
create table "Group" (
id integer references Participant(id) on delete cascade,
name text NOT NULL UNIQUE,
primary key(id)
);
alter table "Group"
ADD relatedGroup integer references "Group"(id) DEFAULT NULL;

create table belong (
userId integer references "User"(id) on delete cascade,
groupId integer references "Group"(id) on delete cascade,
role RoleValue NOT NULL,
primary key(userId,groupId)
);
create table Activity (
id serial,
userId integer references "User"(id) NOT NULL,
participantId integer references Participant(id) default NULL,
title text default NULL,
message text default NULL,
timeposted timestamp NOT NULL,
lastmodified timestamp NOT NULL,
--MATERIAL
attachment text default NULL,
--MEETING
meetingStartDateTime timestamp default NULL,
meetingEndDateTime timestamp default NULL,
active RateValue default NULL,
--ASSIGNMENT
fullMark float default NULL,
--MEETING + ASSIGNMENT
googleId text default NULL,
dueDate text default NULL,
--RATE
rate RateValue default NULL,
primary key(id)
);
alter table Activity
--comment
ADD relatedActivity integer references Activity(id) default NULL;
alter table Activity
ADD constraint checkActivityIntegrity check
((attachment is null and meetingStartDateTime is null and fullMark is null and dueDate is null and relatedActivity is null and rate is null) or
(attachment is not null and meetingStartDateTime is null and fullMark is null and dueDate is null and relatedActivity is null and rate is null) or
(attachment is null and meetingStartDateTime is not null and fullMark is null and dueDate is null and relatedActivity is null and rate is null) or
(attachment is not null and meetingStartDateTime is null and fullMark is not null and dueDate is not null and relatedActivity is null and rate is null) or
(attachment is null and meetingStartDateTime is null and fullMark is null and dueDate is null and relatedActivity is not null and rate is null) or
(attachment is null and meetingStartDateTime is null and fullMark is null and dueDate is null and relatedActivity is not null and rate is not null));