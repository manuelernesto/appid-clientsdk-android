/*
	Copyright 2017 IBM Corp.
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package com.ibm.bluemix.appid.android.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ibm.bluemix.appid.android.api.userattributes.UserAttributeManager;
import com.ibm.bluemix.appid.android.internal.OAuthManager;
import com.ibm.bluemix.appid.android.internal.loginwidget.LoginWidgetImpl;
import com.ibm.bluemix.appid.android.internal.userattributesmanager.UserAttributeManagerImpl;

import org.jetbrains.annotations.NotNull;

public class AppID {

	private static AppID instance;

    private String tenantId;
    private String bluemixRegionSuffix;

	private LoginWidgetImpl loginWidget;
	private OAuthManager oAuthManager;
	private UserAttributeManager userAttributeManager;

    public static String overrideServerHost = null;
	public static String userProfilesHost = null;

    public final static String REGION_US_SOUTH = ".ng.bluemix.net";
    public final static String REGION_UK = ".eu-gb.bluemix.net";
    public final static String REGION_SYDNEY = ".au-syd.bluemix.net";

    private Context context;


    // TODO: document
	@NonNull
	public static synchronized AppID getInstance(){
		if (null == instance) {
			synchronized (AppID.class) {
				if (null == instance) {
					instance = new AppID();
				}
			}
		}
		return instance;
	}

	private AppID(){}

	// TODO: document
	@NonNull
	public AppID initialize (@NonNull Context context, @NonNull String tenantId, @NonNull String bluemixRegion) {
		this.tenantId = tenantId;
		this.bluemixRegionSuffix = bluemixRegion;
		this.oAuthManager = new OAuthManager(context.getApplicationContext(), this);
		this.loginWidget = new LoginWidgetImpl(this.oAuthManager);
		this.userAttributeManager = new UserAttributeManagerImpl(this.oAuthManager);
		this.context = context;
		return instance;
	}

    /**
     * @return The Context this AppID was initialized with
     */
    public Context getContext() {
        return context;
    }

    /**
     * @return The AppID instance tenantId
     */
	@NonNull
    public String getTenantId() {
		if (null == this.tenantId){
			throw new RuntimeException("AppID is not initialized. Use .initialize() first.");
		}
        return this.tenantId;
    }

    /**
     * @return Bluemix region suffix ,use to build URLs
     */
	@NonNull
    public String getBluemixRegionSuffix() {
		if (null == this.bluemixRegionSuffix){
			throw new RuntimeException("AppID is not initialized. Use .initialize() first.");
		}
        return this.bluemixRegionSuffix;
    }

	@NonNull
	public LoginWidget getLoginWidget(){
		if (null == this.loginWidget){
			throw new RuntimeException("AppID is not initialized. Use .initialize() first.");
		}
		return this.loginWidget;
	}

	@NonNull
	public OAuthManager getOAuthManager(){
		if (null == this.oAuthManager){
			throw new RuntimeException("AppID is not initialized. Use .initialize() first.");
		}
		return this.oAuthManager;
	}

	@NonNull
	public UserAttributeManager getUserAttributeManager(){
		if (null == this.userAttributeManager){
			throw new RuntimeException("AppID is not initialized. Use .initialize() first.");
		}
		return this.userAttributeManager;
	}

	public void loginAnonymously(@NotNull AuthorizationListener authorizationListener){
		this.loginAnonymously(authorizationListener, null);
	}

	public void loginAnonymously(@NotNull AuthorizationListener authorizationListener, String accessToken){
		oAuthManager.getAuthorizationManager().loginAnonymously(accessToken, authorizationListener);

	}

}
