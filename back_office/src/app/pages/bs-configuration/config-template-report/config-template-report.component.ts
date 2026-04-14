import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-config-template-report',
  templateUrl: './config-template-report.component.html',
  styleUrl: './config-template-report.component.scss'
})
export class ConfigTemplateReportComponent {

  editReport = false;
  reportTemplateId: string;

  public toggleDetails(edit: boolean, data?: string): void {
    this.editReport = edit;
    if (data) {
      this.reportTemplateId = data;
    }
  }
}
