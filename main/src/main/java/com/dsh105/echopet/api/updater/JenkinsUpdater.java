/*
 * This file is part of EchoPet.
 *
 * EchoPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EchoPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EchoPet. If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.echopet.api.updater;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import com.dsh105.echopet.compat.api.plugin.EchoPet;
import com.dsh105.echopet.compat.api.util.IUpdater;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.Plugin;

public class JenkinsUpdater implements IUpdater{
	
	private static final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.of(30, ChronoUnit.SECONDS)).followRedirects(HttpClient.Redirect.ALWAYS).build();
	private static final Gson gson = new GsonBuilder().create();
	
	private static final String BASE_URL = "https://jenkins.arnah.ca/job/%s/api/json";
	private static final String STABLE = "EchoPet", DEV = "EchoPet-Dev";
	
	private final Plugin plugin;
	
	private final boolean beta;
	private final int buildNumber;
	
	private JenkinsBuild newestBuild;
	
	public JenkinsUpdater(Plugin plugin){
		this.plugin = plugin;
		String version = plugin.getDescription().getVersion();
		beta = version.contains("-SNAPSHOT");
		
		String build = version.substring(version.lastIndexOf("b") + 1);
		if(build.isBlank()){
			buildNumber = -1;
		}else{
			buildNumber = Integer.parseInt(build);
		}
		if(buildNumber == -1){
			plugin.getLogger().warning("Could not parse build number from version: %s, assuming local build.".formatted(version));
		}else{
			if(EchoPet.getConfig(EchoPet.ConfigType.MAIN).getBoolean("checkForUpdates", true)){
				plugin.getServer().getScheduler().runTaskAsynchronously(plugin, this::checkForUpdates);
			}
		}
	}
	
	private void checkForUpdates(){
		try{
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL.formatted(beta ? DEV : STABLE))).GET().build();
			
			HttpResponse<String> result = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			JenkinsJob job = gson.fromJson(result.body(), JenkinsJob.class);
			JenkinsBuild build = job.getLastSuccessfulBuild();
			if(build != null && build.getNumber() > buildNumber){
				newestBuild = build;
				plugin.getLogger().info("An update is available for EchoPet! (Build #%d)".formatted(build.getNumber()));
				plugin.getLogger().info(build.getUrl());
			}else{
				plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::checkForUpdates, TimeUnit.DAYS.toMillis(1));
			}
		}catch(Exception ignored){
			// Assume jenkins is down, who cares.
		}
	}
	
	@Override
	public boolean isUpdateFound(){
		return newestBuild != null;
	}
	
	@Override
	public String getManualUpdateURL(){
		return newestBuild.getUrl();
	}
}
