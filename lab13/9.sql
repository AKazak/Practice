SELECT users.name, messages.text, messages.date FROM users join messages on messages.user_id = users.id where length(text) > 140;