import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../../shared/services/login.service';
import { AppToastNotificationService } from '../../shared/services/appToastNotification.service';
import { TranslateModule, TranslatePipe } from '@ngx-translate/core';
import { TokenUtilsService } from '../../shared/services/token-utils.service';
import { MatIcon } from '@angular/material/icon';
import { NgClass, NgIf } from '@angular/common';
import { LanguageSwitcherComponent } from '../../shared/language-switcher/language-switcher.component';

@Component({
  selector: 'app-rne-login',
  standalone: true,
  imports: [TranslateModule, MatIcon, ReactiveFormsModule, NgIf, LanguageSwitcherComponent, NgClass],
  templateUrl: './rne-login.component.html',
  styleUrl: './rne-login.component.scss'
})
export class RneLoginComponent implements OnInit {
  public loginForm: FormGroup;
  public fieldTextType: boolean = false;
  public language: string;
  public currentLanguage: string;

  constructor(
    private readonly router: Router,
    private readonly loginService: LoginService,
    private readonly toasterService: AppToastNotificationService,
    private readonly translatePipe: TranslatePipe,
    private readonly tokenUtilsService: TokenUtilsService
  ) {}

  ngOnInit(): void {
    this.currentLanguage = localStorage.getItem('language') || 'fr';
    this.language = this.currentLanguage.charAt(0).toUpperCase() + this.currentLanguage.slice(1).toLowerCase();
    this.loginForm = new FormGroup({
      fiscalIdOrUsername: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      rememberMe: new FormControl(true)
    });
  }

  public onSubmit(): void {
    if (this.loginForm.valid) {
      this.loginService.rneLogin(this.loginForm.value).subscribe({
        next: (data: any) => {
          if (data.access_token) {
            this.toasterService.onSuccess(this.translatePipe.transform('users.LOGIN_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
            this.loginService.saveToken(data.access_token);
            this.redirectUser();
          }
        },
        error: () => {
          this.toasterService.onError(this.translatePipe.transform('users.errors.INVALID_USERNAME_OR_PASSWORD'), this.translatePipe.transform('menu.ERROR'));
          this.loginService.clearUserData();
        } ,
        complete : () => {
          this.checkFirstConnection();
        }
      });
    }
  }

  private checkFirstConnection() : void {
    if (this.tokenUtilsService.getFirstConnection()) {
      this.router.navigate(['/auth/forgot-password'])
    }
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

  public goBack(): void {
    this.router.navigate(['/auth/login-moral-person']);
  }

  protected onLanguageChanged(newLanguage: string): void {
    this.language = newLanguage.charAt(0).toUpperCase() + newLanguage.slice(1).toLowerCase();
  }
}
