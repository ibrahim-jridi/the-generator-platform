import { Template } from "./template"

export class ReportTemplate {
    id:string;
    type:string
    path:string
    template:Template
    createdBy:string
    createdDate:Date
    lastModifiedBy:string
    lastModifiedDate:Date
    deleted:boolean
    file: File
}
