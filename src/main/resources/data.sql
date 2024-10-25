-- Insert roles
INSERT INTO role (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO role (id, name) VALUES (2, 'ROLE_USER');

-- Insert user
INSERT INTO app_user (id, username, password, email, full_name)
VALUES (1, 'john_doe', '$2a$10$tECecNpIs/LarsVnfuvjwOMQJvqMegD4Bx6yzkRuRabRIdU2UQ76K', 'john.doe@example.com', 'John Doe');

-- Link user to roles
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);  -- Linking user to ROLE_ADMIN
