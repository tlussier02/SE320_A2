erDiagram
    USER ||--o{ USER_SESSION : owns
    USER ||--o{ DIARY_ENTRY : writes
    USER ||--o{ TRUSTED_CONTACT : has
    USER_SESSION ||--o{ CHAT_MESSAGE : contains
    CBT_SESSION ||--o{ CHAT_MESSAGE : contains
    DIARY_ENTRY }|--|{ COGNITIVE_DISTORTION : identifies
    
    USER {
        long id PK
        string username
        string email
        string password_hash
    }
    USER_SESSION {
        long id PK
        string state
        timestamp started_at
        timestamp ended_at
    }
    DIARY_ENTRY {
        long id PK
        string title
        text content
        timestamp created_at
    }
    COGNITIVE_DISTORTION {
        long id PK
        string label
        text description
        text cbt_prompt_mapping
    }
    TRUSTED_CONTACT {
        long id PK
        string name
        string phone
        string relation
    }
