import { User } from "./user.model";

export class ExternalUser extends User {
    identifier : string;
    nif : string;
    rccm : string;
    companyName: string
    companyAddress : string;
    companyPhone : string;
    companyEmail : string;
    createdDate: Date;
}
