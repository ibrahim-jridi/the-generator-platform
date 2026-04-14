export enum RequestMethod {
  GET = 'GET',
  POST = 'POST',
  PUT = 'PUT',
  PATCH = 'PATCH',
  DELETE = 'DELETE',
}

export const requestMethods = Object.keys(RequestMethod).map((key) => ({
  id: RequestMethod[key as keyof typeof RequestMethod],
  name: key,
}));
