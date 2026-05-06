// import { Injectable } from '@angular/core';
// import { GlobalService } from './global.service';
// import { Observable} from 'rxjs';
// import { RequestType } from '../enums/requestType';
// import {HttpClient, HttpParams} from '@angular/common/http';
//
// @Injectable({
//   providedIn: 'root'
// })
// export class FileService {
//
//   public FILE_URI = this.globalService.BASE_REPORTS_URL + this.globalService.API_V1_URL + 'files';
//
//   constructor(private globalService: GlobalService, private http: HttpClient) {
//   }
//
//   public getImageReportUrl(name: string): Observable<Blob> {
//     let params = new HttpParams().append('name', name);
//     return this.globalService.call(
//       RequestType.GET,
//       `${this.FILE_URI}/logo-report-url`,
//       { params, responseType: 'blob' as 'json' }
//     );
//   }
//
//
//   public uploadLogoReport(file:File,name: string): Observable<any> {
//     const formData = new FormData();
//     formData.append('file', file);
//     formData.append('name', name);
//
//     return this.globalService.call(
//       RequestType.POST, `${this.FILE_URI}/upload-logo-report`, formData);
//   }
//
//   public uploadTemplateReport(file: File, type: string): Observable<string> {
//     const formData = new FormData();
//     formData.append('file', file);
//     formData.append('type', type);
//
//     return this.globalService.call(
//         RequestType.POST, `${this.FILE_URI}/upload-report`, formData, { responseType: 'text' });
//   }
//
//   public getTemplateReportUrl(name: string): Observable<any> {
//     let params = new HttpParams().append('name', name);
//     return this.globalService.call(
//         RequestType.GET,`${this.FILE_URI}/report-url`,{params}, { responseType: 'text' });
//   }
//
//   public updateTemplateReport(file: File, id: string, type: string): Observable<string> {
//     const formData = new FormData();
//     formData.append('file', file);
//     formData.append('id',id);
//     formData.append('type',type);
//
//     return this.globalService.call(
//         RequestType.POST, `${this.FILE_URI}/update-report`, formData, { responseType: 'text' });
//   }
//
//   public deleteReport(id: string): Observable<any> {
//     return this.globalService.call(
//         RequestType.DELETE, this.FILE_URI + '/' + id
//     )
//   }
//
// }
//
//

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GlobalService } from './global.service';
import { RequestType } from '../enums/requestType';

export interface FolderDto {
  id: string;
  folderMinioId: string;
  name: string;
  path: string;
  recurrence?: number;
  userId?: string;
  files?: FileDto[];
  parent?: FolderDto;
  folders?: FolderDto[];
  createdDate?: Date;
  createdBy?: string;
}

export interface FileDto {
  id: string;
  fileMinioId: string;
  name: string;
  path: string;
  extension: string;
  type: string;
  size: string;
  inWorkflow: boolean;
  folder?: FolderDto;
  createdDate?: Date;
  createdBy?: string;
}

export interface CreateFolderRequest {
  parentId: string;
  newFolderName: string;
}

@Injectable({
  providedIn: 'root'
})
export class FileManagementService {
  private FOLDER_API = this.globalService.BASE_File_URL + this.globalService.API_V1_URL + 'folder';
  private FILE_API = this.globalService.BASE_File_URL + this.globalService.API_V1_URL + 'minio/file';
  private FILE_URI = this.globalService.BASE_REPORTS_URL + this.globalService.API_V1_URL + 'minio/file';

  constructor(private globalService: GlobalService, private http: HttpClient) {}

  getUserFolder(): Observable<FolderDto> {
    return this.globalService.call(RequestType.GET, `${this.FOLDER_API}/user`);
  }

  getFoldersByParentId(parentId: string): Observable<FolderDto[]> {
    return this.globalService.call(RequestType.GET, `${this.FOLDER_API}/by-minio-id/${parentId}`);
  }

  getAllFolders(): Observable<FolderDto[]> {
    return this.globalService.call(RequestType.GET, `${this.FOLDER_API}`);
  }

  createFolder(parentId: string, folderName: string): Observable<FolderDto> {
    const request: CreateFolderRequest = {
      parentId: parentId,
      newFolderName: folderName
    };
    return this.globalService.call(RequestType.POST, `${this.FOLDER_API}/createChildFolder`, request);
  }

  deleteFolder(folderPath: string): Observable<string> {
    const params = new HttpParams().set('path', folderPath);
    return this.http.delete(`${this.FOLDER_API}/delete`, {
      params,
      responseType: 'text'
    });
  }

  uploadFile(file: File, path: string): Observable<FileDto> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('path', path);
    return this.globalService.call(RequestType.POST, `${this.FILE_API}/upload`, formData);
  }

  downloadFile(fileName: string): Observable<Blob> {
    return this.http.get(`${this.FILE_API}/download/${fileName}`, { responseType: 'blob' });
  }

  deleteFile(fileName: string): Observable<string> {
    return this.globalService.call(RequestType.DELETE, `${this.FILE_API}/delete/${fileName}`);
  }

  renameFile(oldFileName: string, newFileName: string): Observable<string> {
    const params = new HttpParams().set('oldFileName', oldFileName).set('newFileName', newFileName);
    return this.globalService.call(RequestType.POST, `${this.FILE_API}/rename`, {}, { params });
  }
  renameFolder(folderPath: string, newName: string): Observable<FolderDto> {
    const params = new HttpParams().set('path', folderPath).set('newName', newName);
    return this.http.post<FolderDto>(`${this.FOLDER_API}/rename`, null, { params });
  }
  getFileById(fileId: string): Observable<FileDto> {
    return this.globalService.call(RequestType.GET, `${this.FILE_API}/id/${fileId}`);
  }

  getAllFiles(): Observable<FileDto[]> {
    return this.globalService.call(RequestType.GET, `${this.FILE_API}`);
  }
  getFilesByFolderId(folderId: string): Observable<FileDto[]> {
    return this.http.get<FileDto[]>(`${this.FILE_API}/files/folder/${folderId}`);
  }

  getFileExtension(fileName: string): string {
    const lastDot = fileName.lastIndexOf('.');
    if (lastDot !== -1 && lastDot !== 0) {
      return fileName.substring(lastDot + 1).toLowerCase();
    }
    return '';
  }
  public getImageReportUrl(name: string): Observable<Blob> {
    let params = new HttpParams().append('name', name);
    return this.globalService.call(RequestType.GET, `${this.FILE_URI}/logo-report-url`, { params, responseType: 'blob' as 'json' });
  }

  public uploadLogoReport(file: File, name: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('name', name);

    return this.globalService.call(RequestType.POST, `${this.FILE_URI}/upload-logo-report`, formData);
  }

  public uploadTemplateReport(file: File, type: string): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);

    return this.globalService.call(RequestType.POST, `${this.FILE_URI}/upload-report`, formData, { responseType: 'text' });
  }

  public getTemplateReportUrl(name: string): Observable<any> {
    let params = new HttpParams().append('name', name);
    return this.globalService.call(RequestType.GET, `${this.FILE_URI}/report-url`, { params }, { responseType: 'text' });
  }

  public updateTemplateReport(file: File, id: string, type: string): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('id', id);
    formData.append('type', type);

    return this.globalService.call(RequestType.POST, `${this.FILE_URI}/update-report`, formData, { responseType: 'text' });
  }

  public deleteReport(id: string): Observable<any> {
    return this.globalService.call(RequestType.DELETE, this.FILE_URI + '/' + id);
  }
  getFolderContents(path: string): Observable<FolderDto> {
    return this.http.get<FolderDto>(`${this.FOLDER_API}/contents`, { params: { path } });
  }
  createFolderByPath(parentPath: string, folderName: string): Observable<FolderDto> {
    return this.http.post<FolderDto>(`${this.FOLDER_API}/create-by-path`, {
      parentPath,
      folderName
    });
  }
  getFolderContentsWithFiles(folderId: string): Observable<{ folders: FolderDto[]; files: FileDto[] }> {
    return this.globalService.call(RequestType.GET, `${this.FOLDER_API}/contents-with-files/${folderId}`);
  }
}