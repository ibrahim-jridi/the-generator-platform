import {RequestMethod} from "./request-method.enum";

export enum AuthenticationMethod {
  BASIC_AUTH = "BASIC_AUTH",
  API_KEY_AUTH = "API_KEY_AUTH",
  TOKEN_BASED_AUTH = "TOKEN_BASED_AUTH",
}

export const authenticationMethods = Object.keys(AuthenticationMethod).map((key) => ({
  id: AuthenticationMethod[key as keyof typeof AuthenticationMethod],
  name: key,
}));
