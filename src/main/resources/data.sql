-- Seed Users
INSERT INTO users (username, email, password_hash) VALUES ('demo', 'demo@dta.local', 'temp-hash');
INSERT INTO users (username, email, password_hash) VALUES ('timmy', 'timmy@theraburn.com', 'hashedpassword123');

-- Seed Trusted Contacts (Linked to Timmy)
INSERT INTO trusted_contacts (user_id, name, phone, relation) VALUES (2, 'Dr. Smith', '555-0199', 'Therapist');

-- Seed Cognitive Distortions
INSERT INTO cognitive_distortions (label, description, cbt_prompt_mapping) VALUES 
('All-or-nothing thinking', 'Seeing things in black and white categories.', 'Help the user find a middle ground.'),
('Catastrophizing', 'Expecting the absolute worst to happen.', 'Ask the user for realistic outcomes.'),
('Mind reading', 'Assuming people are reacting negatively to you.', 'Ask the user for facts and evidence.'),
('Overgeneralization', 'Seeing a single negative event as a never-ending pattern.', 'Remind the user that one event is not a pattern.'),
('Should statements', 'Having strict rules for how you or others must act.', 'Suggest replacing should with prefer.');
