import {ContentType} from "../enums/content-type.enum";
import {RequestMethod} from "../enums/request-method.enum";
import {AuthenticationMethod} from "../enums/authentication-method.enum";

export class Connector {
  id: string;
  url: string;
  apiName: string;
  description: string;
  token: string;
  apiLabel: string;
  contentType: ContentType;
  requestMethod: RequestMethod;
  authenticationMethod: AuthenticationMethod;
  headers: string;
  connectTimeout: number;
  readTimeout: number;
  queryParameters: string;
  requestBody: string;
  responseBody: string;
  retryCount: string;
  retryInterval: number;
}
