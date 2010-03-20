import com.thoughtworks.selenium.SeleneseTestCase;



public class TwitterTest extends SeleneseTestCase {

	@Override
	public void setUp() throws Exception {
		setUp("http://twitter.com/", "*chrome");
	}

	public void testHomeLoginAndPostStatus() throws Exception {
		// Clean up
		selenium.deleteAllVisibleCookies();
		// Home page
		selenium.open("/");
		// Login form not shown
		verifyFalse(selenium.isVisible("signin_submit"));
		// Show login form
		selenium.click("//div[@id='topnav']/a/span");
		for (int second = 0;; second++) {
			if (second >= 60) {
				fail("timeout");
			}
			try { if (selenium.isVisible("signin_submit")) {
				break;
			} } catch (Exception e) {}
			Thread.sleep(1000);
		}

		verifyTrue(selenium.isVisible("signin_submit"));
		// Login
		selenium.type("username", "joesondowjpr10");
		selenium.type("password", "clarksmarket");
		selenium.click("signin_submit");
		selenium.waitForPageToLoad("30000");
		// New message
		String date = selenium.getEval("new Date().getTime()");
		String message = "Looking for fun and feeling groovy " + date + " times";
		// Not posted yet
		verifyFalse(selenium.isTextPresent(message));
		// Post tweet
		selenium.type("status", message);
		selenium.click("tweeting_button");
		for (int second = 0;; second++) {
			if (second >= 60) {
				fail("timeout");
			}
			try { if (selenium.isTextPresent(message)) {
				break;
			} } catch (Exception e) {}
			Thread.sleep(1000);
		}

		verifyTrue(selenium.isTextPresent(message));
		verifyEquals("", selenium.getValue("status"));
		// Clean up
		selenium.deleteAllVisibleCookies();
	}
}
