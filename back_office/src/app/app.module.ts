import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NavBarComponent} from './theme/layout/admin/nav-bar/nav-bar.component';
import {NavLeftComponent} from './theme/layout/admin/nav-bar/nav-left/nav-left.component';
import {ConfigurationComponent} from './theme/layout/admin/configuration/configuration.component';
import {NavRightComponent} from './theme/layout/admin/nav-bar/nav-right/nav-right.component';
import {NavigationComponent} from './theme/layout/admin/navigation/navigation.component';
import {
  NavContentComponent
} from './theme/layout/admin/navigation/nav-content/nav-content.component';
import {AdminComponent} from './theme/layout/admin/admin.component';

import {
  NavGroupComponent
} from './theme/layout/admin/navigation/nav-content/nav-group/nav-group.component';
import {
  NavCollapseComponent
} from './theme/layout/admin/navigation/nav-content/nav-collapse/nav-collapse.component';
import {ToggleFullScreenDirective} from './shared/full-screen/toggle-full-screen';
import {NavigationItem} from './theme/layout/admin/navigation/navigation';
import {SharedModule} from './theme/shared/shared.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgScrollbarModule} from 'ngx-scrollbar';
import {NgbDropdownModule, NgbNavModule, NgbTooltipModule} from '@ng-bootstrap/ng-bootstrap';
import {
  NavItemComponent
} from './theme/layout/admin/navigation/nav-content/nav-item/nav-item.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {SnotifyModule, SnotifyService, ToastDefaults} from 'ng-alt-snotify';
import {AppToastNotificationService} from './shared/services/appToastNotification.service';
import {RequestInterceptor} from './shared/interceptors/request.interceptor';
import {TokenUtilsService} from './shared/services/token-utils.service';
import {LanguageSwitcherComponent} from './shared/language-switcher/language-switcher.component';
import {FooterComponent} from './theme/layout/admin/footer/footer.component';
import {ModalService} from './shared/services/modal.service';
import {ModalComponent} from './shared/modal/modal.component';
import {LoaderComponent} from './shared/loader/loader.component';
import {AuthHeaderInterceptor} from "./shared/interceptors/auth-header.interceptor";

export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent,
    AdminComponent,
    NavigationComponent,
    NavContentComponent,
    NavGroupComponent,
    NavCollapseComponent,
    NavItemComponent,
    NavBarComponent,
    NavLeftComponent,
    NavRightComponent,
    ConfigurationComponent,
    FooterComponent,
    ToggleFullScreenDirective,
    ModalComponent,
    LoaderComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    SharedModule,
    NgScrollbarModule,
    NgbDropdownModule,
    NgbTooltipModule,
    NgbNavModule,
    TranslateModule.forRoot({
      defaultLanguage: 'en',
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    SnotifyModule,
    LanguageSwitcherComponent
  ],
  providers: [
    NavigationItem,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: RequestInterceptor,
      multi: true
    },
    {provide: HTTP_INTERCEPTORS, useClass: AuthHeaderInterceptor, multi: true},
    AppToastNotificationService,
    {provide: 'SnotifyToastConfig', useValue: ToastDefaults},
    SnotifyService,
    ModalService,
    TokenUtilsService
  ],
  exports: [NavCollapseComponent],
  bootstrap: [AppComponent]
})
export class AppModule {
}
