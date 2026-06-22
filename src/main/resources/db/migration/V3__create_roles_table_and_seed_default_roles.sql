-- Add username column to users for authentication login identity
ALTER TABLE users
    ADD COLUMN username VARCHAR(255);

UPDATE users
SET username = email
WHERE username IS NULL;

ALTER TABLE users
    ALTER COLUMN username SET NOT NULL;

ALTER TABLE users
    ADD CONSTRAINT uq_users_username UNIQUE (username);

-- Create Roles table
CREATE TABLE roles (
                       role_id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       description TEXT,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NULL,

                       CONSTRAINT uq_roles_name UNIQUE (name)
);

-- Add indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_roles_name ON roles(name);

-- Insert default roles
INSERT INTO roles (name, description)
VALUES
    ('ROLE_USER', 'Standard user role'),
    ('ROLE_ADMIN', 'Administrative user role')
ON CONFLICT (name) DO NOTHING;

-- Connect user_roles.role values to roles.name
ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role)
            REFERENCES roles(name);
