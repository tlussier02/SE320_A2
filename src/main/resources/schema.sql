CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(150) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_sessions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  state VARCHAR(50),
  started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ended_at TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS cbt_sessions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  focus_area VARCHAR(255),
  session_state VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS chat_messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  session_id BIGINT,
  cbt_session_id BIGINT,
  role VARCHAR(50) NOT NULL,
  content TEXT NOT NULL,
  sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (session_id) REFERENCES user_sessions(id),
  FOREIGN KEY (cbt_session_id) REFERENCES cbt_sessions(id)
);

CREATE TABLE IF NOT EXISTS diary_entries (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS cognitive_distortions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  label VARCHAR(100) NOT NULL UNIQUE,
  description TEXT,
  cbt_prompt_mapping TEXT
);

CREATE TABLE IF NOT EXISTS diary_entry_distortions (
  diary_entry_id BIGINT NOT NULL,
  distortion_id BIGINT NOT NULL,
  PRIMARY KEY (diary_entry_id, distortion_id),
  FOREIGN KEY (diary_entry_id) REFERENCES diary_entries(id),
  FOREIGN KEY (distortion_id) REFERENCES cognitive_distortions(id)
);

CREATE TABLE IF NOT EXISTS trusted_contacts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  phone VARCHAR(50) NOT NULL,
  relation VARCHAR(100),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
