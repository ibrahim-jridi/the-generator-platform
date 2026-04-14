import {Component, OnInit} from '@angular/core';
import {TranslateModule, TranslatePipe, TranslateService} from "@ngx-translate/core";
import {ActivatedRoute, Router} from "@angular/router";
import {LoginService} from "../../shared/services/login.service";
import {CommonModule} from "@angular/common";
import decode from "jwt-decode";
import {AppToastNotificationService} from "../../shared/services/appToastNotification.service";
import {NgbDropdown, NgbDropdownMenu, NgbDropdownToggle} from '@ng-bootstrap/ng-bootstrap';
import {
  LanguageSwitcherComponent
} from '../../shared/language-switcher/language-switcher.component';

@Component({
  selector: 'app-mail-activation',
  standalone: true,
  imports: [
    TranslateModule,
    CommonModule,
    NgbDropdown,
    NgbDropdownMenu,
    NgbDropdownToggle,
    LanguageSwitcherComponent
  ],
  templateUrl: './mail-activation.component.html',
  styleUrl: './mail-activation.component.scss'
})
export class MailActivationComponent implements OnInit{
  isActivated: boolean = false;
  isLoading:boolean = true;
  isTokenExpired: boolean = false ;
  token: string = '';
  public constructor(private route:ActivatedRoute,
                     private loginService: LoginService,
                     private router: Router,
                     private toastrService: AppToastNotificationService,
                     private translatePipe: TranslatePipe,
                     private translateService: TranslateService) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
      let decodedToken: any = decode(this.token)
      if (!decodedToken.exp) {
        decodedToken = null;
      }
      if (decodedToken && decodedToken.exp && decodedToken.exp > Math.floor(Date.now() / 1000)) {
        this.ActivationMail(this.token);
      } else {
        this.isTokenExpired = true ;
        this.isActivated = false;
        this.isLoading = false;
      }
    });
  }
  ActivationMail(token : string) {
    this.loginService.activationEmail(token).subscribe(
      res => {
        this.isActivated = true;
        this.isLoading = false;
        this.toastrService.onSuccess(
            this.translatePipe.transform('users.SEND_ACTIVATION_EMAIL_SUCCESS'),
            this.translatePipe.transform('menu.SUCCESS')
        );
      },
      error => {
        this.isLoading = false;
        this.toastrService.onError(
            this.translatePipe.transform('users.SEND_ACTIVATION_EMAIL_ERROR'),
            this.translatePipe.transform('menu.ERROR')
        );

      }
    );
  }

  goToLoginPage() {
    this.router.navigate(['/auth/login']);
  }



  resendVerificationMail() {
    this.loginService.resendVerificationMailJWTToken(this.token).subscribe(res => {
      this.router.navigate(['/auth/login']);
    });

  }

}

