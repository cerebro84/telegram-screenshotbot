package com.forty2apps;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import org.apache.commons.validator.routines.UrlValidator;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class WebShotBot extends TelegramLongPollingBot {

  private final UrlValidator urlValidator = new UrlValidator();
  private final ScreenshotMaker screenshotMaker = new ScreenshotMaker();
  private static final Logger LOGGER = Logger.getLogger("WebShotBot");
  private final BotConfig botConfig;

  WebShotBot(BotConfig botConfig) {
    this.botConfig = botConfig;
  }

  @Override
  public void onUpdateReceived(Update update) {
    LOGGER.info("Received update: " + update);
    if (update.hasMessage() && update.getMessage().hasText()) {
      final Long chatId = update.getMessage().getChatId();
      if (urlValidator.isValid(update.getMessage().getText())) {
        sendTextMessage(chatId, "Working on it");
        SendPhoto sendPhotoRequest = new SendPhoto();
        sendPhotoRequest.setChatId(chatId);
        try {
          sendPhotoRequest.setNewPhoto("screenshot.png", new ByteArrayInputStream(
              screenshotMaker.takeScreenshotAsBytes(update.getMessage().getText())));
          sendPhoto(sendPhotoRequest);
        } catch (IOException | TelegramApiException e) {
          e.printStackTrace();
          sendTextMessage(chatId, "An error occurred while serving your request");
        }
      } else if (update.getMessage().getText().toLowerCase().startsWith("/start")) {
        sendTextMessage(chatId, "BOT started, send me a web address");
      } else {
        sendTextMessage(chatId,"Sorry, I don't know what you're talking about. Send me an URL maybe?");
      }
    }
  }

  @Override
  public String getBotUsername() {
    return botConfig.username();
  }

  @Override
  public String getBotToken() {
    return botConfig.token();
  }

  private void sendTextMessage(Long chatId, String message) {
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(chatId);
    sendMessage.setText(message);
    try {
      execute(sendMessage);
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }


}
