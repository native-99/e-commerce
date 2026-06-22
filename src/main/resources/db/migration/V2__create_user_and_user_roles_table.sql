-- Create Users table
CREATE TABLE users (
                       user_id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NULL,

                       CONSTRAINT uq_users_email UNIQUE (email)
);

-- Create User Roles table
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role VARCHAR(50) NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NULL,

                            PRIMARY KEY (user_id, role),

                            CONSTRAINT fk_user_roles_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(user_id)
                                    ON DELETE CASCADE
);

-- Add indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_user_roles_role ON user_roles(role);
