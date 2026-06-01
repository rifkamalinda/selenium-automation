Feature: Download and Upload barcode

	Scenario: To verify the user should be able to download and upload barcode
		Given the user launches the ZXing website
		When the user input random values to the fields
		And the user taps on Generate button
		Then the user should see Download button
		And the user taps on Download button
		And the user take screenshot of the barcode
		When the user launches the QR Decoder website
		And the user upload the barcode
		And the user taps on Submit button
		Then the now loading message should be displayed