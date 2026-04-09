INSERT INTO users (id, full_name, email, password_hash, role, deleted, created_at)
SELECT
  CAST('11111111-1111-1111-1111-111111111111' AS UUID),
  'Demo User',
  'demo@dta.local',
  '$2a$10$/.jmKA6nWdZLXGYOqu/yyOQ6OF0OULaBvQHAfD5qvtXUS2WrckuqC',
  'USER',
  FALSE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE email = 'demo@dta.local'
);

INSERT INTO users (id, full_name, email, password_hash, role, deleted, created_at)
SELECT
  CAST('22222222-2222-2222-2222-222222222222' AS UUID),
  'Timmy Ngo',
  'timmy@theraburn.com',
  '$2a$10$/.jmKA6nWdZLXGYOqu/yyOQ6OF0OULaBvQHAfD5qvtXUS2WrckuqC',
  'USER',
  FALSE,
  CURRENT_TIMESTAMP
WHERE NOT EXISTS (
  SELECT 1 FROM users WHERE email = 'timmy@theraburn.com'
);

INSERT INTO trusted_contacts (user_id, name, phone, relation)
SELECT
  CAST('22222222-2222-2222-2222-222222222222' AS UUID),
  'Dr. Smith',
  '555-0199',
  'Therapist'
WHERE NOT EXISTS (
  SELECT 1
  FROM trusted_contacts
  WHERE user_id = CAST('22222222-2222-2222-2222-222222222222' AS UUID)
    AND name = 'Dr. Smith'
);

INSERT INTO cognitive_distortions (label, description, cbt_prompt_mapping)
SELECT
  'All-or-nothing thinking',
  'Seeing things in black and white categories.',
  'Help the user find a middle ground.'
WHERE NOT EXISTS (
  SELECT 1 FROM cognitive_distortions WHERE label = 'All-or-nothing thinking'
);

INSERT INTO cognitive_distortions (label, description, cbt_prompt_mapping)
SELECT
  'Catastrophizing',
  'Expecting the absolute worst to happen.',
  'Ask the user for realistic outcomes.'
WHERE NOT EXISTS (
  SELECT 1 FROM cognitive_distortions WHERE label = 'Catastrophizing'
);

INSERT INTO cognitive_distortions (label, description, cbt_prompt_mapping)
SELECT
  'Mind reading',
  'Assuming people are reacting negatively to you.',
  'Ask the user for facts and evidence.'
WHERE NOT EXISTS (
  SELECT 1 FROM cognitive_distortions WHERE label = 'Mind reading'
);

INSERT INTO cognitive_distortions (label, description, cbt_prompt_mapping)
SELECT
  'Overgeneralization',
  'Seeing a single negative event as a never-ending pattern.',
  'Remind the user that one event is not a pattern.'
WHERE NOT EXISTS (
  SELECT 1 FROM cognitive_distortions WHERE label = 'Overgeneralization'
);

INSERT INTO cognitive_distortions (label, description, cbt_prompt_mapping)
SELECT
  'Should statements',
  'Having strict rules for how you or others must act.',
  'Suggest replacing should with prefer.'
WHERE NOT EXISTS (
  SELECT 1 FROM cognitive_distortions WHERE label = 'Should statements'
);
