import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

/**
 * Class to compile a text file into a collection of valid twitter messages
 */
public class ValidationTest {
    private WebDriver driver;
    private String baseUrl;
    @SuppressWarnings("MismatchedQueryAndUpdateOfStringBuilder")
    private StringBuffer verificationErrors = new StringBuffer();

    // The current working directory
    private static String currentDir = System.getProperty("user.dir");

    /*
     * Set this flag to true if you would like to use a custom path for your
     * input file, i.e. story.txt; next, change the "story" parameter in the
     * commands.properties file to match your desired path. Otherwise, set this flag
     * to false if you would like to continue to use the default input file
     * path, i.e. twitter-story-teller/src/main/java/story.txt.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private static Boolean customStoryPathFlag = false;

    /*
     * Set this flag to true if your tweets are not safe for work and you would
     * like to keep your window minimized. Tweets will continue to be automated
     * in the background. Otherwise, set this flag to false if you don't care.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private static Boolean tweetsNSFW = false;

    private static final String CONFIG_PATH = currentDir + "\\src\\main\\java\\commands.properties";


    @Before
    public void setUp() throws Exception {

        // Google Chrome
        File file = new File(currentDir + "\\src\\main\\java\\chromedriver.exe");

        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
        driver = new ChromeDriver();

        // Firefox
        // driver = new FirefoxDriver();

        baseUrl = "https://www.twitter.com";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }


    @Test
    public void testValidation() throws Exception {

        // Configure properties file
        Properties props = new Properties();

        FileInputStream resourceStream = new FileInputStream(new File(CONFIG_PATH));
        props.load(resourceStream);

        // Authentication information
        /*
         * Update the commands.properties file with the appropriate information
		 * for your project
		 */
        if (!(props.getProperty("username").isEmpty()) && !(props.getProperty("password").isEmpty())) {
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            // Open site
            launchActivity();

            //noinspection PointlessBooleanExpression
            if (tweetsNSFW == false) {
                // Maximize window on start up
                maximizeWindow();
            } else {
                // Minimize window on start up (for NSFW)
                minimizeWindow();
            }

            // Enter authentication information
            sendKeysAndWait("signin-email", username);
            sendKeysAndWait("signin-password", password);

            // Log in
            clickXPathButtonAndWait("//button[@type='submit']");

            // Parses a text file to produce an ArrayList from which short (140
            // char) messages may be sent
            parseStory(props);

        } else {
            System.out.println("You need to input a username and password.");
        }

    }


    /**
     */
    private void launchActivity() {
        driver.get(baseUrl + "/");
    }


    @SuppressWarnings("unused")
    private void maximizeWindow() throws InterruptedException {
        driver.manage().window().maximize();
        sleepShort();
    }


    @SuppressWarnings("unused")
    private void minimizeWindow() throws InterruptedException {
        driver.manage().window().setPosition(new Point(-6000, 0));
    }


    /**
     * Writes a string to an HTML element and waits a random time
     *
     * @param elem The element to send the keys
     * @param keys The string to write to the element
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    private void sendKeysAndWait(String elem, String keys) throws InterruptedException {
        driver.findElement(By.id(elem)).click();
        driver.findElement(By.id(elem)).sendKeys(keys);
        sleepShort();
    }


    /**
     * @param id The id of the button found in HTML
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity.
     */
    private void clickButtonAndWait(@SuppressWarnings("SameParameterValue") String id) throws InterruptedException {
        driver.findElement(By.id(id)).click();
        sleepShort();
    }


    @SuppressWarnings("unused")
    private void clickCSSButtonAndWait(String id) throws InterruptedException {
        driver.findElement(By.cssSelector(id)).click();
        sleepShort();
    }


    private void clickXPathButtonAndWait(@SuppressWarnings("SameParameterValue") String id) throws InterruptedException {
        driver.findElement(By.xpath(id)).click();
        sleepShort();
    }


    private void parseStory(Properties props) throws IOException, InterruptedException {

        //noinspection PointlessBooleanExpression
        if (customStoryPathFlag == true) {
            String storyPath = props.getProperty("story");
            startStory(props, storyPath);
        } else {
            String storyPath = currentDir + "\\src\\main\\java\\story.txt";
            startStory(props, storyPath);
        }

    }


    private void startStory(Properties props, String storyPath)
            throws FileNotFoundException, IOException, InterruptedException {
        FileInputStream fstream = new FileInputStream(
				/*
				 * Update the path of your text file for your own project in the
				 * commands.properties file. You may place this anywhere.
				 */
                storyPath);

        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;

        //noinspection MismatchedQueryAndUpdateOfCollection
        List<String> list = new ArrayList<String>();

        // Reads non-empty lines one by one
        while ((strLine = br.readLine()) != null) {
            if (!strLine.isEmpty()) {
                list.add(strLine);

                // Create ArrayList to store text in 140 character chunks
                String[] stringArr = strLine.split("(?<=\\G.{140})");
                System.out.println(Arrays.toString(stringArr));

                // Loop through parsed ArrayList and send tweets
                for (String s : stringArr) {
                    clickButtonAndWait("global-new-tweet-button");
                    sendKeysAndWait("tweet-box-global", s);

                    String selectAll = Keys.chord(Keys.CONTROL, Keys.RETURN);
                    driver.findElement(By.id("tweet-box-global")).sendKeys(selectAll);
                    sleepLong();
                }

            }
        }

        System.out.println("All messages have been sent.");

        // Close the input stream
        br.close();
    }


    @SuppressWarnings("unused")
    private void sendDashedLinesStart() throws InterruptedException {
        clickButtonAndWait("global-new-tweet-button");
        sendKeysAndWait("tweet-box-global", "!----------");
        String selectAll = Keys.chord(Keys.CONTROL, Keys.RETURN);
        driver.findElement(By.id("tweet-box-global")).sendKeys(selectAll);
        sleepLong();
    }


    @SuppressWarnings("unused")
    private void sendDashedLinesEnd() throws InterruptedException {
        clickButtonAndWait("global-new-tweet-button");
        sendKeysAndWait("tweet-box-global", "----------!");
        String selectAll = Keys.chord(Keys.CONTROL, Keys.RETURN);
        driver.findElement(By.id("tweet-box-global")).sendKeys(selectAll);
        sleepLong();
    }


    private void sleepLong() throws InterruptedException {
        // Sends a tweet within a random interval of between 1 and 2 minutes
		/*int randomWaitDuration = 60000 + (int) (Math.random() * 120000);*/

        // Sends a tweet within a random interval of between 2.5 and 5 seconds
        int randomWaitDuration = 2500 + (int) (Math.random() * 5000);
        Thread.sleep(randomWaitDuration);
    }


    private void sleepShort() throws InterruptedException {
        int randomWaitDuration = 500 + (int) (Math.random() * 1000);
        Thread.sleep(randomWaitDuration);
    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
}
