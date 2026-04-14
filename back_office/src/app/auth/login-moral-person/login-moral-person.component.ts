import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-moral-person',
  templateUrl: './login-moral-person.component.html',
  styleUrl: './login-moral-person.component.scss'
})
export class LoginMoralPersonComponent implements OnInit {
  constructor(private router: Router) {
  }

  ngOnInit(): void {

  }

  loginRNE() {
    this.router.navigate(['/auth/rne-login']);
  }

  loginDIGIGO() {
    console.log('Authenticating by DIGIGO');

  }

  goBack() {
    this.router.navigate(['/auth/welcome']);
  }
}
