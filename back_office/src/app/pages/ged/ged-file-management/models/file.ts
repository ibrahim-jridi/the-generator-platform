
export interface FileElement {
  id?: string;
  isFolder: boolean;
  name: string;
  parent: string;
  fullPath?: string;
  privilege : string;
  parentPath: string;
  inWorkflow? : boolean;
  path:string ;
  type: string ;
  uuid: string;
  pathUuid: string;
  idParent : number;
  children?: FileElement[]; // Add this line
}

export interface MinioItem {
  Key?: string;
  Size?: number;
}

export enum Level {
  ONE, TWO, THREE,
}
