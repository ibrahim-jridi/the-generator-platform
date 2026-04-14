import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-welcome-page',
  templateUrl: './welcome-page.component.html',
  styleUrl: './welcome-page.component.scss'
})
export class WelcomePageComponent implements OnInit {
  public language: string;
  public currentLanguage: string;

  constructor(private router: Router, private translateService: TranslateService) {}
  ngOnInit(): void {
    this.currentLanguage = localStorage.getItem('language') || 'fr';
    this.language = this.currentLanguage.charAt(0).toUpperCase() + this.currentLanguage.slice(1).toLowerCase();
  }
  onPhysicalPersonAuth() {
    this.router.navigate(['/auth/login']);
  }

  onMoralPersonAuth() {
    this.router.navigate(['/auth/login-moral-person']);
  }
  onAdminAuth() {
    this.router.navigate(['/auth/login-admin']);
  }

  protected onLanguageChanged(newLanguage: string): void {
    this.language = newLanguage.charAt(0).toUpperCase() + newLanguage.slice(1).toLowerCase();
  }
}
