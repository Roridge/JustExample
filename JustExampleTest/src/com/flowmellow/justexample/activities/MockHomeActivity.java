package com.flowmellow.justexample.activities;

import com.flowmellow.justexample.activities.HomeActivity;

public class MockHomeActivity extends HomeActivity {
	
	private boolean isGPSProviderDisabled = true;
	private boolean showNoLocationWarningDialogCalled = false;
	
	public void isGPSProviderDisabled(boolean disabled) {
		isGPSProviderDisabled = disabled;
	}

	@Override
	protected boolean isGPSProviderDisabled() {
		return isGPSProviderDisabled;
	}
	
	@Override
	protected void showNoLocationWarningDialog() {
		showNoLocationWarningDialogCalled = true;
	}
	
	protected boolean warningDialogDisplayed() {
		return showNoLocationWarningDialogCalled;
	}
	
}
