import {Component, Inject} from '@angular/core';
import {MAT_DATE_LOCALE, NativeDateAdapter} from "@angular/material/core";
import {TranslateService} from "@ngx-translate/core";
import {MatCalendarHeader} from "@angular/material/datepicker";

@Component({
  selector: 'app-custom-date-adapter',
  templateUrl: './custom-date-adapter.component.html',
  styleUrls: ['./custom-date-adapter.component.scss']
})

export class CustomDateAdapterComponent extends NativeDateAdapter {
  protected locate: string;

  constructor(
    private translateService: TranslateService,
    @Inject(MAT_DATE_LOCALE) protected override locale: string
  ) {
    super(locale);
    this.locate = this.translateService.currentLang;
  }

  override getFirstDayOfWeek(): number {
    return 1;
  }

  override getDayOfWeekNames(style): string[] {
    const SHORT_NAMES_EN = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    const SHORT_NAMES_FR = ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'];
    if (this.locale === 'fr') {
      return SHORT_NAMES_FR;
    } else {
      return SHORT_NAMES_EN;
    }
  }

  override getMonthNames(style): string[] {
    const LONG_MONTH_NAMES_EN = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    const LONG_MONTH_NAMES_FR = ['Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'];
    if (this.locale === 'fr') {
      return LONG_MONTH_NAMES_FR;
    } else {
      return LONG_MONTH_NAMES_EN;
    }
  }
}
