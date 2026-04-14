import { LOCALE_ID, NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { AuthRoutingModule } from './auth-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { WelcomePageComponent } from './welcome-page/welcome-page.component';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { LoginMoralPersonComponent } from './login-moral-person/login-moral-person.component';
import { SignupComponent } from './signup/signup.component';
import { MatStepperModule } from '@angular/material/stepper';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';
import { MailVerificationComponent } from './mail-verification/mail-verification.component';
import { TranslateModule } from '@ngx-translate/core';
import { NgbDropdown, NgbDropdownMenu, NgbDropdownToggle } from '@ng-bootstrap/ng-bootstrap';
import { LanguageSwitcherComponent } from '../shared/language-switcher/language-switcher.component';
import { MatCheckbox } from '@angular/material/checkbox';
import { PreSignupComponent } from './pre-signup/pre-signup.component';
import { RneLoginComponent } from './rne-login/rne-login.component';
import { ModalModule } from '../theme/shared/components';
import { SharedModule } from '../theme/shared/shared.module';
import {LoginAdminComponent} from "./login-admin/login-admin.component";
import { NgxCaptchaModule } from '@binssoft/ngx-captcha';

@NgModule({
  declarations: [
    LoginComponent,
    WelcomePageComponent,
    ResetPasswordComponent,
    LoginMoralPersonComponent,
    SignupComponent,
    MailVerificationComponent,
    PreSignupComponent,
    LoginAdminComponent
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    ReactiveFormsModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatStepperModule,
    MatRadioModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    TranslateModule,
    NgbDropdown,
    NgbDropdownMenu,
    NgbDropdownToggle,
    LanguageSwitcherComponent,
    MatCheckbox,
    RneLoginComponent,
    ModalModule,
    SharedModule,
    NgxCaptchaModule
  ],
  providers: [
    { provide: MAT_DATE_LOCALE, useValue: 'fr-FR' },
    { provide: DatePipe, useClass: DatePipe },
    { provide: LOCALE_ID, useValue: 'fr-FR' },
    {
      provide: MAT_DATE_FORMATS,
      useValue: {
        parse: { dateInput: 'DD/MM/YYYY' },
        display: {
          dateInput: 'DD/MM/YYYY', // Display format in input
          monthYearLabel: 'MMMM YYYY', // Header format in calendar,
          dateA11yLabel: 'LL', // Accessibility format for date
          monthYearA11yLabel: 'MMMM YYYY' // Accessibility format for month and year
        }
      }
    }
  ]
})
export class AuthModule {}
