package stepDefinition;

import com.UIConstants.DownloadAndUploadBarcodeConstants;
import com.library.CommonLibrary;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DownloadUploadBarcodeStepDef {

	@Given("the user launches the ZXing website")
	public void launchZxing() throws Throwable {
		CommonLibrary.ZxingLaunch();
	}

	@When("the user launches the QR Decoder website")
	public void launchQrDecoder() throws Throwable {
		CommonLibrary.QrDecoderLaunch();
	}

	@When("the user input random values to the fields")
	public void inputRandomValues() throws Throwable {
		CommonLibrary.clearAndEnterText(DownloadAndUploadBarcodeConstants.FIELD_NAME, "Test");
		CommonLibrary.clearAndEnterText(DownloadAndUploadBarcodeConstants.FIELD_COMPANY, "Company");
		CommonLibrary.clearAndEnterText(DownloadAndUploadBarcodeConstants.FIELD_TITLE, "QA");
		CommonLibrary.clearAndEnterText(DownloadAndUploadBarcodeConstants.FIELD_PHONENO, "081232123112");
		CommonLibrary.clearAndEnterText(DownloadAndUploadBarcodeConstants.FIELD_EMAIL, "test@yopmail.com");
		CommonLibrary.clearAndEnterText(DownloadAndUploadBarcodeConstants.FIELD_ADDRESS, "Orchard Rd");
		CommonLibrary.clearAndEnterText(DownloadAndUploadBarcodeConstants.FIELD_ADDRESS2, "Singapore");
		CommonLibrary.clearAndEnterText(DownloadAndUploadBarcodeConstants.FIELD_WEBSITE, "zxing.com");
		CommonLibrary.clearAndEnterText(DownloadAndUploadBarcodeConstants.FIELD_MEMO, "Test");
	}

	@And("the user taps on Generate button")
	public void tapsGenerateBtn() throws Throwable {
		CommonLibrary.isElementPresentVerifyClick(DownloadAndUploadBarcodeConstants.BTN_GENERATE);
	}

	@Then("the user should see Download button")
	public void verifyDownloadBtn() throws Throwable {
		CommonLibrary.isElementPresentVerification(DownloadAndUploadBarcodeConstants.BTN_DOWNLOAD);
	}

	@When("the user taps on Download button")
	public void tapsDownloadBtn() throws Throwable {
		CommonLibrary.isElementPresentVerifyClick(DownloadAndUploadBarcodeConstants.BTN_DOWNLOAD);
	}

	@And("the user take screenshot of the barcode")
	public void takeScreenshotBarcode() throws Throwable {
		CommonLibrary.takeScreenShot();
	}

	@And("the user upload the barcode")
	public void uploadBarcode() throws Throwable {
		CommonLibrary.uploadFile();
	}

	@And("the user taps on Submit button")
	public void tapsSubmitBtn() throws Throwable {
		CommonLibrary.isElementPresentVerifyClick(DownloadAndUploadBarcodeConstants.BTN_SUBMIT);
	}

	@Then("the now loading message should be displayed")
	public void verifySuccessMsg() throws Throwable {
		CommonLibrary.isElementPresentVerification(DownloadAndUploadBarcodeConstants.DECODED_MSG);
	}

}
