INSERT INTO users (username, email, password_hash) VALUES ('demo', 'demo@dta.local', 'temp-hash');
INSERT INTO cognitive_distortion (name, description) VALUES
('all-or-nothing thinking', 'Seeing things in black and white only'),
('catastrophizing', 'Expecting the worst outcome');
INSERT INTO cognitive_distortions (label, description, cbt_prompt_mapping) VALUES ('All-or-nothing thinking', 'Seeing things in black and white categories.', 'Help the user find a middle ground.');
INSERT INTO cognitive_distortions (label, description, cbt_prompt_mapping) VALUES ('Catastrophizing', 'Expecting the absolute worst to happen.', 'Ask the user for realistic outcomes.');
INSERT INTO cognitive_distortions (label, description, cbt_prompt_mapping) VALUES ('Mind reading', 'Assuming people are reacting negatively to you.', 'Ask the user for facts and evidence.');
INSERT INTO cognitive_distortions (label, description, cbt_prompt_mapping) VALUES ('Overgeneralization', 'Seeing a single negative event as a never-ending pattern.', 'Remind the user that one event is not a pattern.');
INSERT INTO cognitive_distortions (label, description, cbt_prompt_mapping) VALUES ('Should statements', 'Having strict rules for how you or others must act.', 'Suggest replacing should with prefer.');
