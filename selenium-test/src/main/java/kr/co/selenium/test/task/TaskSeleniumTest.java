package kr.co.selenium.test.task;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@SuppressWarnings("unused")
public class TaskSeleniumTest {

	private Map<String, String> jobParametersMap = new HashMap<>();

	public Tasklet doTask(Map<String, String> jobParametersMap) {

		this.jobParametersMap = jobParametersMap;

		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

				try {

					String chromeDriver = "webdriver.chrome.driver";
					String chromePath   = "C:\\Users\\ksj\\_DEV\\chromedriver-win32\\chromedriver.exe";

					System.setProperty(chromeDriver, chromePath);
					System.setProperty("java.awt.headless", "false");

					ChromeOptions options = new ChromeOptions();
					options.setCapability("ignoreProtectedModeSettings", true);

					WebDriver driver = new ChromeDriver();
					WebDriverWait wait = new WebDriverWait(driver, 30);

					driver.get("https://naver.com/");

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#account > div > a")));
					driver.findElement(By.cssSelector("#account > div > a")).click();

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id")));

					// 네이버 캡챠우회
					StringSelection id = new StringSelection("아이디");
					Clipboard clipboardId = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboardId.setContents(id, id);

					driver.findElement(By.id("id")).sendKeys(Keys.CONTROL + "v");

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pw")));

					// 네이버 캡챠우회
					StringSelection pw = new StringSelection("비밀번호");
					Clipboard clipboardPw = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboardPw.setContents(pw, pw);

					driver.findElement(By.id("pw")).sendKeys(Keys.CONTROL + "v");

					wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("log.login")));
					driver.findElement(By.id("log.login")).click();

				} catch (Exception e) {
					e.printStackTrace();
				}

				return RepeatStatus.FINISHED;
			}
		};
	}
}
