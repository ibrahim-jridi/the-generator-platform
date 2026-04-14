import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../../shared/services/login.service';
import decode from 'jwt-decode';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { TokenUtilsService } from '../../shared/services/token-utils.service';
import { ResetPasswordRequest } from '../../shared/models/reset-password.model';
import { AppToastNotificationService } from '../../shared/services/appToastNotification.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.scss'
})
export class ResetPasswordComponent implements OnInit {
  protected resetPasswordForm: FormGroup;
  protected showNewPassword = false;
  protected showConfirmPassword = false;
  protected keycloakToken: boolean = false;
  private token: any;
  protected username: string = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private loginService: LoginService,
    private translateService: TranslateService,
    private translatePipe: TranslatePipe,
    private toasterService: AppToastNotificationService,
    private jwtUtils: TokenUtilsService
  ) {}

  public ngOnInit(): void {
    this.initForm();
    this.route.queryParams.subscribe((params) => {
      this.token = params['token'];
    });
    this.checkToken();
  }

  protected toggleShowNewPassword(): void {
    this.showNewPassword = !this.showNewPassword;
  }

  protected toggleShowConfirmPassword(): void {
    this.showConfirmPassword = !this.showConfirmPassword;
  }

  private passwordsMatch(group: FormGroup): { [key: string]: boolean } | null {
    const newPassword = group.get('newPassword').value;
    const confirmPassword = group.get('confirmPassword').value;
    return newPassword === confirmPassword ? null : { passwordsMismatch: true };
  }

  protected onSubmit(): void {
    if (this.resetPasswordForm.valid) {
      if (this.keycloakToken) {
        this.firstConnectionSubmit();
      } else {
        this.forgetPasswordSubmit();
      }
    }
  }

  protected onCancel(): void {
    this.router.navigate(['/auth/login']);
  }

  private regexValidator(regex: RegExp, error: ValidationErrors): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } => {
      if (!control.value) {
        return null;
      }
      const valid = regex.test(control.value);
      return valid ? null : error;
    };
  }

  private initForm() {
    this.resetPasswordForm = this.fb.group(
      {
        newPassword: [
          '',
          [
            Validators.required,
            this.regexValidator(new RegExp('^.{8,}'), { precision: true }),
            this.regexValidator(new RegExp('^.*[a-z].*'), { miniscule: true }),
            this.regexValidator(new RegExp('^.*[A-Z].*'), { majiscule: true }),
            this.regexValidator(new RegExp('^.*[0-9].*'), { digit: true }),
            this.regexValidator(new RegExp('^.*[\\p{P}\\p{S}].*', 'u'), { specialchar: true }),
          ]
        ],
        confirmPassword: ['', Validators.required]
      },
      { validator: this.passwordsMatch }
    );
  }

  private checkToken(): void {
    if (this.token == null) {
      this.token = sessionStorage.getItem('token');
      this.keycloakToken = true;
      this.username = this.jwtUtils.getUsername();
    } else {
      this.username = this.jwtUtils.getUserName(this.token);
    }
  }

  private forgetPasswordSubmit(): void {
    let decodedToken: any = decode(this.token);
    if (!decodedToken.exp) {
      decodedToken = null;
    }
    if (decodedToken && decodedToken.exp && decodedToken.exp > Math.floor(Date.now() / 1000)) {
      this.submit(this.token);
    } else {
      this.toasterService.onError(this.translatePipe.transform('login.TOKEN_EXPIRED'), this.translatePipe.transform('menu.ERROR'));
    }
  }

  private firstConnectionSubmit(): void {
    let resetPasswordRequest: ResetPasswordRequest = new ResetPasswordRequest();
    resetPasswordRequest.username = this.username;
    resetPasswordRequest.newPassword = this.resetPasswordForm.get('newPassword').value;
    resetPasswordRequest.confirmPassword = this.resetPasswordForm.get('confirmPassword').value;
    this.loginService.firstConnectionResetPassword(resetPasswordRequest).subscribe({
      next: () => {
        this.toasterService.onSuccess(
          this.translatePipe.transform('login.PASSWORD_RESET_SUCCESSFULLY'),
          this.translatePipe.transform('menu.SUCCESS')
        );
        if (this.jwtUtils.isAdministrator()) {
          this.router.navigate(['/auth/login-admin']);
        } else if (!this.jwtUtils.isAdministrator() && !this.jwtUtils.isPM()) {
          this.router.navigate(['/auth/login']);
        } else if (this.jwtUtils.isPM()) {
          this.router.navigate(['/auth/login-moral-person']);
        } else {
          this.router.navigate(['/auth']);
        }
        this.jwtUtils.clearToken();

      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('login.PASSWORD_RESET_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  private submit(token: string): void {
    let resetPasswordRequest: ResetPasswordRequest = new ResetPasswordRequest();
    resetPasswordRequest.username = this.username;
    resetPasswordRequest.newPassword = this.resetPasswordForm.get('newPassword').value;
    resetPasswordRequest.confirmPassword = this.resetPasswordForm.get('confirmPassword').value;
    this.loginService.resetPassword(resetPasswordRequest, token).subscribe({
      next: () => {
        this.toasterService.onSuccess(
          this.translatePipe.transform('login.PASSWORD_RESET_SUCCESSFULLY'),
          this.translatePipe.transform('menu.SUCCESS')
        );
        this.router.navigate(['/auth']);
      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('login.PASSWORD_RESET_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }
}
