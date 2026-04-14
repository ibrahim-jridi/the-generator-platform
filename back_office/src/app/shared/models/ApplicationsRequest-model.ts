export class ApplicationsRequest{
  names: string[];
  constructor(names: string[] = []) {
    this.names = names;
  }
}
