import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NgbDropdown, NgbDropdownMenu, NgbDropdownToggle} from '@ng-bootstrap/ng-bootstrap';
import {TranslateModule, TranslateService} from '@ngx-translate/core';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-language-switcher',
  standalone: true,
  imports: [
    NgbDropdown,
    NgbDropdownMenu,
    NgbDropdownToggle,
    TranslateModule,
    NgClass
  ],
  templateUrl: './language-switcher.component.html',
  styleUrl: './language-switcher.component.scss'
})
export class LanguageSwitcherComponent implements OnInit{
  public selectedLanguage: string;
  @Output() languageChanged: EventEmitter<string> = new EventEmitter<string>();

  constructor(
    private translateService: TranslateService) {}

  ngOnInit() {
    this.selectedLanguage = localStorage.getItem('language');
    this.translateService.setDefaultLang(this.selectedLanguage);
    this.translateService.use(this.selectedLanguage);
    this.updateRtlLayout();
  }
  public switchLanguage(lang: string, isRtl: boolean = false) {
    this.translateService.setDefaultLang(lang);
    this.selectedLanguage = lang;
    localStorage.setItem('language', lang);
    this.translateService.use(lang);
    this.setRtlLayout(isRtl);
    this.languageChanged.emit(lang);
  }

  private changeRtlLayout(flag: boolean): void {
    const body = document.querySelector('body');
    if (flag) {
      body?.classList.add('bs-theme-rtl');
    } else {
      body?.classList.remove('bs-theme-rtl');
    }
  }

  private setRtlLayout(isRtl: boolean): void {
    this.changeRtlLayout(isRtl);
  }

  private isRtlLanguage(lang: string): boolean {
    return lang === 'ar';
  }

  private updateRtlLayout(): void {
    const isRtl = this.isRtlLanguage(this.selectedLanguage);
    const body = document.querySelector('body');
    if (isRtl) {
      body?.classList.add('bs-theme-rtl');
    } else {
      body?.classList.remove('bs-theme-rtl');
    }
  }

}
