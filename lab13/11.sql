SELECT datediff(curdate(), (SELECT date from chat.messages where date = (select min(date) from chat.messages)));