/**
 * <pre>
 * 	This program is free software; you can redistribute it and/or modify it under the terms of 
 * the GNU AFFERO GENERAL PUBLIC LICENSE as published by the Free Software Foundation; either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * 	This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU AFFERO GENERAL PUBLIC LICENSE for more details. 
 * 	You should have received a copy of the GNU AFFERO GENERAL PUBLIC LICENSE along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * </pre>
 */
package com.meidusa.amoeba.net;

import java.io.IOException;

/**
 * ֧��Connection �����֤���̵� ConnectionManager
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 */
public class AuthingableConnectionManager extends ConnectionManager {
    protected Authenticator _author;
    
    public AuthingableConnectionManager() throws IOException{
    }

    public AuthingableConnectionManager(String managerName) throws IOException{
        super(managerName);
    }

    public void setAuthenticator(Authenticator author) {
        _author = author;
    }

    public Authenticator getAuthenticator() {
        return _author;
    }

}
