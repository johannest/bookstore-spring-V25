package org.vaadin.example;

import com.vaadin.flow.testutil.ChromeBrowserTest;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.loadtest.LoadTestItHelper;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Base class for ITs
 * <p>
 * The tests use Chrome driver (see pom.xml for integration-tests profile) to
 * run integration tests on a headless Chrome. If a property {@code test.use
 * .hub} is set to true, {@code AbstractViewTest} will assume that the
 * TestBench test is running in a CI environment. In order to keep the this
 * class light, it makes certain assumptions about the CI environment (such
 * as available environment variables). It is not advisable to use this class
 * as a base class for you own TestBench tests.
 * <p>
 * To learn more about TestBench, visit
 * <a href="https://vaadin.com/docs/testbench/testbench-overview.html">Vaadin TestBench</a>.
 */
public abstract class AbstractViewTest extends ChromeBrowserTest {
    private static final int SERVER_PORT = 8081;

    private final String route;
    private final By rootSelector;

    @Rule
    public ScreenshotOnFailureRule rule = new ScreenshotOnFailureRule(this,
            false);

    public AbstractViewTest() {
        this("", By.tagName("body"));
    }

    protected AbstractViewTest(String route, By rootSelector) {
        this.route = route;
        this.rootSelector = rootSelector;
    }

    @Before
    @Override
    public void setup() throws Exception {
        super.setup();
        testBench().resizeViewPortTo(1000, 1000);
        
        setDriver(LoadTestItHelper.openWithProxy( getDriver(), getTestURL()));
    }

    @Override
    protected String getTestPath() {
        return "/";
    }

    @Override
    protected int getDeploymentPort() {
        return Integer.parseInt(System.getProperty("app.port", "8081"));
    }

    @Override
    protected void open() {
        doOpen();
    }

    protected void doOpen() {
        super.open();
    }

//    @Before
//    public void setup() throws Exception {
//        if (isUsingHub()) {
//            super.setup();
//        } else {
//            ChromeOptions options = new ChromeOptions();
//            if (Boolean.getBoolean("headless")) {
//                options.addArguments("--headless");
//            }
//            setDriver(TestBench.createDriver(new ChromeDriver(options)));
//        }
//        getDriver().get(getURL(route));
//    }

    /**
     * Convenience method for getting the root element of the view based on
     * the selector passed to the constructor.
     *
     * @return the root element
     */
    protected WebElement getRootElement() {
        return findElement(rootSelector);
    }

    /**
     * Property set to true when running on a test hub.
     */
    private static final String USE_HUB_PROPERTY = "test.use.hub";

    /**
     * Returns deployment host name concatenated with route.
     *
     * @return URL to route
     */
    private static String getURL(String route) {
        return String.format("http://%s:%d/%s", "localhost",
                SERVER_PORT, route);
    }

    /**
     * Returns whether we are using a test hub. This means that the starter
     * is running tests in Vaadin's CI environment, and uses TestBench to
     * connect to the testing hub.
     *
     * @return whether we are using a test hub
     */
    private static boolean isUsingHub() {
        return Boolean.TRUE.toString().equals(
                System.getProperty(USE_HUB_PROPERTY));
    }

    /**
     * If running on CI, get the host name from environment variable HOSTNAME
     *
     * @return the host name
     */
//    private static String getDeploymentHostname() {
//        return isUsingHub() ? System.getenv("HOSTNAME") : "localhost";
//    }
}
