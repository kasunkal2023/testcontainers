-- init.sql
CREATE TABLE user_table (
                            id VARCHAR(255) PRIMARY KEY,
                            username VARCHAR(255),
                            email VARCHAR(255)
);

INSERT INTO user_table (id, username, email) VALUES ('1', 'Alice', 'alice@example.com');
INSERT INTO user_table (id, username, email) VALUES ('2', 'Bob', 'bob@example.com');
INSERT INTO user_table (id, username, email) VALUES ('3', 'Charlie', 'charlie@example.com');
