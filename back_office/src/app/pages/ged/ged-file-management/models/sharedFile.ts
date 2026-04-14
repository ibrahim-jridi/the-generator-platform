import {PrivilegeOnFile} from "./privilegeOnFile.enum";

export class SharedFile {
  id: number;
  shareDate: Date;
  idUser: number;
  privilege: PrivilegeOnFile;
  sharePath : String;
  sharePathUuid:String;
}
