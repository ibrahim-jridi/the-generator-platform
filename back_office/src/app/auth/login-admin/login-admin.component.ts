import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {LoginService} from '../../shared/services/login.service';
import {AppToastNotificationService} from '../../shared/services/appToastNotification.service';
import {TranslatePipe} from '@ngx-translate/core';
import {TokenUtilsService} from '../../shared/services/token-utils.service';
import {CamundaService} from '../../shared/services/camunda.service';

@Component({
  selector: 'app-login',
  templateUrl: './login-admin.component.html',
  styleUrls: ['./login-admin.component.scss'] // Corrected typo here
})
export class LoginAdminComponent implements OnInit {
  public signinForm: FormGroup;
  public fieldTextType: boolean = false;
  private processDefKey: string = 'COMPLETE_PROFILE_PP';
  public language: string;
  public currentLanguage: string;

  constructor(
      private router: Router,
      private loginService: LoginService,
      private toastrService: AppToastNotificationService,
      private translatePipe: TranslatePipe,
      private tokenUtilsService: TokenUtilsService,
      private camundaService: CamundaService
  ) {
  }

  ngOnInit(): void {
    this.currentLanguage = localStorage.getItem('language') || 'fr';
    this.language = this.currentLanguage.charAt(0).toUpperCase() + this.currentLanguage.slice(1).toLowerCase();
    this.signinForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      rememberMe: new FormControl(true)
    });
  }

  public onSubmit(): void {
    if (this.signinForm.valid) {
      this.loginService.login(this.signinForm.value).subscribe({
        next: (data: any) => {
          sessionStorage.setItem('profileCompleted', data.profileCompleted);
          if (data.access_token && data.access_token) {
            this.loginService.saveToken(data.access_token);
            this.loginService.saveRefreshToken(data.refresh_token);
            this.loginService.isAuthenticated.next(true);
            this.loginService.startTokenRefresh(data.access_token);

            if (!this.tokenUtilsService.isAdministrator()) {
              this.toastrService.onError(this.translatePipe.transform('users.errors.LOGIN_FAILED'), this.translatePipe.transform('menu.ERROR'));
              this.loginService.clearUserData();
              return;
            }
            if (
                this.tokenUtilsService.isAuthenticated() &&
                !this.tokenUtilsService.isPM() &&
                !data.profileCompleted
            ) {
              const vars = {
                starter: data.userId,
                Last_name: data.lastName,
                First_name: data.firstName,
                Gender: data.gender,
                Date_of_birth: data.birthDate,
                Phone_number: data.phoneNumber,
                Age: data.age,
                national_id_number: data.cin,
                email_address: data.email,
                e_barid: data.e_barid,
                Address: data.address
              };
            }
            this.toastrService.onSuccess(this.translatePipe.transform('users.LOGIN_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
            this.redirectUser();
          }
        },
        error: () => {
          this.toastrService.onError(this.translatePipe.transform('users.errors.LOGIN_FAILED'), this.translatePipe.transform('menu.ERROR'));
          this.loginService.clearUserData();
        },
        complete: () => {
          this.checkFirstConnection();
        }
      });
    }
  }

  private redirectUser(): void {
    if (!this.tokenUtilsService.isAuthenticated()) {
      this.loginService.clearUserData();
      this.router.navigate(['/auth/login-admin']);
    } else if (this.tokenUtilsService.isAuthenticated() && !this.tokenUtilsService.isEmailVerified()) {
      this.loginService.clearUserData();
      this.router.navigate(['/auth/verify-mail']);
    } else {
      this.router.navigate(['/pages/dashboard']);
    }
  }

  public toggleFieldTextType(): void {
    this.fieldTextType = !this.fieldTextType;
  }

  protected onLanguageChanged(newLanguage: string): void {
    this.language = newLanguage.charAt(0).toUpperCase() + newLanguage.slice(1).toLowerCase();
  }

  private checkFirstConnection(): void {
    if (this.tokenUtilsService.getFirstConnection()) {
      this.router.navigate(['/auth/forgot-password'])
    }
  }

  public goBack(): void {
    this.router.navigate(['/auth']);
  }

}
