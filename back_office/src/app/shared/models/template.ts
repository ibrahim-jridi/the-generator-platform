import { ReportTemplate } from "./report-template"

export class Template {
    id:string;
    objet:string
    description:string
    fileName:string
    content:any
    fileContentContentType:string
    reportTemplate:ReportTemplate
    createdBy:string
    createdDate:Date
    lastModifiedBy:string
    lastModifiedDate:Date
    deleted:boolean
}
