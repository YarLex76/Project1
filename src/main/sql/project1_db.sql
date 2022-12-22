create TABLE Person(
                       id int SERIAL PRIMARY KEY,
                       name varchar NOT NULL,
                       age int check (age>10),
                       email varchar UNIQUE,
                       address varchar NOT NULL
)



CREATE TABLE Book (
                      id SERIAL PRIMARY KEY,
                      title varchar(100) NOT NULL,
                      author varchar(100) NOT NULL,
                      year int NOT NULL,
                      person_id int REFERENCES Person(id) ON DELETE SET NULL
);