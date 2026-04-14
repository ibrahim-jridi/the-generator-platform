import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../../shared/services/login.service';
import { AppToastNotificationService } from '../../shared/services/appToastNotification.service';
import { TranslatePipe } from '@ngx-translate/core';
import { TokenUtilsService } from '../../shared/services/token-utils.service';
import { CamundaService } from '../../shared/services/camunda.service';
import { NgxCaptchaComponent, NgxCaptchaService } from '@binssoft/ngx-captcha';
import { ThemeService } from '../../shared/services/theme.service';
import { LoaderService } from '../../shared/services/loader.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  public signinForm: FormGroup;
  public fieldTextType: boolean = false;
  private processDefKey: string = 'COMPLETE_PROFILE_PP';
  public language: string;
  public currentLanguage: string;
  public captchaStatus: boolean;
  public captchaResponse: string;
  public captchavalue: boolean;
  public blueColor = null;
  public darkGrayColor = null;
  public lightGrayColor = null;
  public captchaConfig: any;
  code: any = null;
  resultCode: any = null;
  config: any = {};
  @ViewChild(NgxCaptchaComponent, { static: false }) captchaComponent: NgxCaptchaComponent;

  constructor(
    private router: Router,
    private loginService: LoginService,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private tokenUtilsService: TokenUtilsService,
    private camundaService: CamundaService,
    private loaderService: LoaderService,
    private captchaService: NgxCaptchaService,
    private themeService: ThemeService
  ) {}

  ngOnInit(): void {
    this.currentLanguage = localStorage.getItem('language') || 'fr';
    this.language = this.currentLanguage.charAt(0).toUpperCase() + this.currentLanguage.slice(1).toLowerCase();
    this.signinForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      rememberMe: new FormControl(true),
      recaptcha: new FormControl('')
    });
    this.initCaptcha();
  }

  initCaptcha(): void {
    this.blueColor = this.themeService.getCSSVariable('--theme-vibrant-blue');
    this.darkGrayColor = this.themeService.getCSSVariable('--color-dark-gray');
    this.lightGrayColor = this.themeService.getCSSVariable('--light-gray-color');
    this.captchaConfig = {
      type: 1,
      length: 6,
      cssClass: 'custom-captcha',
      back: {
        stroke: this.blueColor,
        solid: this.lightGrayColor
      },
      font: {
        color: this.darkGrayColor,
        size: '35px'
      }
    };
  }

  getCaptchaResponse() {
    if (this.captchaComponent) {
      this.captchaResponse = this.captchaComponent.captch_input;
    }
  }

  checkCaptcha(): Promise<boolean> {
    return new Promise<boolean>((resolve, reject) => {
      const captchaValue = this.captchaComponent.checkCaptcha();
      this.getCaptchaResponse();
      this.captchaService.captchStatus.subscribe((status) => {
        this.captchaStatus = status;
        if (this.captchaStatus === true) {
          resolve(true);
        } else if (this.captchaStatus === false) {
          resolve(false);
        }
      });
    });
  }

  checkCaptchaInputValue() {
    this.getCaptchaResponse();
    return this.captchaResponse === '';
  }

  checkCaptchaValue(): void {
    this.checkCaptcha()
      .then((result) => {
        this.captchavalue = result;
        this.submitForm();
      })
      .catch((error) => {
        this.toastrService.onError(this.translatePipe.transform('users.errors.INVALID_CAPTCHA'), this.translatePipe.transform('menu.ERROR'));
      });
  }

  submitForm(): void {
    if (this.captchavalue == false) {
      this.toastrService.onError(this.translatePipe.transform('users.errors.INVALID_CAPTCHA'), this.translatePipe.transform('menu.ERROR'));
      return;
    }
    if (this.signinForm.valid) {
      this.loaderService.show();
      this.loginService.login(this.signinForm.value).subscribe({
        next: (data: any) => {
          sessionStorage.setItem('profileCompleted', data.profileCompleted);
          if (data.access_token && data.access_token) {
            this.loginService.saveToken(data.access_token);
            this.loginService.saveRefreshToken(data.refresh_token);
            this.loginService.isAuthenticated.next(true);
            this.loginService.startTokenRefresh(data.access_token);

            if (this.tokenUtilsService.isAdministrator()) {
              this.toastrService.onError(this.translatePipe.transform('users.errors.LOGIN_FAILED'), this.translatePipe.transform('menu.ERROR'));
              this.loginService.clearUserData();
              return;
            }
            if (this.tokenUtilsService.isInt()) {
              this.redirectUser();
              return;
            }
            if (
                this.tokenUtilsService.isAuthenticated() &&
                this.tokenUtilsService.isUserExternal() &&
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
              this.camundaService.startProcessOnceByKey(this.processDefKey, vars).subscribe(() => {
                this.toastrService.onInfo(this.translatePipe.transform('process.YOU_HAVE_TASK_TO_DO_PP'), this.translatePipe.transform('menu.INFO'));
              });
            }
            else{
              this.toastrService.onInfo(this.translatePipe.transform('process.DO_PP_TASK'), this.translatePipe.transform('menu.INFO'));
            }
            this.toastrService.onSuccess(this.translatePipe.transform('users.LOGIN_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
            this.redirectUser();
          }
        },
        error: () => {
          this.toastrService.onError(this.translatePipe.transform('users.errors.LOGIN_FAILED'), this.translatePipe.transform('menu.ERROR'));
          this.loginService.clearUserData();
          this.loaderService.hide();
        },
        complete: () => {
          this.checkFirstConnection();
          this.loaderService.hide();
        }
      });
    }
  }

  onSubmit(): void {
    this.checkCaptchaValue();
  }

  private redirectUser(): void {
    if (!this.tokenUtilsService.isAuthenticated()) {
      this.loginService.clearUserData();
      this.router.navigate(['/auth/login']);
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
      this.router.navigate(['/auth/forgot-password']);
    }
  }

  public goBack(): void {
    this.router.navigate(['/auth']);
  }
}
