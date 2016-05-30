SELECT users.name FROM messages join users on users.id = messages.user_id where date(date) = curdate() group by user_id having count(*) > 3;