import { Component, OnInit } from '@angular/core';
import { Router} from '@angular/router';
import {LoginService} from "../../shared/services/login.service";
import {AppToastNotificationService} from "../../shared/services/appToastNotification.service";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-welcome-page',
  templateUrl: './mail-verification.component.html',
  styleUrl: './mail-verification.component.scss'
})
export class MailVerificationComponent implements OnInit{
  token: any;

  constructor( private router: Router, private loginService: LoginService,
               private toastrService: AppToastNotificationService, private translatePipe: TranslatePipe) {
  }
  ngOnInit(): void {
    this.token = sessionStorage.getItem('token');
  }

  resendVerificationMail() {
    this.loginService.resendVerificationMail(this.token).subscribe(res => {
    });
    this.router.navigate(['/auth/login']);
  }

}
