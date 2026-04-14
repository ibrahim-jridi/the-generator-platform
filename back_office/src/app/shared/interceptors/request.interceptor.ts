import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {tap} from 'rxjs/operators';

/** Pass untouched request through to the next request handler. */
@Injectable()
export class RequestInterceptor implements HttpInterceptor {
  constructor(
  ) {
  }

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const isLoginRequest = request.url.includes('/oauth') || request.url.includes('/send-email') || request.url.includes('/resend-email');
    if (!request.url.includes('rss2json') && !request.url.includes('assets') && !isLoginRequest) {
      const token = sessionStorage.getItem('token');
      if ( token != null) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`
          }
        });
      }
    }
    return next.handle(request).pipe(
      tap(
        event => {
        },
        error => {
          return throwError(error);
        }
      )
    );
  }
}
