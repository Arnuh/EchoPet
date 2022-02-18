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

package com.dsh105.echopet.compat.api.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bukkit.plugin.java.JavaPlugin;

public class YAMLConfigManager{
	
	private JavaPlugin plugin;
	
	public YAMLConfigManager(JavaPlugin plugin){
		this.plugin = plugin;
	}
	
	public YAMLConfig getNewConfig(String filePath, String[] header){
		File file = this.getConfigFile(filePath);
		
		boolean fileExists = file.exists();
		if(!fileExists){
			this.prepareFile(filePath);
			
			if(header != null && header.length != 0){
				this.setHeader(file, header);
			}
		}
		
		return new YAMLConfig(getConfigContent(filePath), file, this.getCommentsNum(file), plugin);
	}
	
	public YAMLConfig getNewConfig(String filePath){
		return this.getNewConfig(filePath, null);
	}
	
	private File getConfigFile(String file){
		if(file.isEmpty() || file == null){
			return null;
		}
		
		File configFile;
		if(file.contains("/")){
			if(file.startsWith("/")){
				configFile = new File(plugin.getDataFolder() + file.replace("/", File.separator));
			}else{
				configFile = new File(plugin.getDataFolder() + File.separator + file.replace("/", File.separator));
			}
		}else{
			configFile = new File(plugin.getDataFolder(), file);
		}
		
		return configFile;
		
	}
	
	public void prepareFile(String filePath, String resource){
		File file = this.getConfigFile(filePath);
		if(file.exists()){
			return;
		}
		
		try{
			file.getParentFile().mkdirs();
			file.createNewFile();
			
			if(resource != null && !resource.isEmpty()){
				try(InputStream stream = plugin.getResource(resource)){
					copyResource(stream, file);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void prepareFile(String filePath){
		this.prepareFile(filePath, null);
	}
	
	public void setHeader(File file, String[] header){
		if(!file.exists()){
			return;
		}
		
		try{
			String currentLine;
			StringBuilder config = new StringBuilder("");
			try(BufferedReader reader = new BufferedReader(new FileReader(file))){
				while((currentLine = reader.readLine()) != null){
					config.append(currentLine)
						.append("\n");
				}
			}
			config.append("# +----------------------------------------------------+ #\n");
			
			for(String line : header){
				
				if(line.length() > 50){
					continue;
				}
				
				int length = (50 - line.length()) / 2;
				StringBuilder finalLine = new StringBuilder(line);
				
				for(int i = 0; i < length; i++){
					finalLine.append(" ");
					finalLine.reverse();
					finalLine.append(" ");
					finalLine.reverse();
				}
				
				if(line.length() % 2 != 0){
					finalLine.append(" ");
				}
				
				config.append("# < ")
					.append(finalLine.toString())
					.append(" > #\n");
				
			}
			
			config.append("# +----------------------------------------------------+ #");
			
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
				writer.write(prepareConfigString(config.toString()));
				writer.flush();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public String getConfigContent(File file){
		if(!file.exists()){
			return null;
		}
		
		try{
			int commentNum = 0;
			
			String pluginName = this.getPluginName();
			
			StringBuilder whole = new StringBuilder();
			try(BufferedReader reader = new BufferedReader(new FileReader(file))){
				String currentLine;
				while((currentLine = reader.readLine()) != null){
					if(currentLine.startsWith("#")){
						String commentKey = pluginName + "_COMMENT_" + commentNum++ + ":";
						whole.append(currentLine.replaceFirst("#", commentKey))
							.append("\n");
					}else{
						whole.append(currentLine)
							.append("\n");
					}
				}
			}
			return whole.toString();
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	
	private int getCommentsNum(File file){
		if(!file.exists()){
			return 0;
		}
		
		try(BufferedReader reader = new BufferedReader(new FileReader(file))){
			int comments = 0;
			String currentLine;
			
			while((currentLine = reader.readLine()) != null){
				if(currentLine.startsWith("#")){
					comments++;
				}
			}
			return comments;
		}catch(IOException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public String getConfigContent(String filePath){
		return getConfigContent(getConfigFile(filePath));
	}
	
	private String prepareConfigString(String configString){
		int lastLine = 0;
		int headerLine = 0;
		
		String commentKey = getPluginName() + "_COMMENT";
		
		String[] lines = configString.split("\n");
		StringBuilder config = new StringBuilder();
		
		for(String line : lines){
			if(line.startsWith(commentKey)){
				String comment = "#" + line.trim().substring(line.indexOf(":") + 1);
				
				if(comment.startsWith("# +-")){
					/*
					 * If header line = 0 then it is header start, if it's equal
					 * to 1 it's the end of header
					 */
					if(headerLine == 0){
						config.append(comment)
							.append("\n");
						
						lastLine = 0;
						headerLine = 1;
					}else{
						config.append(comment)
							.append("\n\n");
						lastLine = 0;
						headerLine = 0;
					}
				}else{
					/*
					 * Last line = 0 - Comment Last line = 1 - Normal path
					 */
					String normalComment;
					
					if(comment.startsWith("# ' ")){
						normalComment = comment.substring(0, comment.length() - 1).replaceFirst("# ' ", "# ");
					}else{
						normalComment = comment;
					}
					
					if(lastLine == 0){
						config.append(normalComment)
							.append("\n");
					}else{
						config.append("\n")
							.append(normalComment)
							.append("\n");
					}
					lastLine = 0;
				}
			}else{
				config.append(line)
					.append("\n");
				lastLine = 1; // Notes that we need to add an extra newline if next line is adding comments.
			}
		}
		return config.toString();
	}
	
	public void saveConfig(String configString, File file){
		String configuration = this.prepareConfigString(configString);
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
			writer.write(configuration);
			writer.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public String getPluginName(){
		return plugin.getDescription().getName();
	}
	
	private void copyResource(InputStream resource, File file){
		try(OutputStream out = new FileOutputStream(file)){
			int lenght;
			byte[] buf = new byte[1024];
			
			while((lenght = resource.read(buf)) > 0){
				out.write(buf, 0, lenght);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}