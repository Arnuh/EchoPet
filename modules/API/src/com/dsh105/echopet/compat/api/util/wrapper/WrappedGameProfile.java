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

package com.dsh105.echopet.compat.api.util.wrapper;

import java.util.UUID;

@Deprecated
public class WrappedGameProfile extends AbstractWrapper{
	
	private WrappedGameProfile(Object ident, String name){
		// Might remake, need to determine if useless first.
		throw new UnsupportedOperationException();
		/*
		Class<?> gameProfileClass = null;
		try{
			gameProfileClass = Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
		}catch(ClassNotFoundException e){
			try{
				gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");
			}catch(ClassNotFoundException e1){
				e1.printStackTrace();
			}
		}
		
		try{
			if(ident instanceof UUID){
				super.setHandle(gameProfileClass.getConstructor(ident.getClass(), String.class).newInstance(ident, name));
			}else if(ident instanceof String){
				super.setHandle(gameProfileClass.getConstructor(String.class, String.class).newInstance(ident, name));
			}
		}catch(InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
			e.printStackTrace();
		}*/
	}
	
	public WrappedGameProfile(UUID uuid, String name){
		this((Object) uuid, name);
	}
	
	public WrappedGameProfile(String ident, String name){
		this((Object) ident, name);
	}
	
	public static WrappedGameProfile getNewProfile(WrappedGameProfile old, String newName){
		return new WrappedGameProfile(old.getUniqueId(), newName);
	}
	
	public UUID getUniqueId(){
		return getId();
	}
	
	public String getIdent(){
		return getId();
	}
	
	private <T> T getId(){
		throw new UnsupportedOperationException();
		// return ReflectionUtil.invokeMethod(ReflectionUtil.getMethod(getHandle().getClass(), ReflectionConstants.GAMEPROFILE_FUNC_ID.getName()), getHandle());
	}
}
