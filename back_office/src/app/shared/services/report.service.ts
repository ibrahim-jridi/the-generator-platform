import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Report} from '../models/report.model';
import {Template} from '../models/template.model';
import {RequestType} from '../enums/requestType';
import {GlobalService} from './global.service';
import {HttpHeaders} from "@angular/common/http";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private BASE_TEMPLATES_URL = this.globalService.BASE_REPORTS_URL + this.globalService.API_V1_URL + 'report-templates';
  private BASE_REPORTS_URL = this.globalService.BASE_REPORTS_URL + this.globalService.API_V1_URL + 'reports';
  private BASE_REPORTS_MINIO_URL = this.globalService.BASE_REPORTS_URL + this.globalService.API_V1_URL + 'minio';
  constructor(private globalService: GlobalService) {}

  getTemplates(): Observable<Template[]> {
    return this.globalService.call(RequestType.GET, `${this.BASE_TEMPLATES_URL}`);
  }

  getTemplatesAsPromise(): Promise<Template[]> {
    return this.getTemplates().toPromise();
  }


  uploadJrxmlFile(template: Template, file: File): Observable<Template> {
    const formData: FormData = new FormData();
    formData.append('name', template.name);
    formData.append('file', file);
    formData.append('description', template.description);

    return this.globalService.call(RequestType.POST,`${this.BASE_TEMPLATES_URL}/upload-jrxml`,formData)
  }

  getReportsByTemplateId(templateId: string): Observable<Report[]> {
    return this.globalService.call(RequestType.GET,`${this.BASE_REPORTS_URL}/by-template/${templateId}`)

  }
  public uploadFile(file: File, taxRegistration: string): Observable<any> {
    const bucketName = "bs-bucket-" + taxRegistration ;
    const formData = new FormData();
    formData.append("file", file);
    return this.globalService.call(RequestType.POST,`${this.BASE_REPORTS_MINIO_URL}/upload-file?bucketName=${bucketName}`,formData,
        { responseType: 'text' as 'json' })
  }
  public getFile(fileName: string, bucketName: string): Observable<any> {
    const formData = new FormData();
    formData.append("fileName", fileName);
    formData.append("bucketName", bucketName);
    return this.globalService.call(RequestType.GET,`${this.BASE_REPORTS_MINIO_URL}/download-file?fileName=${fileName}&&bucketName=${bucketName}`,
        { responseType: 'blob', headers: new HttpHeaders() }).pipe(
        catchError((error) => {
          console.error('Error downloading file:', error);
          return new Observable<Blob>();
        })
    );
  }
  public getFileUrl(fileName: string, bucketName: string): Observable<string> {
    const formData = new FormData();
    formData.append("fileName", fileName);
    formData.append("bucketName", bucketName);
    return this.globalService.call(RequestType.GET,`${this.BASE_REPORTS_MINIO_URL}/get-file-url?fileName=${fileName}&&bucketName=${bucketName}`, { responseType: 'text' });
  }
}

