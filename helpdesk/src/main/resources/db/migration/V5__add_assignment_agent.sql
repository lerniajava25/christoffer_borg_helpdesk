alter table tickets
    add column assigned_agent_id bigint references users(id);