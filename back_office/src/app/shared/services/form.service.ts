import {Injectable} from '@angular/core';
import {firstValueFrom, Observable} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {GlobalService} from './global.service';
import {RequestType} from '../enums/requestType';
import {Form} from '../models/form.model';
import {User} from '../models/user.model';
import {PaginationArgs} from '../models/paginationArgs.model';
import {FormResponseModel} from '../models/form-response-model';
import {FilterValuesModel} from '../models/filter-values-model';
import {DraftDTO} from "../models/DraftDTO.model";

@Injectable({
  providedIn: 'root'
})
export class FormService {
  constructor(private http: HttpClient, private globalService: GlobalService) {}

  private BASE_FILE_URL = this.globalService.BASE_File_URL + this.globalService.API_V1_URL + 'minio';
  private BASE_FORM_URL = this.globalService.BASE_FORMS_URL + this.globalService.API_V1_URL + 'forms';
  private BASE_SUBMISSION_URL = this.globalService.BASE_FORMS_URL + this.globalService.API_V1_URL + 'submissions';
  private BASE_DRAFT_URL = this.globalService.BASE_FORMS_URL + this.globalService.API_V1_URL + 'drafts';

  public getForms(criteria: FilterValuesModel[], paginationArgs: PaginationArgs): Observable<FormResponseModel> {
    let params = new HttpParams().append('sort', paginationArgs.sort).append('page', paginationArgs.page).append('size', paginationArgs.size);

    if (criteria != undefined && criteria.length > 0) {
      criteria.forEach((element) => {
        if (this.isDate(element.filterValue)) {
          var date = new Date(element.filterValue);
          params = params.append(`${element.columnName}.greaterThanOrEqual`, date.toISOString());
          params = params.append(`${element.columnName}.lessThan`, new Date(date.setDate(date.getDate() + 1)).toISOString());
        } else {
          params = params.append(`${element.columnName}.contains`, element.filterValue);
        }
      });
    }

    const options = {
      params: params,
      observe: 'response'
    };

    return this.globalService.call(RequestType.GET, this.BASE_FORM_URL, options);
  }

  getAllForms(): Promise<Form[]> {
    return firstValueFrom(this.globalService.call(RequestType.GET, this.BASE_FORM_URL + '/all'));
  }

  createForm(form: Object): Observable<Object> {
    return this.globalService.call(RequestType.POST, this.BASE_FORM_URL, form, {
      headers: new HttpHeaders().set('Content-Type', 'application/json')
    });
  }

  duplicateForm(id: string): Observable<Object> {
    return this.globalService.call(
      RequestType.POST,
      this.BASE_FORM_URL + '/duplicate',
      { id },
      {
        headers: new HttpHeaders().set('Content-Type', 'application/json')
      }
    );
  }

  updateForm(id: string, form: Form): Observable<Form> {
    return this.globalService.call(RequestType.PUT, `${this.BASE_FORM_URL}/${id}`, form);
  }
  getFormById(id: string): Observable<any> {
    return this.http.get<any>(`${this.BASE_FORM_URL}/${id}`);
  }

  uploadFile(file: File, path: string): Observable<any> {
    const token = sessionStorage.getItem('token');
    const formData = new FormData();
    formData.append('file', file);
    formData.append('path', path);
    return this.globalService.call(RequestType.POST, `${this.BASE_FILE_URL}/file/upload`, formData, {
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  deleteForm(id: string): Observable<User> {
    return this.globalService.call(RequestType.DELETE, this.BASE_FORM_URL + '/' + id);
  }

  saveDraftData(draftDataDto: DraftDTO): Observable<any> {
    const url = `${this.BASE_DRAFT_URL}/`;
    return this.globalService.call(
        RequestType.POST,
        url,
        draftDataDto,
        {
          headers: new HttpHeaders().set('Content-Type', 'application/json'),
        }
    );
  }
  getSubmissionsByTaskInstanceId(taskInstanceId: string): Observable<any[]> {
    const url = `${this.BASE_SUBMISSION_URL}/by-task-instance/${taskInstanceId}`;
    return this.globalService.call(RequestType.GET, url);
  }

  private isDate(lineValue: any): boolean {
    if (lineValue instanceof Date) {
      return !isNaN(lineValue.getTime());
    } else if (typeof lineValue === 'string') {
      const regex = /^\d{4}-\d{2}-\d{2}$/;

      // Check if the string matches the date format
      if (regex.test(lineValue)) {
        const date = new Date(lineValue);
        return !isNaN(date.getTime());
      }
    }
    return false;
  }

  getDraftById(id: string): Observable<any> {
    const url = `${this.BASE_DRAFT_URL}/${id}`;
    return this.globalService.call(RequestType.GET, url);
  }
}
