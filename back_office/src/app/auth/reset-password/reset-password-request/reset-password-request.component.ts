import { Component, OnInit } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { NgIf } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SharedModule } from '../../../theme/shared/shared.module';
import { TranslateModule, TranslatePipe, TranslateService } from '@ngx-translate/core';
import { LoginService } from '../../../shared/services/login.service';
import { AppToastNotificationService } from '../../../shared/services/appToastNotification.service';
import { NgbDropdown, NgbDropdownMenu, NgbDropdownToggle } from '@ng-bootstrap/ng-bootstrap';
import { LanguageSwitcherComponent } from '../../../shared/language-switcher/language-switcher.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reset-password-request',
  standalone: true,
  imports: [
    MatIcon,
    NgIf,
    ReactiveFormsModule,
    SharedModule,
    TranslateModule,
    NgbDropdown,
    NgbDropdownMenu,
    NgbDropdownToggle,
    LanguageSwitcherComponent
  ],
  templateUrl: './reset-password-request.component.html',
  styleUrl: './reset-password-request.component.scss'
})
export class ResetPasswordRequestComponent implements OnInit {
  resetPasswordRequestForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private loginService: LoginService,
    private toastrService: AppToastNotificationService,
    private translatePipe: TranslatePipe,
    private translateService: TranslateService,
    private router: Router
  ) {}

  public ngOnInit(): void {
    this.resetPasswordRequestForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.resetPasswordRequestForm.valid) {
      this.loginService
        .sendResetPassword(this.resetPasswordRequestForm.get('email').value, this.resetPasswordRequestForm.get('username').value)
        .subscribe({
          next: (res) => {
            this.toastrService.onSuccess(
              this.translatePipe.transform('resetPassword.RESET_REQUEST_SEND_SUCCESS'),
              this.translatePipe.transform('resetPassword.RESET_REQUEST_SEND_SUCCESS')
            );
            this.router.navigate(['/auth']);
          },
          error: (err) => {
            this.toastrService.onError(
              this.translatePipe.transform('resetPassword.RESET_REQUEST_SEND_ERROR'),
              this.translatePipe.transform('resetPassword.RESET_REQUEST_SEND_ERROR')
            );
          }
        });
    }
  }

  protected onCancel(): void {
    this.router.navigate(['/auth']);
  }
}
