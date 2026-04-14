import { SessionStatus } from "../enums/session-status.enum"

export class Session {
    id:string;
    label:string
    openingAmount:number
    closingAmount:number
    startDate:Date
    endDate:Date
    sessionStatus:SessionStatus
    userId: string;
    createdBy:string
    createdDate:Date
    lastModifiedBy:string
    lastModifiedDate:Date
    deleted:boolean
    caisseGateId:string;
    caisseStationId:string;
    formatstartDate: string
    formatendDate: string
    gateId : string;
    replace : boolean
}
