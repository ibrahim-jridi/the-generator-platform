import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {LocalStorageService} from 'ngx-webstorage';
import {Observable} from 'rxjs';
import {catchError} from 'rxjs/operators';

@Injectable()
export class ResponseInterceptor implements HttpInterceptor {
  constructor(
      private localSt: LocalStorageService,
      private router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.status === 401) {
            this.localSt.store('expired', true);
            this.router.navigateByUrl('/auth');
            return new Observable<never>();
          } else if (error.status === 403) {
            //pas d'autorisation
            return new Observable<never>();
          } else {
            return new Observable<never>();
          }
        })
    );
  }
}
