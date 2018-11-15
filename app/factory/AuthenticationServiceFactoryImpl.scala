package factory

import java.util.UUID

import com.typesafe.config.ConfigFactory
import models.AuthenticateServiceResponse


object AuthenticationGoogleServiceFactoryImpl extends AuthenticationServiceFactory{
  val configuration = ConfigFactory.load()
  override val identityServiceUrl: String = configuration.getString("google.service.url")
  override val identityClientId: String = configuration.getString("google.client.id")
  override val state: String = UUID.randomUUID().toString
  override val returnUri: String = configuration.getString("google.redirect.url")
  override val responseCode: String = "code"
  override val scope: String = configuration.getString("google.scope")
  override def toString = identityServiceUrl+identityClientId+"&state="+state+"&redirect_uri="+returnUri+"&response_type="+responseCode+"&scope="+scope
  override val authenticateServiceResponse = AuthenticateServiceResponse(toString,state)

}


object AuthenticationFacebookServiceFactoryImpl extends AuthenticationServiceFactory{

  val configuration = ConfigFactory.load()
  override val identityServiceUrl: String = configuration.getString("facebook.service.url")
  override val identityClientId: String = configuration.getString("facebook.client.id")
  override val state: String = UUID.randomUUID().toString
  override val returnUri: String = configuration.getString("facebook.redirect.url")
  override val responseCode: String = "code"
  override val scope: String = configuration.getString("facebook.scope")
  override def toString = identityServiceUrl+identityClientId+"&state="+state+"&redirect_uri="+returnUri+"&response_type="+responseCode+"&scope="+scope
  override val authenticateServiceResponse = AuthenticateServiceResponse(toString,state)
}

object AuthenticationLinkedInServiceFactoryImpl extends AuthenticationServiceFactory{

  val configuration = ConfigFactory.load()
  override val identityServiceUrl: String = configuration.getString("linkedIn.service.url")
  override val identityClientId: String = configuration.getString("linkedIn.client.id")
  override val state: String = UUID.randomUUID().toString
  override val returnUri: String = configuration.getString("linkedIn.redirect.url")
  override val responseCode: String = "code"
  override val scope: String = configuration.getString("linkedIn.scope")
  override def toString = identityServiceUrl+identityClientId+"&state="+state+"&redirect_uri="+returnUri+"&response_type="+responseCode+"&scope="+scope
  override val authenticateServiceResponse = AuthenticateServiceResponse(toString,state)
}



object AuthenticationFactor extends AuthenticationOperation {
  private val google = AuthenticationGoogleServiceFactoryImpl;
  private val linkedIn = AuthenticationLinkedInServiceFactoryImpl;
  private val facebook = AuthenticationFacebookServiceFactoryImpl;

  override def create(socialSiteId: String): Option[AuthenticateServiceResponse] = socialSiteId match {
    case socialSiteId if(socialSiteId.equals("google")) => Some(google.authenticateServiceResponse)
    case socialSiteId if(socialSiteId.equals("linkedIn")) => Some(linkedIn.authenticateServiceResponse)
    case socialSiteId if(socialSiteId.equals("facebook")) => Some(facebook.authenticateServiceResponse)
    case _ => None
  }

  def getAllLoginLinks(): List[String] = {
    List(create("google").get.url,create("linkedIn").get.url)
  }

}