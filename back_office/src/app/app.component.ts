import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {GlobalService} from './shared/services/global.service';
import {LoaderService} from './shared/services/loader.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  public title: string = 'BS_Front';
  public styleSnotify = 'material';
  private navigatorLanguages = ['fr', 'en', 'ar'];

  constructor(
    private router: Router,
    private translateService: TranslateService,
    private globalService: GlobalService,
    protected loaderService: LoaderService
  ) {
    this.initApplicationLanguage();
  }

  ngOnInit() {
    this.router.events.subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
      if(evt.url == "/auth/login"){
        sessionStorage.clear();
      }
      sessionStorage.setItem('BASE_USER_MANAGEMENT_URL', this.globalService.BASE_USER_MANAGEMENT_URL);
      sessionStorage.setItem('BASE_URL', this.globalService.BASE_URL);
      window.scrollTo(0, 0);
    });

  }
  initApplicationLanguage(): void {
    const navigatorLanguage = navigator.language.split('-')[0];
    const language = this.navigatorLanguages.indexOf(navigatorLanguage) > -1 ? navigatorLanguage : 'fr';
    localStorage.setItem('language', language);
    this.translateService.use(language.toLowerCase());
  }
}
