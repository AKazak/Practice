package by.bsu.up.chat.storage;

import by.bsu.up.chat.Constants;
import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import by.bsu.up.chat.utils.MessageHelper;
import by.bsu.up.chat.utils.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages = new ArrayList<>();

    public InMemoryMessageStorage() {
        if (new File(DEFAULT_PERSISTENCE_FILE).isFile()) {
            try (FileReader persistenceFile = new FileReader(DEFAULT_PERSISTENCE_FILE)) {
                StringBuilder json = new StringBuilder();
                while (persistenceFile.ready()) {
                    json.append((char) persistenceFile.read());
                }
                JSONParser parser = new JSONParser();
                JSONArray messages = JSONArray.class.cast(parser.parse(json.toString().trim()));
                for (Object message : messages) {
                    addMessage(messageFromJson((JSONObject) message));
                }
            } catch (IOException | ParseException e) {
                logger.error("[MessageStorage] Error while reading from the storage ", e);
            }
        }
    }

    public Message messageFromJson(JSONObject jsonObject) {
        String id = ((String) jsonObject.get(Constants.Message.FIELD_ID));
        String author = ((String) jsonObject.get(Constants.Message.FIELD_AUTHOR));
        long timestamp = ((long) jsonObject.get(Constants.Message.FIELD_TIMESTAMP));
        String text = ((String) jsonObject.get(Constants.Message.FIELD_TEXT));
        Message message = new Message();
        message.setId(id);
        message.setAuthor(author);
        message.setTimestamp(timestamp);
        message.setText(text);
        return message;
    }

    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    @Override
    public void addMessage(Message message) {
        if (!StringUtils.isEmpty(message.getText())) {
            messages.add(message);
            logger.info("[Message storage] Add message with id " + message.getId());
            writeToPersistenceFile();
        } else {
            logger.info("[Message storage] Ignore empty message");
        }
    }

    @Override
    public boolean updateMessage(Message message) {
        for (Message m : messages) {
            if (m.getId().equals(message.getId()) && !StringUtils.isEmpty(m.getText()) && !StringUtils.isEmpty(message.getText())) {
                m.setText(message.getText());
                logger.info("[Message storage] Update message with id " + message.getId());
                writeToPersistenceFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean removeMessage(String messageId) {
        for (Message m : messages) {
            if (m.getId().equals(messageId)) {
                messages.remove(m);
                logger.info("[Message storage] Remove message with id " + m.getId());
                writeToPersistenceFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return messages.size();
    }

    public void writeToPersistenceFile() {
        try (FileWriter persistenceFile = new FileWriter(DEFAULT_PERSISTENCE_FILE)) {
            persistenceFile.write(MessageHelper.getJsonArrayOfMessages(messages).toJSONString());
        } catch (IOException e) {
            logger.error("[Message Storage] Error while writing to the storage", e);
        }
    }
}
