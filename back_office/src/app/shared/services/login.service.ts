import {Injectable} from '@angular/core';
import {BehaviorSubject, interval, map, Observable, Subscription, switchMap} from 'rxjs';
import {TokenResponse} from '../models/token-response.model';
import {GlobalService} from './global.service';
import {Router} from '@angular/router';
import {RequestType} from '../enums/requestType';
import {TokenUtilsService} from './token-utils.service';
import {ResetPasswordRequest} from '../models/reset-password.model';
import {HttpParams} from '@angular/common/http';
import {tap} from "rxjs/operators";
import decode from "jwt-decode";
import {User} from "../models/user.model";

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private refreshInterval$!: Subscription;
  public isAuthenticated = new BehaviorSubject<boolean>(false);
  private refreshMargin = 50;
  private refreshTokenSub!: Subscription;

  constructor(private globalService: GlobalService, private tokenUtilsService: TokenUtilsService, private router: Router) {}

  private Base64 = btoa('client:secret');

  public login(userLogin): Observable<TokenResponse> {
    return this.globalService.call(RequestType.POST, this.globalService.url.base + 'api/v1/oauth/token', userLogin);
  }

  public rneLogin(loginRequest: Observable<TokenResponse>) {
    return this.globalService.call(RequestType.POST, this.globalService.url.base + 'api/v1/oauth/rne-token', loginRequest);
  }

  public signup(user): Observable<any> {
    return this.globalService.call(RequestType.POST, this.globalService.url.base + 'api/v1/oauth/register', user);
  }

  public preSignup(user): Observable<any> {
    return this.globalService.call(RequestType.POST, this.globalService.url.base + 'api/v1/oauth/pre-register', user);
  }

  public resendVerificationMail(token: any): any {
    const url = `${this.globalService.BASE_USER_MANAGEMENT_URL}${this.globalService.API_V1_URL}internal-users/resend-email-verification`;

    return this.globalService.call(RequestType.GET, url, {
      headers: { Authorization: `Bearer ${token}` } // Include the token in headers if required
    });
  }
  public resendVerificationMailJWTToken(token: any): any {
    const url = `${this.globalService.BASE_USER_MANAGEMENT_URL}${this.globalService.API_V1_URL}internal-users/resend-email-verification-jwt`;

    return this.globalService.call(RequestType.GET, url, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }
  public logout(): void {
    let isAdmin = this.tokenUtilsService.isAdministrator();
    this.clearUserData();
    this.tokenUtilsService.isAdmin = false;
    if (this.refreshInterval$) {
      this.refreshInterval$.unsubscribe();
    }
    this.isAuthenticated.next(false);
    if (isAdmin) {
      this.router.navigate(['/auth/login-admin']).then(() => {
        window.location.reload();
      });
    } else {
      this.router.navigate(['/auth']).then(() => {
        window.location.reload();
      });
    }
  }

  public clearUserData(): void {
    sessionStorage.clear();
    this.tokenUtilsService.clearToken();
  }

  public saveToken(tokenData: string): void {
    sessionStorage.setItem('token', tokenData);
  }

  public saveRefreshToken(refreshTokenData: string): void {
    sessionStorage.setItem('refresh_token', refreshTokenData);
  }

  public getRefreshToken(): string {
    return sessionStorage.getItem('refresh_token');
  }

  public activationEmail(token: any): Observable<Boolean> {
    const newUrl = this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + `open-api/verify-mail?token=${token}`;
    return this.globalService.call(RequestType.GET, newUrl);
  }

  public sendResetPassword(email: string, username: string): Observable<any> {
    return this.globalService.call(RequestType.POST, this.globalService.BASE_USER_MANAGEMENT_URL + 'api/v1/open-api/reset-password-request', {
      email,
      username
    });
  }

  public resetPassword(resetPasswordRequest: ResetPasswordRequest, token: any): Observable<any> {
    return this.globalService.call(
      RequestType.POST,
      this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'open-api/reset-user-password?token=' + token,
      resetPasswordRequest
    );
  }

  public firstConnectionResetPassword(resetPasswordRequest: ResetPasswordRequest): Observable<any> {
    return this.globalService.call(
      RequestType.POST,
      this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'user/reset-password-first-connection',
      resetPasswordRequest
    );
  }
  public verifyNationalId(nationalId: any): Observable<any> {
    const newUrl =
      this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + `open-api/nationalid-verif` + '?nationalId=' + nationalId;
    return this.globalService.call(RequestType.GET, newUrl);
  }

  public verifyUserBeforeSignUp(signUpRequest: any): Observable<any> {
    let params = new HttpParams()
      .append('address', signUpRequest.address)
      .append('birthDate', signUpRequest.birthDate)
      .append('firstName', signUpRequest.firstName)
      .append('lastName', signUpRequest.lastName)
      .append('country', signUpRequest.country)
      .append('nationality', signUpRequest.nationality)
      .append('nationalId', signUpRequest.nationalId)
      .append('phoneNumber', signUpRequest.phoneNumber)
      .append('email', signUpRequest.email)
      .append('gender', signUpRequest.gender);

    const newUrl = this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + `open-api/signup-verif`;
    return this.globalService.call(RequestType.GET, newUrl, { params: params });
  }

  public refreshToken(): Observable<string> {
    const refreshToken = this.getRefreshToken();
    return this.globalService.call(RequestType.POST, this.globalService.url.base + 'api/v1/oauth/refresh', { refreshToken }).pipe(
      tap((response) => {
        this.saveToken(response.accessToken);
      }),
      map((response) => response.accessToken)
    );
  }

  startTokenRefresh(token: string): void {
    const decodedToken: any = decode(token);
    const tokenExp = decodedToken.exp;
    const currentTime = Math.floor(Date.now() / 1000);
    const timeUntilExpiration = tokenExp - currentTime;

    if (timeUntilExpiration > this.refreshMargin) {
      const refreshInterval = (timeUntilExpiration - this.refreshMargin) * 1000;

      this.refreshTokenSub = interval(refreshInterval)
        .pipe(switchMap(() => this.refreshToken()))
        .subscribe({
          next: (newToken) => {
            this.saveToken(newToken);
          },
          error: (err) => {
            this.logout();
          }
        });
    }
  }
  public getAllByNationalId(nationalId: string): Observable<User[]> {
    return this.globalService.call(
      RequestType.GET,
      this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'open-api/by-national-id' + '?nationalId=' + nationalId
    );
  }
  public checkInternalUsername(nationalId: string): Observable<boolean> {
    const request = { nationalId: nationalId };

    return this.globalService.call(
      RequestType.POST,
      this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'open-api/check-username-prefix',
      request
    );
  }
  public checkUsernameMatch(nationalId: string, username: string): Observable<boolean> {
    const request = { nationalId, username };

    return this.globalService.call(
      RequestType.POST,
      this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'open-api/check-username-match',
      request
    );
  }
  public checkExternalUsername(nationalId: string): Observable<boolean> {
    const request = { nationalId: nationalId };

    return this.globalService.call(
      RequestType.POST,
      this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'open-api/check-username-ext-prefix',
      request
    );
  }

  public checkCinExists(nationalId: string): Observable<boolean> {
    const url = `${this.globalService.BASE_USER_MANAGEMENT_URL}${this.globalService.API_V1_URL}open-api/check-cin?nationalId=${nationalId}`;
    return this.globalService.call(RequestType.GET, url);
  }

  public checkEmailExists(email: string): Observable<boolean> {
    const url = `${this.globalService.BASE_USER_MANAGEMENT_URL}${this.globalService.API_V1_URL}open-api/check-email?email=${email}`;
    return this.globalService.call(RequestType.GET, url);
  }
}
