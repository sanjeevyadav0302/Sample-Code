package factory

import models.AuthenticateServiceResponse

trait AuthenticationServiceFactory {

  val identityServiceUrl:String
  val identityClientId :String
  val state:String
  val returnUri:String
  val responseCode:String
  val scope:String
  val authenticateServiceResponse : AuthenticateServiceResponse
}


trait AuthenticationOperation{

  def create(socialSiteId:String) :Option[AuthenticateServiceResponse]
}