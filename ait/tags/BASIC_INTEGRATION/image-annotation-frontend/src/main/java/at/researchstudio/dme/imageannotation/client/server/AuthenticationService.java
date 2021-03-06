package at.researchstudio.dme.imageannotation.client.server;

import at.researchstudio.dme.imageannotation.client.server.exception.AuthenticationException;
import at.researchstudio.dme.imageannotation.client.user.User;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * interface to a prototypical authentication service.
 * 
 * background: the image annotation service should be capable of being integrated 
 * into portals. therefore, authentication and authorization should be handled by the 
 * corresponding portal. to pass user credentials to the image annotation service, which
 * may also be hosted by a different provider, a SSO solution plus using OpenIDs would fit
 * best. Within TELplus such a infrastructure is far out of scope. That is why we decided to
 * exchange a secure token that holds the user information. 
 * 
 * @author Christian Sadilek
 */
@RemoteServiceRelativePath("auth")
public interface AuthenticationService extends RemoteService {

	/**
	 * on invocation, external services or portals may pass an encrypted token and a
	 * signature to trustfully provide user credentials to the image annotation service. 
	 * the token is encrypted with the public key of our service and the signature
	 * with the private key of the caller. the keys of our service as well as all public 
	 * keys of valid external applications that may invoke our service are stored 
	 * in a key store configured in a property file.
	 * 
	 * the token is a JSON structure as follows:
	 * {"app": "TELplus", "user": "christian", "pwd" : "mypass", "admin" : "false", 
	 *  "roles" : ["r1","r2"], "permissions" : ["p1","p2"]}
	 * 
	 * the signature is also a JSON structure:
	 * {"app": "TELplus", "appSign": "username_timestamp"}
	 * 
	 * the token is decrypted, the signature validated and a user object is returned in case
	 * of success. otherwise an AuthenticationException will be thrown. 
	 * 
	 * @param authToken
	 * @return user
	 * @throws AuthenticationException
	 */
	public User authenticate(String authToken, String signature) throws AuthenticationException;
}
