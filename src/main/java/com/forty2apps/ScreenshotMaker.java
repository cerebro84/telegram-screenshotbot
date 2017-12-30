package com.forty2apps;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class ScreenshotMaker {

  private WebDriver driver;

  ScreenshotMaker() {
    driver = new PhantomJSDriver();
  }

  public byte[] takeScreenshotAsBytes(String text) throws IOException {
    try {
      driver.get(text);
    } catch (Exception e) {
      Logger.getLogger("ScreenshotMaker").log(Level.WARNING, e.getMessage(), e);
      driver = new PhantomJSDriver();
    }
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
  }

  public void destroy() { // TODO: call this method somewhere
    driver.quit();
  }
}